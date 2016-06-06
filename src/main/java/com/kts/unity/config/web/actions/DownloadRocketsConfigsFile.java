package com.kts.unity.config.web.actions;

import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.ConfigParams;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.springframework.core.io.Resource;

public class DownloadRocketsConfigsFile extends ActionSupport{
    
    
    private InputStream fileInputStream;

    @Override
    public String execute() throws Exception {

        Resource logFileResource = AppContext.getContext().getResource(ConfigParams.APP_ROCKETS_CONFIG_FILE_PATH_NAME);
        fileInputStream = new FileInputStream(new File(logFileResource.getFile().getPath()));

        return ActionConstants.SUCCESS;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }    
    
    
}
