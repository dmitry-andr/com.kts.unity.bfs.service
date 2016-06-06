package com.kts.unity.config.web.utils;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class DictionaryDAO extends SimpleJdbcDaoSupport {

    public int createRecord(String key, String value) {
        int status = 0;
        String sqlInsert = "INSERT INTO " + DictionaryTable.TBL_NAME + "("
                + DictionaryTable.KEY + ", "
                + DictionaryTable.VALUE
                + ") VALUES(?,?)";

        getSimpleJdbcTemplate().update(sqlInsert, key, value);

        status = 1;

        return status;
    }

    public String getValue(String key) {
        String sql = "SELECT " + DictionaryTable.TBL_NAME + "." + DictionaryTable.VALUE + " FROM " + DictionaryTable.TBL_NAME + " WHERE " + DictionaryTable.KEY + " = ?";
        String value = null;
        try {
            value = (String) this.getJdbcTemplate().queryForObject(sql, new Object[]{key}, String.class);
        } catch (IncorrectResultSizeDataAccessException ex) {
        }

        return value;
    }
    
    public int updateValue(String key, String value){
        int status = 0;
        String updateQryStr = "UPDATE " + DictionaryTable.TBL_NAME + " SET "
                + DictionaryTable.VALUE + " = ?"                
                + " WHERE " + DictionaryTable.KEY + " = ?";

        getSimpleJdbcTemplate().update(updateQryStr, value, key);
        
        return status;        
    }

    public int removeRecord(String key) {
        int status = 0;

        String sqlDelete = "DELETE FROM " + DictionaryTable.TBL_NAME + " WHERE " + DictionaryTable.KEY + " = ?";
        getSimpleJdbcTemplate().update(sqlDelete, key);
        
        status = 1;

        return status;
    }
}
