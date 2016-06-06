package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.backend.utils.LoggingRecordsTable;
import com.kts.unity.config.web.entities.LoggingRecord;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class LoggingRecordsDAO extends SimpleJdbcDaoSupport {

    public int createTransactionRecord(LoggingRecord logRecordData) {
        int status = 0;
        String logDataInsertSql = "INSERT INTO " + LoggingRecordsTable.TBL_NAME + "("
                + LoggingRecordsTable.PLAYER_NAME + ","
                + LoggingRecordsTable.REMOTE_APP_VERSION + ","
                + LoggingRecordsTable.TIMESTAMP_DATE + ","
                + LoggingRecordsTable.TIMESTAMP_TIME + ","
                + LoggingRecordsTable.LOG_TEXT
                + ") VALUES(?,?,?,?,?)";

        getSimpleJdbcTemplate().update(
                logDataInsertSql,
                logRecordData.getPlayerName(),
                logRecordData.getRemoteAppVersion(),
                logRecordData.getTransactionDate(),
                logRecordData.getTransactionTime(),
                logRecordData.getLogRegordText());
        status = 1;

        return status;
    }

    public ArrayList<LoggingRecord> getLogRecords(int numOfRecords) {
        String sql = "SELECT " + LoggingRecordsTable.TBL_NAME + ".* FROM " + LoggingRecordsTable.TBL_NAME
                + " ORDER BY " + LoggingRecordsTable.TIMESTAMP_DATE + " DESC, "
                + LoggingRecordsTable.TIMESTAMP_TIME + " DESC"
                + " LIMIT " + numOfRecords;

        List<Map<String, Object>> logsData = this.getJdbcTemplate().queryForList(sql);
        if (logsData != null) {
            ArrayList<LoggingRecord> logsList = new ArrayList<LoggingRecord>();
            for (Map row : logsData) {
                LoggingRecord tmpLogData = new LoggingRecord();
                tmpLogData.setRecordId((Integer) row.get(LoggingRecordsTable.RECORD_ID));
                tmpLogData.setPlayerName((String) row.get(LoggingRecordsTable.PLAYER_NAME));
                tmpLogData.setRemoteAppVersion((String) row.get(LoggingRecordsTable.REMOTE_APP_VERSION));
                tmpLogData.setTransactionDate((Date) row.get(LoggingRecordsTable.TIMESTAMP_DATE));
                tmpLogData.setTransactionTime((String) row.get(LoggingRecordsTable.TIMESTAMP_TIME));
                tmpLogData.setLogRegordText((String) row.get(LoggingRecordsTable.LOG_TEXT));

                logsList.add(tmpLogData);
            }
            return logsList;
        } else {
            return null;
        }
    }

    public int getNumberOfRecords() {
        String sql = "SELECT COUNT(" + LoggingRecordsTable.TBL_NAME + "." + LoggingRecordsTable.RECORD_ID + ") FROM " + LoggingRecordsTable.TBL_NAME;
        int numberOfRecords = -1;
        try {
            numberOfRecords = this.getJdbcTemplate().queryForInt(sql);
        } catch (IncorrectResultSizeDataAccessException ex) {
            numberOfRecords = -1;
        }

        return numberOfRecords;
    }

    /*
    DELETE FROM remote_devices_log WHERE remote_devices_log.record_id NOT IN (
    SELECT record_id  FROM (
    SELECT record_id  FROM remote_devices_log  ORDER BY record_id DESC    LIMIT 500
    ) foo
    ) LIMIT 500;
     */
    public int deleteLogsRecords(int numOfRecordsToKeep) {
        int status = 0;

        String sql = "DELETE FROM " + LoggingRecordsTable.TBL_NAME + " WHERE " + LoggingRecordsTable.TBL_NAME + "." + LoggingRecordsTable.RECORD_ID + " NOT IN ("
                + " SELECT " + LoggingRecordsTable.RECORD_ID + " FROM ( " 
                + "SELECT " + LoggingRecordsTable.RECORD_ID + " FROM " + LoggingRecordsTable.TBL_NAME + " ORDER BY " + LoggingRecordsTable.RECORD_ID + " DESC LIMIT " + numOfRecordsToKeep
                + ") foo"
                + ") LIMIT " + numOfRecordsToKeep;
        
        getSimpleJdbcTemplate().update(sql);
        status = 1;

        return status;
    }
}
