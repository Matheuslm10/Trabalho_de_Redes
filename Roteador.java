// T2 - Redes de Computadores
// Grupo: Ana Flavia Maia Barbosa, Matheus Lima Machado, Nathaly Veneruchi Garcia.
// Semestre: 2017.2

import java.util.Arrays;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Roteador {
	
	//captura os dados existentes na String e aloca em posicoes de um vetor.
	public static String[] desencapsulamento(String dados) {
		String dadosVetor[] = new String[3];
		int indexEspaco[] = new int[2];
		int contador = 0;
		dados = trimAll(dados);
		for (int i = 0; i < dados.length(); i++) {
			if(dados.charAt(i) == ' '){
				if(contador < indexEspaco.length){
			indexEspaco[contador] = i;
				contador++;
				}
			}
		}
		
		dadosVetor[0] = dados.substring (0, indexEspaco[0]);
		dadosVetor[1] = dados.substring (indexEspaco[0]+1, indexEspaco[1]);
		dadosVetor[2] = dados.substring(indexEspaco[1]+1, dados.length());
		
		return dadosVetor;
		
	}
	
	//transforma varios espaços em branco em um unico.
	public static String trimAll(String text){
		String string = text.trim();
		while (string.contains("  ")) {
			string = string.replaceAll("  ", " ");
		}
		return string;
	}
	
	//cria um vetor de "32 bits" para casos em que a mascara esta em formato CIDR.
	public static int[] criaVetorBinarioAbreviado(String mascara) {
		int vetorEmBinario[] = new int[32];
		int mask = Integer.parseInt(mascara);
		
		for (int i = 0; i < mask; i++) {
			vetorEmBinario[i] = 1;
		}	
		
		return vetorEmBinario;
	}
	
	//cria um vetor de "32 bits" para casos em que a mascara esta em formato padrao.
	public static int[] criaVetorBinario(String mascara[]){
		int vetorEmBinario[]= new int[32];
		
		String alvo;
		int octeto = 0;
		
		for(int i=0; i < 4; i++) {
			int x = Integer.parseInt(mascara[i]);
			alvo =Integer.toBinaryString(x);
			String output = String.format("%8s", alvo).replace(' ', '0');
			char octetoDaVez[] = output.toCharArray();
			for(int j = 0; j < 8; j++) {				
				vetorEmBinario[j+octeto] = Character.getNumericValue(octetoDaVez[j]) ;
			}
			octeto += 8;
		}
		return vetorEmBinario;
	}
	
	//se a mascara for do formato CIDR, retorna "true".
	public static boolean verificaMascara(String mascara){
		boolean abreviado = true;
		int qtdDePontos = mascara.indexOf(".");
		if(qtdDePontos!= -1) {
			abreviado = false;
		}
		return abreviado;
	}


	public static void main(String[] args) throws IOException {
		System.out.println("* Roteador Iniciado *");
		try {
			//lê as entradas: porta e tabela
			Scanner leitor = new Scanner(System.in);
			String porta = leitor.next();
			String entrada = leitor.nextLine();
			entrada = entrada.substring (1, entrada.length());	
			entrada = trimAll(entrada);
			
			//contar quantas barras tem;
			int contador = 0;
			for (int i=0; i< entrada.length(); i++){
				if(entrada.charAt(i)=='/'){
					 contador++;
				}
			}
			//separa os dados da entrada
			int qtdLinhas = contador/3;
			String linhas[] = new String[qtdLinhas];
			linhas = entrada.split(" ");		
			String tabela[][] = new String[qtdLinhas][4];
			
			//insere os dados em uma matriz
			for (int i = 0; i < qtdLinhas; i++) {// linha
				String linhaDaVez[]= new String[4];
				linhaDaVez = linhas[i].split("/");
				
				for (int j = 0; j < 4; j++){				
					tabela[i][j] = linhaDaVez[j];
				}
			}			
			
			//cria um socket na porta lida da entrada.
			int portaNumero = Integer.parseInt(porta);
			DatagramSocket socket = new DatagramSocket(portaNumero);
				
				while(true) {
						
					//cria o espaço para a mensagem.		
					byte [] b = new byte[100];
					
					//cria o pacote, dizendo qual é o tamanho(b) da String que contem os dados que ele deve acomodar.
					DatagramPacket pacote = new DatagramPacket(b, b.length);
					
					//faz o socket esperar por um pacote de tamanho b.
					socket.receive(pacote);
					
					//identifica no payload do pacote os dados, e os armazena. 
					String msg = new String (b);		
					String dados[] = desencapsulamento(msg);
					int redeAlvoDestino[];
					
					//transformando para um vetor de "32 bits" a rede destino do pacote. 
					String redeDestinoDoPacote = dados[1];
					String rede[] = redeDestinoDoPacote.split("\\.");
					redeAlvoDestino = criaVetorBinario(rede).clone();
										
					//vetor para armazenar resultado da operacao "& logico"
					int resultado[]= new int[32];
					int maiorMascara[] = new int[32];
					
					//variaveis utilizadas na estrategia de decisao de operacao
					String operacao;
					int countAtual=0;
					int countAntigo=0;
					int candidata = 0;					
					boolean casou = false;
					
					/* percorrendo a tabela de roteamento e verificando quais linhas da 
					  coluna Destino (da tabela de roteamento) casam com o destino do pacote. */
					for (int i = 0; i < tabela.length; i++) {
						String mask = tabela[i][1];	
						int mascaraVetor[];
						//verifica se a mascara esta no formato CIDR.
						if(verificaMascara(mask)) {
							mascaraVetor = criaVetorBinarioAbreviado(mask).clone();
							
						}else {
							String mascara[] = mask.split("\\.");
							mascaraVetor= criaVetorBinario(mascara).clone();
						}
						
						countAtual = 0;
						/* faz "AND logico" "bit a bit" entre a rede de destino do 
						pacote e a mascara da linha (da tabela de roteamento) em questao. */ 
						for (int j = 0; j < 32; j++) {
							if( redeAlvoDestino[j] == 0 || mascaraVetor[j] == 0) {
								resultado[j] = 0;			
							}else {
								resultado[j] = 1;
							}
						}
						
						/* cria um vetor de "32 bits" para a rede de destino indicada pela 
						tabela de roteamento. */
						String redeDestinoDaTabela = tabela[i][0];
						String destino[] = redeDestinoDaTabela.split("\\.");
						int destinoVetor[] = criaVetorBinario(destino).clone();
						
						/* entra no "if" se a rede da linha da coluna Destino (da tabela de 
						 * roteamento) casa com o destino do pacote.*/
						if(Arrays.equals(resultado, destinoVetor)) {
							casou = true;
							for(int x = 0; x < 32; x++) {
								if(mascaraVetor[x]==1) {
									countAtual++;	
								}
							}
							
							if(countAtual >= countAntigo) {
								countAntigo = countAtual;
								//a maior mascara até o momento eh:
								candidata = i;
								maiorMascara = mascaraVetor.clone();
								
							}
						}
						
					}
					
					String portaDaTabela = tabela[candidata][3];
					String nextHop = tabela[candidata][2];
					if(casou == false) {
						operacao = "descarte";
					}else if(portaDaTabela.equals("0") && casou) {
						operacao = "finalizacao";
					}else{
						operacao = "encaminhamento";
					}
									
					
					/* A partir daqui o programa pode tomar 3 fluxos diferentes: encaminhamento, 
					   finalizacao ou descarte. */ 
					
					if(operacao.equals("encaminhamento")){
					
						//cria um pacote, coloca os dados no payload dele e define a porta que ele deve ser enviado
						byte [] bb = msg.getBytes();
						InetAddress ip = InetAddress.getByName("localhost");
						DatagramPacket pacoteDeEnvio = new DatagramPacket(bb, bb.length, ip, Integer.parseInt(portaDaTabela));
						
						socket.send(pacoteDeEnvio);
					
						/* print informando os dados desencapsulados do pacote  (ip origem, ip destino, interface) relevantes 
						para esta operacao (encaminhamento) que o roteador decidiu executar. */
						System.out.println("forwarding packet for "+dados[1]+" to next hop "+nextHop+" over interface "+portaDaTabela);
						
						
					}else if(operacao.equals("finalizacao")){
					
						/* print informando os dados desencapsulados do pacote  (ip origem, ip destino, conteudo da mensagem) relevantes 
						para esta operacao que o roteador decidiu executar. */ 
						System.out.println("destination reached. From "+dados[0]+" to "+dados[1]+" : "+dados[2]);
					
					}else if(operacao.equals("descarte")){
						
						/* print informando os dados desencapsulados do pacote  (ip origem) relevantes 
						para esta operacao (descarte) que o roteador decidiu executar. */
						System.out.println("destination "+dados[1]+" not found in routing table, dropping packet");
					}
				}
			}catch (IOException ioe) {
				System.err.println(ioe);
			}
		

	}

}