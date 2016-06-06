<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Players admin page
        </title>

        <%@include file="styles_scripts.jsp" %>

        <link href="${pageContext.request.contextPath}/css/table_style.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/js/jquery.tablesorter.js"></script>

        <script type="text/javascript">

            $(document).ready(function(){
                
                $("#tablesorter-demo").tablesorter();
                       
                
            });    
        </script>


    </head>
    <body>

        <%@include file="header.jsp" %>

        <h4>           
            <s:property value="message" />
        </h4>

        <h4>Players list admin page</h4>

        <hr/>

        <h3>Players(${numberOfPlayers})</h3>
        <hr/>
        <h4>Show all (5000 max)</h4>
        <s:form action="viewplayerslist">
            <s:hidden name="showAll" value="true"/>            
            <s:submit value="Show All - Can be used only in DEV mode !!!"/>
        </s:form>
        
        <hr/>
        <s:form action="searchplayerbyname">
            <s:textfield name="playerNameStringVal"/>            
            <s:submit value="Search player"/>
        </s:form>
        <hr/>
        

        <table id="tablesorter-demo" class="tablesorter" border="1">
            <thead>
                <tr>
                    <th>No</th>
                    <th>Player name</th>
                    <th>Single player progress</th>
                    <th>Achievements</th>
                    <th>XP</th>
                    <th>Coins</th>
                    <th>Actions</th>
                </tr>
            </thead>

            <c:forEach var="entry" items="${playersList}" varStatus="loop">
                <tr>
                    <td>${loop.index +1}</td>
                    <td>${entry.player.name}</td>
                    <td>${entry.levelsProgress.levelsProgressData}</td>                    
                    <td>${entry.gameProfile.achievementsList}</td>
                    <td>${entry.gameProfile.experiencePoints}</td>
                    <td>${entry.gameProfile.coinsBalance}</td>
                    <td>
                        

                        <form name="input" action="viewplayerdetails.action" method="post">
                            <input type="hidden" name="playerIdStringVal" value="${entry.player.id}"/>
                            <input type="submit" class="actionbutton" value="View details">
                        </form>
                    </td>

                </tr>

            </c:forEach>            



        </table>


    </body>
</html>
