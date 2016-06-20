<%@include file="jsp_header.jsp"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin console</title>

        <%@include file="styles_scripts.jsp" %>

    </head>
    <body>

        <%@include file="header.jsp" %>

        <div>Rocket App Admin menu (RCT_UNI)</div>

        <h3><a href="editparams.action">Edit mobile app config params</a></h3>
        <h3><a href="editrockets.action">Rockets edit form</a></h3>
        <h3><a href="editperks.action">Perks edit form</a></h3>
        <h3><a href="editweapon.action">Weapon edit form</a></h3>
        <h3><a href="editachievements.action">Achievements edit form</a></h3>
        <h3><a href="managefileconfig.action">Upload/download config files</a></h3>
        <h3><a href="viewstatisticshome.action">Statistics</a></h3>
        <h3><a href="viewplayerslist.action">Players list (admin functionality)</a></h3>
        <h3><a href="viewtransactions.action">Purchase transactions</a></h3>
        <h3><a href="log_viewremotelogs.action">Log (Remote apps)</a></h3>
        <h3><a href="gcm_viewgcmusers.action">GCM Users admin</a></h3>
        <h3><a href="gamification_viewchallenges.action">Gamification admin</a></h3>


        <hr/>
        <%--   --%>
        <h6>DEV Build 1.11.1(AWS+Maven edition) : 20***-JUN-2016</h6>
        
        <h5>Admin console. Version: 1.11.1 Build : 17-JUN-2016</h5>
        <h6>Krokus-tech. 2012-2016</h6>

    </body>
</html>