// T2 - Redes de Computadores
// Grupo: Ana Flavia Maia Barbosa, Matheus Lima Machado, Nathaly Veneruchi Garcia.
// Semestre: 2017.2

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Emissor {
	
	//captura os dados existentes na String e aloca em posicoes de um vetor.
	public static String[] desencapsulamento(String dados) {
		String dadosVetor[] = new String[5];
		int indexEspaco[] = new int[4];
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
		dadosVetor[2] = dados.substring(indexEspaco[1]+1, indexEspaco[2]);
		dadosVetor[3] = dados.substring (indexEspaco[2]+1, indexEspaco[3]);
		dadosVetor[4] = dados.substring (indexEspaco[3]+1, dados.length());
		
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
	
	public static void main(String[] args) throws IOException {
		System.out.println("* Emissor Iniciado *");
		boolean continua = true;
		try {
			while(continua){
				//lê as entradas: ipX - porta - ipOrigem - ipDestino - msg
				Scanner leitor = new Scanner(System.in);
				System.out.println("Digite uma nova entrada:");
				String entrada = leitor.nextLine();
				
				//chave para encerrar o programa.
				if(entrada.equals("SAIR")) {
					continua = false;
					System.out.println("Emissor encerrado!");
					break;
				}
				
				String vetorDados[] = desencapsulamento(entrada).clone();
				
				//porta = leitura;
				int porta = Integer.parseInt(vetorDados[1]);
				
				//dados = (ipOrigem + ipDestino + msg);
				String dados = vetorDados[2] +" "+ vetorDados[3] +" "+ vetorDados[4];
				
				//cria socket.
				DatagramSocket socket = new DatagramSocket();
				
				//transforma os dados lidos da entrada para um vetor de bytes.
				byte [] b = dados.getBytes();
				
				InetAddress ip = InetAddress.getByName("localhost");
				
				//insere no pacote os dados, o ip e a porta.
				DatagramPacket pacote = new DatagramPacket(b, b.length, ip, porta);
				
				socket.send(pacote);
			}
			
		}catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}