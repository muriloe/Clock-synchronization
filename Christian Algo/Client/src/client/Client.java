package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        Long timeStampSend;
        Long timeStampRecieve;
        Long myTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        myTime = timestamp.getTime();
        Random gerador = new Random();
        int rand = gerador.nextInt(5000000) + 1000000;
        System.out.println("Hora atual: " + sdf.format(timestamp) + "(Time stamp: " + myTime + ") " + " - " + " TimeStamp: " + rand);
        myTime = myTime - rand;
        System.out.println("Rand horário atual do cliente:" + sdf.format(myTime) + " novo TimeStamp  : " + myTime);

        Socket client = new Socket("127.0.0.1", 12345);
        System.out.println("O cliente se conectou ao servidor!");
        PrintStream out = null;
        out = new PrintStream(client.getOutputStream());

        Random rand2 = new Random();
        int timeRand = rand2.nextInt(10000);
        System.out.println("Hey! Manda a hora ai! E o tempo que demorou pra processar. Delay de " + timeRand / 1000 + " segundos para enviar");
        timeStampSend = timestamp.getTime();
        Thread.sleep(timeRand);
        out.println("Hey! Manda a hora ai! E o tempo que demorou pra processar");

        String message = null;
        InputStream server;
        server = client.getInputStream();
        Scanner s = new Scanner(server);

        message = s.nextLine();
        System.out.println(message);

        timeStampRecieve = timestamp.getTime();

        String[] parts = message.split("->");
        Long timeToProcessServer = Long.parseLong(parts[1]);
        Long realTimeServer = Long.parseLong(parts[0]);

        Long correctTime = ((timeStampRecieve - timeStampSend - timeToProcessServer) / 2) + realTimeServer;

        System.out.println("Hora cliente sincronizada: " + sdf.format(correctTime));

    }

}
