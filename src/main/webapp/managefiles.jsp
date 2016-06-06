<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Configuration Files</title>
        
        <%@include file="styles_scripts.jsp" %>
        
    </head>
    <body>
        
        <%@include file="header.jsp" %>
        
        <h3>Configuration files administration</h3>
        <h3 style="background: blue; color: red;"><s:property value="message" /></h3>
        
        
        <%-- This functionality is obsolete as DB used for log records saving
        <h4>Remote applications(mobile devices) log</h4>
         
        <s:url id="fileDownload" namespace="/" action="downloadlog" ></s:url>
        <h5>
            Download mobile app log file - <s:a href="%{fileDownload}">Log file</s:a>
        </h5>        
        --%>
        <h4>_________________________________________________</h4>
        
        
        <h4>Mobile application config</h4>        
        <s:url id="downloadmobappcfg" namespace="/" action="downloadmobappcfg" ></s:url>
        <s:a href="%{downloadmobappcfg}">Download current configuration</s:a>
        <br/>
        <h3>Upload new game configuration</h3>
        <s:form action="fileUpload" method="post" enctype="multipart/form-data" >
            <s:file name="rocketAppConfig" label="Rocket App config file " />
            <s:submit value="Upload"/>
        </s:form>
        <h4>_________________________________________________</h4>
        
        
        
        <h4>Perks config</h4>        
        <s:url id="downloadperkscfg" namespace="/" action="downloadperkscfg" ></s:url>
        <s:a href="%{downloadperkscfg}">Download perks configuration</s:a>
        <br/>
        <h3>Upload new perks configuration</h3>
        <s:form action="perksFileUpload" method="post" enctype="multipart/form-data" >
            <s:file name="perksConfig" label="Rocket App perks config file " />
            <s:submit value="Upload"/>
        </s:form>
        <h4>_________________________________________________</h4>
        
        
        <h4>Weapon config</h4>        
        <s:url id="downloadweaponcfg" namespace="/" action="downloadweaponcfg" ></s:url>
        <s:a href="%{downloadweaponcfg}">Download weapon configuration</s:a>
        <br/>
        <h3>Upload new weapon configuration</h3>
        <s:form action="weaponFileUpload" method="post" enctype="multipart/form-data" >
            <s:file name="weaponConfig" label="Rocket App weapon config file " />
            <s:submit value="Upload"/>
        </s:form>
        <h4>_________________________________________________</h4>
        
        
        
        <h4>Avatars config</h4>        
        <s:url id="downloadavatarscfg" namespace="/" action="downloadavatarscfg" ></s:url>
        <s:a href="%{downloadavatarscfg}">Download avatars configuration</s:a>
        <br/>
        <h3>Upload new avatars configuration</h3>
        <s:form action="avatarsFileUpload" method="post" enctype="multipart/form-data" >
            <s:file name="avatarsConfig" label="Avatars config file " />
            <s:submit value="Upload"/>
        </s:form>
        <h4>_________________________________________________</h4>
        

        
        <h4>Rockets config</h4>        
        <s:url id="downloadrktscfg" namespace="/" action="downloadrktscfg" ></s:url>
        <s:a href="%{downloadrktscfg}">Download current configuration</s:a>
       
        <br/>
        <h3>Upload rockets configuration</h3>
        <s:form action="rocketsFileUpload" method="post" enctype="multipart/form-data" >
            <s:file name="rocketsConfig" label="Rockets config file path " />
            <s:submit value="Upload"/>
        </s:form>
        <h4>_________________________________________________</h4>
        
        
        
        <h4>Achievements config</h4>        
        <s:url id="downloadachievementscfg" namespace="/" action="downloadachievementscfg" ></s:url>
        <s:a href="%{downloadachievementscfg}">Download achievements configuration</s:a>
       
        <br/>
        <h3>Upload achievements configuration</h3>
        <s:form action="achievementsFileUpload" method="post" enctype="multipart/form-data" >
            <s:file name="achievmentsConfig" label="Achievements config file path " />
            <s:submit value="Upload"/>
        </s:form>
        <h4>_________________________________________________</h4>
        
        
        <h4>Bots config</h4>        
        <s:url id="downloadbotscfg" namespace="/" action="downloadbotscfg" ></s:url>
        <s:a href="%{downloadbotscfg}">Download angry bots configuration</s:a>
       
        <br/>
        <h3>Upload angry bots configuration</h3>
        <s:form action="botsConfigurationFileUpload" method="post" enctype="multipart/form-data" >
            <s:file name="botsConfig" label="Bots config file path " />
            <s:submit value="Upload"/>
        </s:form>
        <h4>_________________________________________________</h4>
        
    </body>
</html>
