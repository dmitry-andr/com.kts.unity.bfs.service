package com.kts.unity.config.web.backend.utils;

import com.kts.unity.config.web.entities.configs.CountryEntry;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.ConfigParams;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class CountriesList {

    private ArrayList<CountryEntry> countries;
    private String listInXML;

    public CountriesList() {

        countries = new ArrayList<CountryEntry>();

        Resource resource = AppContext.getContext().getResource(ConfigParams.COUNTRIES_LIST_PATH_NAME);
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            String countryCodesList = props.getProperty("country.code");
            String countryNamesList = props.getProperty("country.name");

            String[] countryCodes = countryCodesList.split(";");
            String[] countryNames = countryNamesList.split(";");

            StringBuilder xml = new StringBuilder();
            xml.append("<countries>");
            for (int k = 0; k < countryCodes.length; k++) {
                CountryEntry countryEntry = new CountryEntry(countryCodes[k], countryNames[k]);
                countries.add(countryEntry);

                //fill XML structure
                xml.append("<country" + k + ">");
                    xml.append("<countryCode>");
                    xml.append(countryCodes[k]);
                    xml.append("</countryCode>");
                    xml.append("<countryName>");
                    xml.append(countryNames[k]);
                    xml.append("</countryName>");
                xml.append("</country" + k + ">");
            }
            xml.append("</countries>");
            this.listInXML = xml.toString();
            
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }


    }

    public ArrayList<CountryEntry> getCountries() {
        return countries;
    }

    public String getListInXML() {
        return listInXML;
    }
}
