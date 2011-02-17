package com.freakz.ircproxy;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 2/5/11
 * Time: 10:12 AM
 */
public class Tester {

    public static void main(String[] args) {

        ForwardManager manager = ForwardManager.getInstance();
        manager.addForwardHost("localhost", 6667, 3);
        manager.addForwardHost("irc.elisa.fi", 6667, 3);
        manager.addForwardHost("localhost", 6668, 3);

        while (true) {
            ForwardHost host = manager.getForwardHost();
            if (host == null) {
                break;
            }
            System.out.println("Got host: " + host);
        }

    }


}
