package timeserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 *
 * @author muriloerhardt
 */
public class Listener implements Runnable {

    private static ServerSocket port;
    private static Long serverTime;
    private static Socket socketCliente = null;

    public Listener(ServerSocket port, Long serverTime) {
        this.port = port;
        this.serverTime = serverTime;
    }

    @Override
    public void run() {
        Singleton st = Singleton.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Integer timeout = 5000;
        ServerSocket portListener = port;
        try {
            portListener.setSoTimeout(5000);
        } catch (SocketException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            DataInputStream in = null;
            String cliAddr[];
            String recebidoCliente = "esperando pedido para ficar ";

            try {
                socketCliente = port.accept();
                System.out.println("port Client: " + socketCliente.getPort());;
                in = new DataInputStream(socketCliente.getInputStream());

                recebidoCliente = in.readUTF();
                cliAddr = recebidoCliente.split("->");

            } catch (IOException ex) {
                System.out.println("\n\n\n\n\n\nTime Out " + timeout / 1000 + " segundos");
                break;
            }

            Long clientTime = Long.parseLong(cliAddr[0].trim());
            st.addClientTime(clientTime);
            st.addClienteAddr(socketCliente);

            System.out.println(" ---- Cliente enviou a diferença em timestamp:" + clientTime);

        }

        System.out.println("Excedeu tempo de espera :/ ");

        Long newTime = 0l;
        for (Long time : st.getListOfClientTime()) {
            newTime = newTime + time;
        }
        newTime = newTime / (st.getListOfClientTime().size() + 1);
        serverTime = serverTime + newTime;

        System.out.println("Novo horário!!!!!!!  " + sdf.format(serverTime));

        for (int i = 0; i < st.getClientAddr().size(); i++) {
            Long clientDiffTime = st.getListOfClientTime().get(i);
            clientDiffTime = (clientDiffTime * -1) + newTime;

            try {
                Socket clientSocket = null;
                DataOutputStream outToServer = new DataOutputStream(st.getClientAddr().get(i).getOutputStream());
                sendWithDelay();
                outToServer.writeUTF(clientDiffTime.toString());
            } catch (IOException ex) {
                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void sendWithDelay() throws InterruptedException {
        Random gerador = new Random();
        int rand = gerador.nextInt(2000);
        System.out.println("Delay de " + rand / 1000 + " segundos");
        Thread.sleep(rand);
    }
}
