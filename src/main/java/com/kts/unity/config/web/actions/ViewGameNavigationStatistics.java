package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.GameNavigationStatistics;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import java.util.ArrayList;

public class ViewGameNavigationStatistics {
    private ArrayList<GameNavigationStatistics> navigationStatistics;
    private String showAll;
    private int numberOfStatRecords;
    
    
    public String execute(){
        
        ConfigManager confMgr =  (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);        
        this.numberOfStatRecords = confMgr.getNumberOfNavigationStatisticsRecords();
        
        if("true".equals(showAll)){
            this.navigationStatistics = confMgr.getNavigationStatisticsRecords(Settings.getMaxNumOfPlayersOnAdminPage());            
        }else{
            this.navigationStatistics = confMgr.getNavigationStatisticsRecords(Settings.getInitialNumOfPlayersOnAdminPage());             
        }
        
        return ActionConstants.SUCCESS;        
    }

    public ArrayList<GameNavigationStatistics> getNavigationStatistics() {
        return navigationStatistics;
    }

    public void setNavigationStatistics(ArrayList<GameNavigationStatistics> navigationStatistics) {
        this.navigationStatistics = navigationStatistics;
    }

    public int getNumberOfStatRecords() {
        return numberOfStatRecords;
    }

    public void setNumberOfStatRecords(int numberOfStatRecords) {
        this.numberOfStatRecords = numberOfStatRecords;
    }

    public String getShowAll() {
        return showAll;
    }

    public void setShowAll(String showAll) {
        this.showAll = showAll;
    }
}
