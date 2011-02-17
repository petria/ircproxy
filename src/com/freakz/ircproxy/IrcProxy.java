package com.freakz.ircproxy;

import java.io.*;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 2/1/11
 * Time: 3:30 PM
 */
public class IrcProxy {

  private final static int SERVER_PORT = 2222;
  
  private static java.util.logging.Logger LOG
    = java.util.logging.Logger.getLogger("com.freakz.ircproxy");
  
  private boolean _doQuit = false;
  private IdentServer _ident;
  
  public static void main(String[] args) throws Exception {
    ForwardManager manager = ForwardManager.getInstance();
    manager.addForwardHost("localhost", 6667, 3);
    manager.addForwardHost("irc.elisa.fi", 6667, 3);
    manager.addForwardHost("irc.stealth.net", 6667, 10);
    
    LogFormatter.initLogger(LOG);
    IrcProxy proxy = new IrcProxy();
    proxy.serve();
	
	LOG.info("... GOING DOWN!");
	System.exit(0);
	
  }
  
  public void serve() throws Exception {
    
    //_ident = new IdentServer(1113);
    
	Konsole konsole = new Konsole(this);
	konsole.start();
	
    ServerSocket sSocket = new ServerSocket(SERVER_PORT);
    sSocket.setSoTimeout(3 * 1000);

    LOG.info("Waiting connections to port: " + SERVER_PORT);

    while (!_doQuit) {
      
      try {

		Socket socket = sSocket.accept();
		LOG.info("Got connection: " + socket);
		ForwardHost host = ForwardManager.getInstance().getForwardHost();
		LOG.info("ForwardHost = " + host);
		if (host != null) {
			Forwarder f = new Forwarder(this, host);
			f.startForward(socket);	
		} else {
			LOG.info("NO MORE FREE IRCD CONNECTIONS!!");
			socket.close();
		}
	
      } catch (SocketTimeoutException e) {
		//
      }

	}
	LOG.info("Exited mainloop!!");
    
  }
  
  public void forwarderExited(Forwarder forwarder) {
    LOG.info("Forwarder is DONE: " + forwarder);
  }

  public void doQuit() {
	_doQuit = true;
  }
  
}
