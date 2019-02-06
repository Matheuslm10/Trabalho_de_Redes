# Trabalho_de_Redes
Sistema que simula o encaminhamento de pacotes em uma rede, passando por vários roteadores.

Execução no prompt de comando:

>Para o seguinte caso: 1 emissor e 3 roteadores.
>Abra 4 janelas de prompt de comando.
>Compile o programa Roteador.java em 1 janela e o Emissor.java em outra.
>Em 3 janelas, execute o Roteador.java para cada uma delas.
>A seguinte mensagem deve ser exibida em cada uma das 3 janelas: “*Roteador Iniciado*”.
>Então, insira as entradas:
  Por exemplo:
>Em uma janela, insira (sem quebra de linha):
12345  10.0.0.0/255.0.0.0/0.0.0.0/0 150.1.0.0/255.255.0.0/0.0.0.0/0 30.1.2.0/255.255.255.0/127.0.0.1/12346 0.0.0.0/0.0.0.0/127.0.0.1/12346

>Pressione “Enter”. (Não deve ser exibido nenhuma mensagem).
>Em outra janela, insira (sem quebra de linha):
12346  20.10.0.0/255.255.0.0/0.0.0.0/0 0.0.0.0/0.0.0.0/127.0.0.1/12347 30.1.2.0/255.255.255.0/0.0.0.0/0

>Pressione “Enter”. (Não deve ser exibido nenhuma mensagem).
>E em outra, insira (sem quebra de linha):
12347  30.1.0.0/255.255.0.0/0.0.0.0/0  20.16.0.0/255.240.0.0/0.0.0.0/0 20.10.0.0/255.255.0.0/127.0.0.1/12346 30.1.64.0/255.255.255.0/127.0.0.1/4444

Pressione “Enter”. (Não deve ser exibido nenhuma mensagem).
>Na quarta janela, que ainda não foi utilizada, execute o Emissor.java.
>A seguinte mensagem deve ser exibida nesta quarta janela: “ *Emissor Iniciado* “.
>A seguinte mensagem deve ser exibida logo em seguida: “Digite uma nova entrada“.
>Nesta quarta janela, insira:
127.0.0.1 12345 1.1.1.1 150.1.0.5 Olá, mundo!

>Pressione “Enter”.
>Mensagens devem ser exibidas nas janelas onde o Roteador.java está sendo executado. O programa roteador 1 deverá encaminhar, o roteador 2 deverá encaminhar também e o roteador 3 deverá realizar o descarte após interpretar a mensagem.

OBS:
No passo 9.a.i, você pode inserir outras entradas para diferentes resultados:
Para que o programa roteador 1 encaminhe e o roteador 2 finalize, insira: 
127.0.0.1 12345 1.1.1.1 30.1.2.240 Olá, mundo!
E para que o programa roteador 1 finalize, insira:
127.0.0.1 12345 1.1.1.1 20.16.0.1 Olá, mundo!
