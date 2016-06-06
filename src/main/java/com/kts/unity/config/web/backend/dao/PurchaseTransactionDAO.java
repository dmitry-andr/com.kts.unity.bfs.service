package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.backend.utils.purchase.PurchaseTransactionTable;
import com.kts.unity.config.web.backend.utils.player.PlayerTable;
import com.kts.unity.config.web.entities.PurchaseTransactionData;
import com.kts.unity.config.web.entities.player.Player;
import com.kts.unity.config.web.entities.player.PlayerWithPurchaseData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class PurchaseTransactionDAO extends SimpleJdbcDaoSupport {
    
    
    public int createTransactionRecord(PurchaseTransactionData transactData) throws Exception {
        int status = 0;
        String transactDataInsertSql = "INSERT INTO " + PurchaseTransactionTable.TBL_NAME + "("
                + PurchaseTransactionTable.TRANSACTION_DATE + ","
                + PurchaseTransactionTable.TRANSACTION_TIME + ","
                + PurchaseTransactionTable.PLAYER_ID + ","
                + PurchaseTransactionTable.OPERATION_TYPE + ","
                + PurchaseTransactionTable.PURCHASED_ITEM + ","
                + PurchaseTransactionTable.COINS_BALANCE_CHANGE                
                + ") VALUES(?,?,?,?,?,?)";

        getSimpleJdbcTemplate().update(
                transactDataInsertSql,
                transactData.getTransactionDate(),
                transactData.getTransactionTime(),
                transactData.getPlayerId(),
                transactData.getOperationType(),
                transactData.getPurchasedItem(),
                transactData.getCoinsBalanceChange());
        status = 1;

        return status;
    }
    
    public int deleteTransaction(int transactionId) {
        int status = 0;

        String sql = "DELETE FROM " + PurchaseTransactionTable.TBL_NAME + " WHERE " + PurchaseTransactionTable.ID + " = ?";
        getSimpleJdbcTemplate().update(sql, transactionId);
        status = 1;

        return status;
    }
    
    public int getNumberOfTransactions() {        
        String sql = "SELECT COUNT(" + PurchaseTransactionTable.TBL_NAME + "." + PurchaseTransactionTable.ID + ") FROM " + PurchaseTransactionTable.TBL_NAME;
        int numberOfTransacts = -1;
        try {
            numberOfTransacts = this.getJdbcTemplate().queryForInt(sql);
        } catch (IncorrectResultSizeDataAccessException ex) {
            numberOfTransacts = -1;
        }
        
        return numberOfTransacts;
    }
    
    public ArrayList<PlayerWithPurchaseData> getLatestTransactionsList(int maxNumOfRecords){
        
        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PurchaseTransactionTable.TBL_NAME + ".* "
                + " FROM " + PurchaseTransactionTable.TBL_NAME
                + " LEFT JOIN " + PlayerTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + "=" + PurchaseTransactionTable.TBL_NAME + "." + PurchaseTransactionTable.PLAYER_ID
                + " ORDER BY " + PurchaseTransactionTable.TBL_NAME + "." + PurchaseTransactionTable.TRANSACTION_DATE + " DESC, " + PurchaseTransactionTable.TBL_NAME + "." + PurchaseTransactionTable.TRANSACTION_TIME + " DESC"
                + " LIMIT " + maxNumOfRecords;
                
        List<Map<String, Object>> transactData = this.getJdbcTemplate().queryForList(sql);
        
        if(transactData != null){
            ArrayList<PlayerWithPurchaseData> transDataList = new ArrayList<PlayerWithPurchaseData>();
            for (Map row : transactData) {
                PlayerWithPurchaseData playerWithPurchDataTemp = new PlayerWithPurchaseData();
                
                Player tmpPlayer = new Player();
                tmpPlayer.setId((Integer) row.get(PlayerTable.ID));
                tmpPlayer.setName((String) row.get(PlayerTable.NAME));                
                playerWithPurchDataTemp.setPlayer(tmpPlayer);
                
                PurchaseTransactionData tmpTransData = new PurchaseTransactionData();                
                tmpTransData.setId((Integer) row.get(PurchaseTransactionTable.ID));
                tmpTransData.setTransactionDate((Date) row.get(PurchaseTransactionTable.TRANSACTION_DATE));
                tmpTransData.setTransactionTime((String) row.get(PurchaseTransactionTable.TRANSACTION_TIME));
                tmpTransData.setPlayerId((Integer) row.get(PurchaseTransactionTable.PLAYER_ID));
                tmpTransData.setOperationType((Integer) row.get(PurchaseTransactionTable.OPERATION_TYPE));
                tmpTransData.setPurchasedItem((Integer) row.get(PurchaseTransactionTable.PURCHASED_ITEM));
                tmpTransData.setCoinsBalanceChange((Integer) row.get(PurchaseTransactionTable.COINS_BALANCE_CHANGE));
                playerWithPurchDataTemp.setPurchaseData(tmpTransData);
                                
                
                transDataList.add(playerWithPurchDataTemp);                
            }
            return transDataList;            
        }else{
            return null;
        }
    }    
}
