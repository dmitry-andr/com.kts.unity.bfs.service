package com.kts.unity.config.web.entities.configs;

public class CountryEntry {
    private String countryCode;
    private String countryName;

    public CountryEntry(String countryCode, String countryName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    
    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("Country code : ");
        out.append(this.countryCode);
        out.append(" ; Country Name : ");
        out.append(this.countryName);
        return out.toString();
    }
    
}
