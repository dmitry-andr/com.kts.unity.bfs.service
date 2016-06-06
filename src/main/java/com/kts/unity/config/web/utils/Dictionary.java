package com.kts.unity.config.web.utils;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {

    private static DictionaryDAO dictionaryDAO;
    private static Map<String, String> map = null;

    public static String getValue(String key) {

        if (map == null) {
            map = new HashMap<String, String>();
        }
        String value = map.get(key);
        if (value != null) {
            return value;
        } else {
            String valueInDB = dictionaryDAO.getValue(key);
            if (valueInDB != null) {
                map.put(key, valueInDB);
            }
            return map.get(key);
        }
    }

    public static void putValue(String key, String value) {
        if (map == null) {
            map = new HashMap<String, String>();
        }

        map.put(key, value);
        String valueInDB = dictionaryDAO.getValue(key);
        if (valueInDB != null) {
            dictionaryDAO.updateValue(key, value);
        } else {
            dictionaryDAO.createRecord(key, value);
        }
    }
    
    public static void removeValue(String key){
        map.remove(key);
        dictionaryDAO.removeRecord(key);
    }

    public DictionaryDAO getDictionaryDAO() {
        return dictionaryDAO;
    }

    public void setDictionaryDAO(DictionaryDAO dictDAO) {
        dictionaryDAO = dictDAO;
    }
}
