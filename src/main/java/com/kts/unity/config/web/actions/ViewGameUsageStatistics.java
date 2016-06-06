package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.backend.ConfigManager;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.entities.GameUsageStatistics;
import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.AppSpringBeans;
import java.util.ArrayList;

public class ViewGameUsageStatistics {
    
    private ArrayList<GameUsageStatistics> usageStatistics;
    private int maxAllowedAverageWSResponseTimeMillis;
    private int maxAllowedMaximumWSResponseTimeMillis;
    private String showAll;
    private int numberOfStatRecords;
    
    public String execute(){
        
        this.maxAllowedAverageWSResponseTimeMillis = Settings.getMaxAllowedAverageWSResponseTimeMillis();
        this.maxAllowedMaximumWSResponseTimeMillis = Settings.getMaxAllowedMaximumWSResponseTimeMillis();
        
        
        ConfigManager confMgr =  (ConfigManager) AppContext.getContext().getBean(AppSpringBeans.CONFIGS_MANAGER_BEAN);        
        this.numberOfStatRecords = confMgr.getNumberOfStatisticsRecords();
        
        if("true".equals(showAll)){
            this.usageStatistics = confMgr.getUsageStatisticsRecords(Settings.getMaxNumOfPlayersOnAdminPage());            
        }else{
            this.usageStatistics = confMgr.getUsageStatisticsRecords(Settings.getInitialNumOfPlayersOnAdminPage());             
        }
                
        return ActionConstants.SUCCESS;
    }
    

    public ArrayList<GameUsageStatistics> getUsageStatistics() {
        return usageStatistics;
    }

    public void setUsageStatistics(ArrayList<GameUsageStatistics> usageStatistics) {
        this.usageStatistics = usageStatistics;
    }

    public long getCurrentHeapSize() {
        return Runtime.getRuntime().totalMemory();
    }

    public long getFreeHeapSize() {
        return Runtime.getRuntime().freeMemory();
    }

    public long getMaxHeapSize() {
        return Runtime.getRuntime().maxMemory();
    }

    public int getMaxAllowedAverageWSResponseTimeMillis() {
        return maxAllowedAverageWSResponseTimeMillis;
    }

    public int getMaxAllowedMaximumWSResponseTimeMillis() {
        return maxAllowedMaximumWSResponseTimeMillis;
    }
    
    public String getShowAll() {
        return showAll;
    }

    public void setShowAll(String showAll) {
        this.showAll = showAll;
    }

    public int getNumberOfStatRecords() {
        return numberOfStatRecords;
    }

    public void setNumberOfStatRecords(int numberOfStatRecords) {
        this.numberOfStatRecords = numberOfStatRecords;
    }
    
    
}
