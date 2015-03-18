import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CentralizedLog {
	private static Logger logfile;
	
	/**
	 * This method gets the logger used for this system
	 * @return	log file that is used among the system
	 */
	public static Logger getLogger() {
		if(logfile == null) {
			logfile = buildLogger();
		}
		return logfile;
	}
	
	/**
	 * This method build a log file under file name log.txt
	 * @return logfile under file handler called log.txt;
	 */
	private static Logger buildLogger() {
		Logger logfile = Logger.getLogger("log");
		try {
			
			FileHandler fh = new FileHandler("log.txt");
			logfile.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
			logfile.setLevel(Level.CONFIG);
		} catch(Exception e) {
			
		}
		return logfile;
	}
}
