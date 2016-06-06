package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.entities.SecCode;
import com.kts.unity.config.web.backend.utils.SecCodeTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class SecCodeDAO extends SimpleJdbcDaoSupport {

    public int saveSecCodeForPlayer(SecCode secCode) throws Exception{
        int status = 0;

        String sql = "insert into " + SecCodeTable.TBL_NAME + "(" + SecCodeTable.PLAYER_ID + "," + SecCodeTable.SEC_CODE + "," + SecCodeTable.PERMANENT + ") values(?,?,?)";

        getSimpleJdbcTemplate().update(
                sql,
                secCode.getPlayerId(),
                secCode.getSecCode(),
                secCode.getPermanent());
        status = 1;


        return status;
    }

    public SecCode getSecCodeForPlayer(int playerId) {

        String sql = "SELECT * FROM " + SecCodeTable.TBL_NAME + " WHERE " + SecCodeTable.PLAYER_ID + " = ?";
    
        SecCode secCode = null;
        
        try{         
            secCode = (SecCode) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new SecCodeDAO.SecCodeRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){
            //add logging here
        }
        
        return secCode;
    }
    
    public Integer getPlayerIDForSecCode(String secCocde) {

        String sql = "SELECT * FROM " + SecCodeTable.TBL_NAME + " WHERE " + SecCodeTable.SEC_CODE + " = ?";
    
        Integer playerId = null;
        
        try{         
            playerId = ((SecCode) this.getJdbcTemplate().queryForObject(sql, new Object[]{secCocde}, new SecCodeDAO.SecCodeRowMapper())).getPlayerId();
        }catch(IncorrectResultSizeDataAccessException ex){
            //add logging here
        }
        
        return playerId;
    }
    

    public int removeSecCodeForPlayer(int playerId) {
        int status = 0;
        
        String sql = "DELETE FROM " + SecCodeTable.TBL_NAME + " WHERE " + SecCodeTable.PLAYER_ID + " = ?";
        getSimpleJdbcTemplate().update(sql, playerId);
        status = 1;
        
        return status;
    }

    private class SecCodeRowMapper implements RowMapper {

        public SecCodeRowMapper() {
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            SecCode secCode = new SecCode();
            secCode.setPlayerId(rs.getInt(SecCodeTable.PLAYER_ID));
            secCode.setSecCode(rs.getString(SecCodeTable.SEC_CODE));
            secCode.setPermanent(rs.getInt(SecCodeTable.PERMANENT));

            return secCode;
        }
    }
}
