package timeserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author murilo.erhardt
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        String group = "228.5.6.7";
        Integer port = 5865;
        Integer portCoord = 7385;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.setProperty("java.net.preferIPv4Stack", "true");

        ServerSocket serverSocket = new ServerSocket(portCoord);

        while (true) {
            try {

                System.out.println("\n\n\n\n\nEnter para sincronizar rede");
                String next = reader.readLine();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                long myTime = timestamp.getTime();
                Thread thread = new Thread(new Listener(serverSocket, myTime));
                thread.start();

                String messageToClient = portCoord.toString() + "->" + myTime;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
                System.out.println("Hora do time server pré-sincronizar: " + sdf.format(myTime));

                byte[] b = messageToClient.getBytes();
                InetAddress addr = InetAddress.getByName("228.5.6.7");
                DatagramSocket ds = new DatagramSocket();
                DatagramPacket pkg = new DatagramPacket(b, b.length, addr, port);
                ds.send(pkg);

            } catch (Exception e) {
                System.out.println("Nao foi possivel enviar a mensagem");
            }

        }

    }
}
