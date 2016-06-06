package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.backend.utils.AdminTable;
import com.kts.unity.config.web.entities.Admin;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class AdminDAO extends SimpleJdbcDaoSupport {

    public Admin getAdminByName(String adminName) {    
        String sql = "SELECT * FROM " + AdminTable.TBL_NAME + " WHERE " + AdminTable.NAME + " = ?";
        
        Admin admin = null;
        try{
            admin = (Admin) this.getJdbcTemplate().queryForObject(sql, new Object[]{adminName}, new AdminDAO.AdminRowMapper());
        }catch(IncorrectResultSizeDataAccessException ex){
            System.out.println("If this exception does not happen on regular basis - Ignore it !!!");
            ex.printStackTrace();
        }
        
        return admin;
    }

    public int createSysAdmin(Admin admin) throws Exception{
        int status = 0;

        String adminInsertQryStr = "insert into admin(name, password) values(?,?)";

        getSimpleJdbcTemplate().update(
                adminInsertQryStr,
                admin.getName(),
                admin.getPassword());

        status = 1;
        
        return status;
    }
    
    private class AdminRowMapper implements RowMapper{
        
        public AdminRowMapper(){
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            
            Admin admin = new Admin();
            
            admin.setId(rs.getInt(AdminTable.ID));
            admin.setName(rs.getString(AdminTable.NAME));
            admin.setPassword(rs.getString(AdminTable.PASSWORD));
            admin.setEmail(rs.getString(AdminTable.PASSWORD));
            
            return admin;
        }
    }
    
}
