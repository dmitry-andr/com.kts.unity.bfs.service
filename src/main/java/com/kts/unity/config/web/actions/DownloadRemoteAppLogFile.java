package com.kts.unity.config.web.actions;

import com.opensymphony.xwork2.ActionSupport;
import java.io.InputStream;

@Deprecated
public class DownloadRemoteAppLogFile extends ActionSupport {

    private InputStream fileInputStream;

    @Override
    public String execute() throws Exception {

        //Resource logFileResource = AppContext.getContext().getResource(ConfigParams.APP_LOG_FILE_PATH_NAME);
        //fileInputStream = new FileInputStream(new File(logFileResource.getFile().getPath()));


        return ActionConstants.SUCCESS;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }
}
