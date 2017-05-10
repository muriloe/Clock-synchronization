package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author murilo.erhardt
 */
public class Client {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        String group = "228.5.6.7";
        System.setProperty("java.net.preferIPv4Stack", "true");
        Timestamp timestamp;
        timestamp = new Timestamp(System.currentTimeMillis());
        long myTime = timestamp.getTime() - 1000000;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

        MulticastSocket mcs = new MulticastSocket(5865);
        InetAddress grp = InetAddress.getByName("228.5.6.7");
        mcs.joinGroup(grp);

        byte rec[] = new byte[256];
        DatagramPacket pkg = new DatagramPacket(rec, rec.length);
        mcs.receive(pkg);

        System.out.println("-----> A hora do cliente pre sincronizar é: " + sdf.format(myTime) + " ---- com timestamp:" + myTime);

        String data = new String(pkg.getData()).trim();
        System.out.println("Dados recebidos:" + data);
        String[] parts = data.split("->");
        Long timeServe = Long.parseLong(parts[1]);
        Long changeTime = myTime - timeServe;

        System.out.println("-----> Difença de tempo entre time server e cliente é: " + sdf.format(changeTime) + " ---- com timestamp:" + changeTime);
        int portServ = Integer.parseInt(parts[0]);
        Socket clientSocket = new Socket("localhost", portServ);
        String msgIDUsuario = changeTime.toString();
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeUTF(msgIDUsuario);

    }

}
