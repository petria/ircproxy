package com.freakz.ircproxy;

import java.util.Scanner;

/**
 * Created by 
 * User: petria
 * Date: 2/15/11
 * Time: 2:43 PM
 */
 public class Konsole implements Runnable {

    private static java.util.logging.Logger LOG
        = java.util.logging.Logger.getLogger("com.freakz.ircproxy");
 
	private IrcProxy _proxy;
	private boolean _running;
	
	public Konsole(IrcProxy proxy) {
		_proxy = proxy;
	}
 
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
 
	public void stop() {
		_running = false;
	}
 
	public void printStatus() {
		ForwardHost[] fHosts = ForwardManager.getInstance().getForwardHosts();
		System.out.println("-- STATUS --");	
		for (ForwardHost fHost : fHosts) {
			System.out.println(fHost.toString());
		}
	}
 
	public void run() {

		System.out.println(">> Konsole ready!");

		_running = true;
		Scanner stdin = new Scanner(System.in);
		
		while (_running) {
			String line = stdin.next().trim().toLowerCase();
			LOG.info(">> " + line);
			if (line.equals("quit")) {
				_running = false;
				break;
			}
			if (line.equals("logoff")) {
				LogFormatter.setLog(false);
			}
			if (line.equals("logon")) {
				LogFormatter.setLog(true);
			}
			if (line.equals("status")) {
				printStatus();
			}
		}
		LOG.info("Exiting mainloop!!");
		_proxy.doQuit();
	
	}
 
 
 }