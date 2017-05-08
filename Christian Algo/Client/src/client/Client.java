package client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Client {

    public static void main(String[] args) throws IOException {
        int timeStampSend;
        int timeStampRecieve;
        Long myTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        myTime = timestamp.getTime();
        Random gerador = new Random();
        int rand = gerador.nextInt(50000)+10000;
        System.out.println("Hora atual: "+sdf.format(timestamp) + "(Time stamp: "+myTime+") "+ " - "+" TimeStamp: " + rand);
        myTime = myTime - rand;
        System.out.println("Novo TimeStamp  : "+myTime);

        
        try{
            Socket client = new Socket("192.168.0.8", 5572);
            
        System.out.println("O cliente se conectou ao servidor!");
        PrintStream out = null;
        out = new PrintStream(client.getOutputStream());

        out.println(
                "Hey! Manda a hora ai! E o tempo que demorou pra processar");
        }catch(Exception ex){
            System.out.println(ex);
        }


    }

}
