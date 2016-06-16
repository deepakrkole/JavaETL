package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by deepakkole on 6/15/16.
 */

public class MainThread{


    Properties props = new Properties();
    String properties = "config.properties";
    InputStream inputStream = null;

    ExecutorService executor = null;
    static ConcurrentLinkedQueue<String> listOfFiles = new ConcurrentLinkedQueue<String>();

    public MainThread() throws IOException{
        inputStream  = getClass().getClassLoader().getResourceAsStream(properties);
        if(inputStream!=null){
            props.load(inputStream);
        }else{
            throw new FileNotFoundException("property file '" + properties + "' not found in the classpath");
        }
    }

    public void populateQueue(){

        String[] filesImps = null;
        File dirImps = new File(props.getProperty("pathImps"));
        for (String file:dirImps.list()) {
            if(!file.contains("processed")) {
                listOfFiles.add(file);
            }

        }
    }

    public void run() throws Exception {


        executor = Executors.newFixedThreadPool(2);
        Runnable worker = null;


        while(true) {
            populateQueue();
            System.out.println("Again Again");
            while( !listOfFiles.isEmpty() ) {

                if (listOfFiles.peek() != null){
                    String file = listOfFiles.poll();

                    if( !Files.exists( Paths.get(props.getProperty("pathClicks") + file) )){
                        System.out.println("Skipping Clicks file");
                        continue;
                    }

                    worker = new WorkingThread(file, props.getProperty("pathImps"),
                            props.getProperty("outPath") ,  props.getProperty("pathClicks"),
                            props.getProperty("connPath"), props.getProperty("devicePath"));
                    executor.execute(worker);
                }
            }

            Thread.sleep(1*60*1000);
        }
    }
}
