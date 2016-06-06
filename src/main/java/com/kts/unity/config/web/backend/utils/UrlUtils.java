package com.kts.unity.config.web.backend.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {

    public String readUrlContent(String url) {

        String output = "";

        try {
            URL urlToRead = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlToRead.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                output += inputLine;
            }
            in.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return output;
    }
}
