<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Usage/Performance Data
        </title>

        <%@include file="styles_scripts.jsp" %>

        <link href="${pageContext.request.contextPath}/css/table_style.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/js/jquery.tablesorter.js"></script>

        <script type="text/javascript">

            $(document).ready(function(){
                
                $("#tablesorter-demo").tablesorter();
                                
                $(".actionbutton").click(function(){                    
                    var answer = confirm("Are you sure you want to delete this record?\n This cannot be undone !!!");
                    if (answer){
                        return true;
                    }
                    else{
                        return false;
                    }
                });
                
                
                $(".status_tick_icon").click(function(){
                    var self = $(this);
                    alert("Value : " + self.attr("alt"));
                });
                
                
           
                
            });    
        </script>



    </head>
    <body>        
        <%@include file="header.jsp" %>


        <h4>           
            <s:property value="message" />
        </h4>

        <h4>Show all (5000 max) of ${numberOfStatRecords}</h4>
        <s:form action="viewnavigationstatistics">
            <s:hidden name="showAll" value="true"/>            
            <s:submit value="Show All"/>
        </s:form>

        <h3>Game navigation statistics</h3>
        <table id="tablesorter-demo" class="tablesorter" border="1">
            <thead>
                <tr>
                    <th>No</th>
                    <th>App ID</th>
                    <th>Player name</th>
                    <th>Usage date</th>
                    <th>Usage time(UTC)</th>
                    <th>Hangar Visits</th>
                    <th>Coins btn</th>
                    <th>FB share ACHT</th>
                    <th>Tournaments</th>
                    <th>Lvl square</th>
                    <th>Lvl triangle</th>
                    <th>Lvl hex</th>
                    <th>FB invite friend</th>
                    <th>Music OFF</th>

                    <th>coins100</th>
                    <th>coins250</th>
                    <th>coins550</th>
                    <th>training</th>
                    <th>buy</th>
                    <th>avatar</th>
                    <th>ship</th>
                    <th>weapon</th>
                    <th>perks</th>

                    <th>Actions</th>
                </tr>
            </thead>

            <tbody>

                <c:forEach var="entry" items="${navigationStatistics}" varStatus="loop">
                    <tr>
                        <td>${loop.index +1}</td>
                        <td>${entry.applicationId}</td>
                        <td>${entry.playerName}</td>
                        <td>${entry.dataCollectionDate}</td>
                        <td>${entry.dataCollectionTime}</td>
                        <td>${entry.numOfHangarVisits}</td>
                        <td>${entry.numOfCoinsButtonPress}</td>
                        <td>${entry.fbShareAcheivement}</td>
                        <td>${entry.tournamentsButton}</td>
                        <td>${entry.levelSquare}</td>
                        <td>${entry.levelTriangle}</td>
                        <td>${entry.levelHex}</td>
                        <td>${entry.fbFriends}</td>
                        <td>${entry.musicOffBtn}</td>
                                                
                        <td>${entry.coins100}</td>
                        <td>${entry.coins250}</td>
                        <td>${entry.coins550}</td>
                        <td>${entry.training}</td>
                        <td>${entry.buy}</td>
                        <td>${entry.avatar}</td>
                        <td>${entry.ship}</td>
                        <td>${entry.weapon}</td>
                        <td>${entry.perks}</td>
                        

                        <td>
                            <form name="input" action="deletegamenavigationstatrecord.action" method="post">
                                <input type="hidden" name="statRecordIdStringVal" value="${entry.applicationId}"/>
                                <input type="submit" class="actionbutton" value="Delete">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

    </body>
</html>
