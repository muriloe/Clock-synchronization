package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 *
 * @author murilo.erhardt
 */
public class Client {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        String group = "228.5.6.7";
        System.setProperty("java.net.preferIPv4Stack", "true");
        Timestamp timestamp;
        timestamp = new Timestamp(System.currentTimeMillis());
        long myTime = timestamp.getTime();
        Random gerador = new Random();
        int randDelay = gerador.nextInt(1000000);
        

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        MulticastSocket mcs = new MulticastSocket(5865);
        InetAddress grp = InetAddress.getByName("228.5.6.7");
        mcs.joinGroup(grp);

        byte rec[] = new byte[256];
        DatagramPacket pkg = new DatagramPacket(rec, rec.length);
        mcs.receive(pkg);

        timestamp = new Timestamp(System.currentTimeMillis());
        myTime = timestamp.getTime() + randDelay;
        System.out.println("-----> A hora do cliente pre sincronizar é: " + sdf.format(myTime) + " ---- com timestamp:" + myTime);

        String data = new String(pkg.getData()).trim();
        System.out.println("Dados recebidos:" + data);
        String[] parts = data.split("->");
        Long timeServe = Long.parseLong(parts[1]);
        
        timestamp = new Timestamp(System.currentTimeMillis());
        myTime = timestamp.getTime() + randDelay;
        Long changeTime = myTime - timeServe;

        System.out.println("-----> Difença de timestamp:" + changeTime);
        int portServ = Integer.parseInt(parts[0]);
        Socket clientSocket = new Socket("localhost", portServ);
        String msgIDUsuario = changeTime.toString();
        msgIDUsuario = msgIDUsuario + "->" + clientSocket.getLocalPort();
        System.out.println("My Port -> "+clientSocket.getLocalPort());

        sendWithDelay();
        
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeUTF(msgIDUsuario);
        
        DataInputStream inputStram = new DataInputStream(clientSocket.getInputStream());
        String recieve;
        recieve = inputStram.readUTF();
        

        
        System.out.println("Time para ajuste = " + recieve);
        
        timestamp = new Timestamp(System.currentTimeMillis());
        myTime = timestamp.getTime() + randDelay;
        System.out.println("Novo horário: " + sdf.format(myTime + Long.parseLong(recieve)));
    }
    
    public static void sendWithDelay() throws InterruptedException{
        Random gerador = new Random();
        int rand = gerador.nextInt(4000);
        System.out.println("Delay de " + rand/1000 + " segundos");
        Thread.sleep(rand);
    }

}
