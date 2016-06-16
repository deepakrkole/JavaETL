package utils;

import org.apache.log4j.Logger;

/**
 * Created by deepakkole on 6/14/16.
 */

public class MainController {

    final static Logger logger = Logger.getLogger(MainController.class);

    public static void main(String[] args) throws Exception {

        logger.info("Main Process: ");
        MainThread t = new MainThread();
        t.run();
    }
}
