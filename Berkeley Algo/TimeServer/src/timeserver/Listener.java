package timeserver;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author muriloerhardt
 */
public class Listener implements Runnable {

    private static ServerSocket port;

    public Listener(ServerSocket port) {
        this.port = port;
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
            Socket socketCliente = null;
            DataInputStream in = null;
            String recebidoCliente = "esperando pedido para ficar ";

            try {
                socketCliente = port.accept();
                in = new DataInputStream(socketCliente.getInputStream());
                recebidoCliente = in.readUTF();

            } catch (IOException ex) {
                System.out.println("\n\n\n\n\n\nTime Out " + timeout / 1000 + " segundos");
                break;
            }

            Long clientTime = Long.parseLong(recebidoCliente.trim());
            st.addClientTime(clientTime);

            System.out.println("-----> Um cliente enviou sua hora: " + sdf.format(recebidoCliente.trim()) + " ---- com timestamp:" + recebidoCliente.trim());

        }

        System.out.println("Excedeu tempo de espera :/ ");
    }

}
