package com.freakz.ircproxy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 2/5/11
 * Time: 10:53 AM
 */
public class IdentServer implements Runnable {

    private static java.util.logging.Logger LOG
            = java.util.logging.Logger.getLogger("com.freakz.ircproxy");

    private Thread _thread;
    private int _port;

    public IdentServer(int port) {
        _port = port;
        _thread = new Thread(this);
        _thread.start();
    }

    public void run() {

        try {
            ServerSocket identSocket = new ServerSocket(_port);
            while (_thread == Thread.currentThread()) {
                LOG.info("*** Waiting IDENT request on port: " + _port);
                Socket socket = identSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String line = reader.readLine();
                if (line != null) {
                  LOG.info("*** Ident request received: " + line);
                  line = line + " : USERID : UNIX : " + ForwardManager.getInstance().handleIdent(line);
                  writer.write(line + "\r\n");
                  writer.flush();
                  LOG.info("*** Ident reply sent: " + line);
                  writer.close();
                }


            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
