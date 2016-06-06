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


        <h4>Server memory state</h4>
        <table border="1">
            <tr>
                <td>Current Heap Size, MB</td>
                <td>${currentHeapSize/1048576}</td>
            </tr>
            <tr>
                <td>Max Heap Size, MB</td>
                <td>${maxHeapSize/1048576}</td>
            </tr>
            <tr>
                <td>Free Heap Size, MB</td>
                <td>${freeHeapSize/1048576}</td>
            </tr>
        </table>
        <hr/>


        <h4>WS Response timing legend</h4>
        <table border="1">
            <tr>
                <td>Average response time, ms</td>
                <td>0</td>
                <td>&lt;</td>
                <td><img src="${pageContext.request.contextPath}/images/tick_ok.jpg" alt="OK"/></td>
                <td>${maxAllowedAverageWSResponseTimeMillis}</td>
                <td>&lt;</td>
                <td><img src="${pageContext.request.contextPath}/images/tick_not_ok.jpg" alt="Failed"/></td>
            </tr>
            <tr>
                <td>Maximum response time, ms</td>
                <td>0</td>
                <td>&lt;</td>
                <td><img src="${pageContext.request.contextPath}/images/tick_ok.jpg" alt="OK"/></td>
                <td>${maxAllowedMaximumWSResponseTimeMillis}</td>
                <td>&lt;</td>
                <td><img src="${pageContext.request.contextPath}/images/tick_not_ok.jpg" alt="Failed"/></td>
            </tr>

        </table>
        <hr/>        

        <h5>Current time = UTC + 3</h5>
        
        <h4>Show all (5000 max) of ${numberOfStatRecords}</h4>
        <s:form action="viewusagestat">
            <s:hidden name="showAll" value="true"/>            
            <s:submit value="Show All"/>
        </s:form>

        <h3>Game usage/performance statistics</h3>
        <table id="tablesorter-demo" class="tablesorter" border="1">
            <thead>
                <tr>
                    <th>No</th>
                    <th>App ID</th>
                    <th>Player name</th>
                    <th>Usage date</th>
                    <th>Usage time(UTC)</th>
                    <th>Time spent total(sec)</th>
                    <th>Time spent (last)(sec)</th>
                    <th>Used times</th>
                    <th>Aver ws response(ms)</th>
                    <th>Max ws response(ms)</th>
                    <th>Min ws response(ms)</th>
                    <th>Aver Ping</th>
                    <th>Max Ping</th>
                    <th>Min Ping</th>
                    <th>Aver FPS</th>
                    <th>Max FPS</th>
                    <th>Min FPS</th>
                    <th>Time in SP mode (sec)</th>
                    <th>Played SP mode(times)</th>
                    <th>Actions</th>
                </tr>
            </thead>

            <tbody>

                <c:forEach var="entry" items="${usageStatistics}" varStatus="loop">
                    <tr>
                        <td>${loop.index +1}</td>
                        <td>${entry.applicationId}</td>
                        <td>${entry.playerName}</td>
                        <td>${entry.dataCollectionDate}</td>
                        <td>${entry.dataCollectionTime}</td>
                        <td>${entry.usageTime}</td>
                        <td>${entry.lastUsageTime}</td>
                        <td>${entry.usedTimes}</td>

                        <td>
                            <c:if test="${(entry.averageRespWait <= maxAllowedAverageWSResponseTimeMillis)}">
                                <img class="status_tick_icon" src="${pageContext.request.contextPath}/images/tick_ok.jpg" alt="${entry.averageRespWait}"/>
                            </c:if>
                            <c:if test="${(entry.averageRespWait > maxAllowedAverageWSResponseTimeMillis)}">
                                <img class="status_tick_icon" src="${pageContext.request.contextPath}/images/tick_not_ok.jpg" alt="${entry.averageRespWait}"/>
                            </c:if>
                            ${entry.averageRespWait}
                        </td>

                        <td>
                            <c:if test="${(entry.maxRespWait <= maxAllowedMaximumWSResponseTimeMillis)}">
                                <img class="status_tick_icon" src="${pageContext.request.contextPath}/images/tick_ok.jpg" alt="${entry.maxRespWait}"/>
                            </c:if>
                            <c:if test="${(entry.maxRespWait > maxAllowedMaximumWSResponseTimeMillis)}">
                                <img class="status_tick_icon" src="${pageContext.request.contextPath}/images/tick_not_ok.jpg" alt="${entry.maxRespWait}"/>
                            </c:if>
                            ${entry.maxRespWait}
                        </td>
                        <td>${entry.minRespWait}</td>


                        <td>${entry.averPing}</td>
                        <td>${entry.maxPing}</td>
                        <td>${entry.minPing}</td>
                        <td>${entry.averFps}</td>
                        <td>${entry.maxFps}</td>
                        <td>${entry.minFps}</td>
                        <td>${entry.secondsOfUsageSPMode}</td>
                        <td>${entry.numberOfSPGames}</td>
                        <td>
                            <form name="input" action="deletegameusagestatrecord.action" method="post">
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
