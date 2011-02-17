package com.freakz.ircproxy;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 2/1/11
 * Time: 3:36 PM
 */
public class Forwarder {

    private static java.util.logging.Logger LOG
        = java.util.logging.Logger.getLogger("com.freakz.ircproxy");

    private Runner _toFrom;
    private Runner _fromTo;
    private int _runnersCount;
    private IrcProxy _proxy;
	private ForwardHost _forwardHost;

//------------------------	
    public static class Runner implements Runnable {

        private static final int BUF_SIZE = 4096;

        private Forwarder _forwarder;
        private Socket _fromSocket;
        private Socket _toSocket;
        private boolean _doQuit = false;

        public Runner(Forwarder forwarder, Socket fromSocket, Socket toSocket) {
            _forwarder = forwarder;
            _fromSocket = fromSocket;
            _toSocket = toSocket;
        }

        public void start() {
            Thread t = new Thread(this);
            t.start();
        }

        public void run() {

            LOG.info("Starting forward FROM: " + _fromSocket + " --> TO: " + _toSocket);

            byte[] buf = new byte[BUF_SIZE];
            try {
                _fromSocket.setSoTimeout(30 * 1000);
                BufferedInputStream  fromStreamReader = new BufferedInputStream(_fromSocket.getInputStream());
                BufferedOutputStream toStreamWriter = new BufferedOutputStream(_toSocket.getOutputStream());

                while (!_doQuit) {
                    try {
                        int read = fromStreamReader.read(buf, 0, BUF_SIZE);
//                        LOG.info("READ: " + read);
                        if (read != -1) {
                            toStreamWriter.write(buf, 0, read);
                            toStreamWriter.flush();
                        } else {
                            break;
                        }

                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
                LOG.info("EXITING loop!");
                _forwarder.runnerExited(this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setQuit(boolean doQuit) {
            _doQuit = doQuit;
            try {
                _fromSocket.close();
                _toSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//------------------------	

    public Forwarder(IrcProxy proxy, ForwardHost forwardHost) {
        _proxy = proxy;
		_forwardHost = forwardHost;
    }

    private void runnerExited(Runner runner) {
        _runnersCount--;

        LOG.info("Exited: " + runner + ", runners left: " + _runnersCount);

        _toFrom.setQuit(true);
        _fromTo.setQuit(true);

        if (_runnersCount == 0 && _proxy != null) {
            _proxy.forwarderExited(this);
			_forwardHost.free();
        }
    }


    public void startForward(Socket fromSocket) throws IOException {
        Socket toSocket = new Socket(_forwardHost.getHost(), _forwardHost.getPort());
        _toFrom = new Runner(this, toSocket, fromSocket);
        _fromTo = new Runner(this, fromSocket, toSocket);

        _toFrom.start();
        _fromTo.start();
        _runnersCount = 2;

    }
}
