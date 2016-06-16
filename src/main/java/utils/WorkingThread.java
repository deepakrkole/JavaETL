package utils;


import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

/**
 * Created by deepakkole on 6/15/16.
 */
public class WorkingThread implements Runnable{

    PlainPojo p = null;
    static Map<Integer, String> mapDeviceType = null;
    static Map<Integer, String> mapConnType = null;
    FileReader readerImpl = null;
    FileReader readerclick = null;
    FileReader readerConn = null;
    FileReader readerDev = null;
    String outPath;
    String outFileName;
    String impPath;
    String clickPath;
    String connPath;
    String devicePath;

    final static Logger logger = Logger.getLogger(WorkingThread.class);

    public WorkingThread(String file,String impPath,String outPath,
                         String clickPath, String connPath, String devicePath) throws Exception {

        this.outPath = outPath;
        this.impPath = impPath;
        this.clickPath = clickPath;
        this.connPath = connPath;
        this.devicePath = devicePath;
        String newPath = impPath + file.substring(0, file.length() - 4) + "-processed.csv";
        renameFile(impPath + file,newPath);
        this.outFileName = file.substring(0, file.length()-4)+".json";
        readerImpl = new FileReader(newPath);
        readerclick = new FileReader(clickPath + file);
        readerConn = new FileReader(connPath);
        readerDev = new FileReader(devicePath);
        this.mapDeviceType = new HashMap<Integer, String>();
        this.mapConnType = new HashMap<Integer, String>();

    }

    public void run() {

        logger.info(outFileName+" etl start.");

        try {

            loadDimensions(mapConnType, readerConn);
            loadDimensions(mapDeviceType, readerDev);

            BufferedReader bufReader = new BufferedReader(readerImpl);
            BufferedReader bufReaderClicks = new BufferedReader(readerclick);
            String line = null;
            Date time = null;
            String lineClicks = null;
            String[] values = null;
            String[] valuesClick = null;

            while ((line = bufReader.readLine()) != null) {

                if (line != null) {
                    values = line.split(",");

                    long unixSeconds = Long.parseLong(values[0]);
                    Date date = new Date(unixSeconds*1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    String formattedDate = sdf.format(date);
                    System.out.println(formattedDate);

//                    Instant instant = Instant.ofEpochMilli(Long.parseLong(values[0]));
//                    System.out.println(instant.toString());
                    values[0]=formattedDate;
                }

                while((lineClicks = bufReaderClicks.readLine()) !=null){
                    if (lineClicks != null) {
                        valuesClick = lineClicks.split(",");
                        if ((values[1].equals(valuesClick[1]))) {
                            p = new PlainPojo();
                            p.setTransaction_id(values[1]);
                            p.setTimestamp(values[0]);
                            p.setClicks(Integer.parseInt(valuesClick[2]));
                            p.setimps(Integer.parseInt(values[4]));
                            p.setConnection_type(mapConnType.get(Integer.parseInt(values[2])));
                            p.setDevice_type(mapDeviceType.get(Integer.parseInt(values[3])));
                            convertToJson(p);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        finally {
            try {
                readerImpl.close();
                readerclick.close();
                readerDev.close();
                readerConn.close();
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void convertToJson(PlainPojo p) throws Exception{

        if(p != null){
            FileWriter fileWriter=null;

            try {
                if(!Files.exists(Paths.get(outPath + outFileName)))
                    Files.createFile(Paths.get(outPath + outFileName));

                fileWriter = new FileWriter(outPath + outFileName,true);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writer(new MinimalPrettyPrinter("\n")).writeValue(fileWriter,p );

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                    if (fileWriter != null)
                        fileWriter.close();
            }
        }
        logger.info(outFileName+" ETL end.");
    }

    public synchronized void loadDimensions(Map<Integer, String> mapType,FileReader reader)
            throws IOException {

        BufferedReader bufReaderConn = new BufferedReader(reader);
        String line = null;


        while ((line = bufReaderConn.readLine()) != null) {
            if (line != null) {
                JSONArray jsonArray = new JSONArray(line);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mapType.put(i + 1, jsonObject.get(String.valueOf(i + 1)).toString());
                }
                break;
            }
        }
    }

    public synchronized void renameFile(String filePath,String newPath)throws Exception{
        java.nio.file.Files.move(Paths.get(filePath),Paths.get(newPath));
    }

}
