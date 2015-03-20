import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CentralizedLog {
	private Logger logfile;
	private static CentralizedLog centralLog;
	
	private CentralizedLog() {
		logfile = buildLogger();
	}
	
	/**
	 * This method initialize centralLog object and 
	 * gets the logger used for this system
	 * @return	log file that is used among the system
	 */
	public static CentralizedLog getLogger() {
		if(centralLog == null) {
			centralLog = new CentralizedLog();
		}
		
		return centralLog;
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
	
	public void setLevel(Level newLvl) {
		logfile.setLevel(newLvl);
	}
	
	public void log(Level level, String msg) {
		logfile.log(level, msg);
	}
	
	public void severe(String msg) {
		logfile.severe(msg);
	}
	public void info(String msg) {
		logfile.info(msg);
	}
	public void warning(String msg) {
		logfile.warning(msg);
	}
	public void config(String msg) {
		logfile.config(msg);
	}
	public void fine(String msg) {
		logfile.fine(msg);
	}
	public void finer(String msg) {
		logfile.finer(msg);
	}
	public void finest(String msg) {
		logfile.finest(msg);
	}
}
