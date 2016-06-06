package com.kts.unity.config.web.backend;

import com.kts.unity.config.web.backend.dao.AdminDAO;
import com.kts.unity.config.web.entities.Admin;

public class AdminManager {

    private AdminDAO adminDAO;

    public int checkAdminCredentials(Admin adminToCheck) {
        int status = 0;
        
        Admin adminAtServer = this.getAdminByName(adminToCheck.getName());
        
        if(adminAtServer != null){
            if((adminToCheck.getPassword().equals(adminAtServer.getPassword()))){
                return 1;
            }
        }else{
            return 2;
        }
        
        return status;
    }
    
    public Admin getAdminByName(String adminName){
        return this.adminDAO.getAdminByName(adminName);
    }

    public int createAdmin(Admin admin) {
        int status = 0;
        try {
            status = this.adminDAO.createSysAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public AdminDAO getAdminDAO() {
        return adminDAO;
    }

    public void setAdminDAO(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }
}
