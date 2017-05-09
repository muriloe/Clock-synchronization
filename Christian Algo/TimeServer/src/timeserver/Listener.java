/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Listener implements Runnable {

    private static Socket client;

    public Listener(Socket client) {

        this.client = client;

    }

    private static void metodo() throws IOException, InterruptedException {
        Socket nClient = client;

        
            System.out.println("endereco do cliente:" + nClient.getInetAddress());
            InputStream cliente = nClient.getInputStream();
            System.out.println("localaddr:" + nClient.getLocalAddress() + " inet" + nClient.getInetAddress());
            Scanner s;
            s = new Scanner(cliente);

            try {
                s = new Scanner(nClient.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (s.hasNextLine()) {
                try {
                    decodeMessage(s.nextLine(), nClient);
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        

    }

    @Override
    public void run() {

        try {
            metodo();

        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void decodeMessage(String message, Socket nCliente) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long serverTime=getRealTime();
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("O servidor demorou: " + estimatedTime/1000 + " segundos, para obter o hora");
        String result = ""+ serverTime + "->" + estimatedTime;
       
        Random rand2 = new Random();
        int timeRand = rand2.nextInt(10000);
        Thread.sleep(timeRand);
        
        PrintStream ps = new PrintStream(nCliente.getOutputStream());
        ps.println(result);
        
    }

    public static Long getRealTime() throws InterruptedException {
        Long serverTime;
        int processTime;
        Random rand2 = new Random();
        processTime = rand2.nextInt(25000) + 5000;
        System.out.println("O tempo de processamento é de: " + processTime/1000 + " segundos");
        Thread.sleep(processTime);
       
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        serverTime = timestamp.getTime();
        return serverTime;

    }

}
