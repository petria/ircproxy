package com.freakz.ircproxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 2/5/11
 * Time: 9:46 AM
 */
public class ForwardManager {

    private static java.util.logging.Logger LOG
            = java.util.logging.Logger.getLogger("com.freakz.ircproxy");

    List<ForwardHost> _forwardHosts = new ArrayList<ForwardHost>();
    private static ForwardManager _instance = new ForwardManager();

    private ForwardManager() {

    }

    public static ForwardManager getInstance() {
        return _instance;
    }

    public void addForwardHost(String host, int port, int available) {
        ForwardHost forwardHost = new ForwardHost(host, port, available);
        _forwardHosts.add(forwardHost);
    }

    public ForwardHost getForwardHost() {
        for (ForwardHost forwardHost : _forwardHosts) {
            if (forwardHost.reserve()) {
                return forwardHost;
            }
        }
        return null;
    }


    private int ident = 0;

    public String handleIdent(String line) {
        LOG.info("HANDLE: " + line);
        return String.format("%s%03d", "username", ident++);
    }
	
	public ForwardHost[] getForwardHosts() {
		return _forwardHosts.toArray(new ForwardHost[_forwardHosts.size()]);
	}
	
}
