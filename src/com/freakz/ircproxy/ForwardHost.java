package com.freakz.ircproxy;

/**
 * User: petria
 * Date: 2/5/11
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ForwardHost {

  private int _inUse;
  private int _available;
  
  private String _host;
  private int _port;
  
  public ForwardHost(String host, int port, int available) {
    _host = host;
    _port = port;
    _available = available;
  }
  
  
  public int available() {
    return _available;
  }
  
  public synchronized boolean reserve() {
    if (_inUse < _available) {
      _inUse++;
      return true;
    }
    return false;
  }
  
  
  public synchronized void free() {
    _inUse--;
  }

  public String getHost() {
    return _host;
  }
  
  public int getPort() {
    return _port;
  }

  public String toString() {
    String str = String.format("[%s:%4d] Available: %3d - InUse: %3d", _host, _port, _available, _inUse);
    return str;
  }
  
  
}
