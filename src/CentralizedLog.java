import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//Restrictions are set onto logger methods by using few of the methods only
//@author A0108385B
public class CentralizedLog {

    private static final String LOGGER_NAME = "log";
    private static final String LOG_FILE_NAME = "log.txt";
    private Logger logfile;

    public CentralizedLog() {
        logfile = buildLogger();
    }


    /**
     * This method builds a log file under file name log.txt
     * @return logfile under file handler called log.txt;
     */
    private static Logger buildLogger() {
        Logger logfile = Logger.getLogger(LOGGER_NAME);

        try {

            FileHandler fh = new FileHandler(LOG_FILE_NAME, true);
            logfile.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);
            logfile.setLevel(Level.CONFIG);
        } catch (Exception e) {

        }
        return logfile;
    }

    /**
     * This method changes the level of severity to be logged for debugging purposes.
     * @param newLvl	Level to be set to
     */
    public void setLevel(Level newLvl) {
        logfile.setLevel(newLvl);
    }

    /**
     * This method logs the message under the severity level Severe
     * @param msg Message to be logged
     */
    public void severe(String msg) {
        logfile.severe(msg);
    }

    /**
     * This method logs the message under the severity level Info
     * @param msg Message to be logged
     */
    public void info(String msg) {
        logfile.info(msg);
    }

    /**
     * This method logs the message under the severity level Warning
     * @param msg Message to be logged
     */ 
    public void warning(String msg) {
        logfile.warning(msg);
    }

    /**
     * This method logs the message under the severity level Config
     * @param msg Message to be logged
     */
    public void config(String msg) {
        logfile.config(msg);
    }

    /**
     * This method logs the message under the severity level Fine
     * @param msg Message to be logged
     */
    public void fine(String msg) {
        logfile.fine(msg);
    }

    /**
     * This method logs the message under the severity level Finer
     * @param msg Message to be logged
     */
    public void finer(String msg) {
        logfile.finer(msg);
    }

    /**
     * This method logs the message under the severity level Finest
     * @param msg Message to be logged
     */
    public void finest(String msg) {
        logfile.finest(msg);
    }
}
