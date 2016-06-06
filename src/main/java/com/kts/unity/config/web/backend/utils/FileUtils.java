package com.kts.unity.config.web.backend.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    public static boolean saveConfigInFile(File configFile, String pathToFileInSystem) {
        boolean status = false;
        try {
            InputStream is = new FileInputStream(configFile);
            File fileInSystem = new File(pathToFileInSystem);
            is = new FileInputStream(configFile);
            OutputStream out = new FileOutputStream(fileInSystem);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            is.close();
            status = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static boolean saveXMLStringToFile(String xmlData, String pathToFileInSystem) {

        boolean status = false;

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(pathToFileInSystem));
            writer.write(xmlData);
            status = true;
        } catch (IOException e) {            
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {                
                e.printStackTrace();
            }
        }

        return status;        
    }
}
