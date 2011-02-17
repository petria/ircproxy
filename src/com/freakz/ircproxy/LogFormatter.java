package com.freakz.ircproxy;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Created by IntelliJ IDEA.
 * User: petria
 * Date: 2/5/11
 * Time: 9:29 AM
 */
public class LogFormatter extends SimpleFormatter {

	public static class LogFilter implements Filter {
		
		protected boolean _logOn = true;
	
		public boolean isLoggable(LogRecord record) {
//			System.out.println("--> " + _logOn);
			return _logOn;
		}
		
		public void setLog(boolean onOff) {
			_logOn = onOff;
		}
		
	}


    private final static Date _date = new Date();
    private final static SimpleDateFormat _formatter = new SimpleDateFormat("HH:mm:ss");

	private static LogFilter _logFilter;
	
	public static void setLog(boolean onOff) {
		_logFilter.setLog(onOff);
	}
	
    public static void initLogger(Logger log) {

        log.setUseParentHandlers(false);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.ALL);
		
		_logFilter = new LogFilter();
		ch.setFilter(_logFilter);
		
        LogFormatter formatter = new LogFormatter();
        ch.setFormatter(formatter);
        log.addHandler(ch);

    }

    public synchronized String format(LogRecord logRecord) {

      _date.setTime(logRecord.getMillis());

      StringBuffer sb = new StringBuffer();
      sb.append(_formatter.format(_date));
      if (!logRecord.getLevel().getLocalizedName().equals("INFO")) {
        sb.append(" <<");
        sb.append(logRecord.getLevel().getLocalizedName());
        sb.append(">>");
      }
      sb.append(" ");
      sb.append(logRecord.getSourceClassName());
      sb.append(".");
      sb.append(logRecord.getSourceMethodName());
      sb.append("(): '");
      sb.append(logRecord.getMessage());

      if (logRecord.getThrown() != null) {
        sb.append("' STACK DUMP\n");
        CharArrayWriter caw = new CharArrayWriter();
        PrintWriter pw = new PrintWriter(caw);

        logRecord.getThrown().printStackTrace(pw);

        sb.append(caw.toString());
        pw.close();

        sb.append("\n");

      } else {
        sb.append("'\n");
      }

      return sb.toString();
    }

}
