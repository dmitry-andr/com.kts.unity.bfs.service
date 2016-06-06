<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            GCM admin page
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
                
                
                $("#send_broadcast_btn").click(function(){
                    var answ = confirm("You are going to send broadcast \n meassge to ALL registered users.\n Please confirm !!!");
                    if (answ){
                        return true;
                    }
                    else{
                        return false;
                    }
                });
                
                
            });    
        </script>


    </head>
    <body>

        <%@include file="header.jsp" %>

        <h4>
            <s:property value="message" />
        </h4>

        <h4>GCM records admin page</h4>

        <hr/>

        <h4>Limit : 1000 records to display and broadcast</h4>

        <c:if test="${notifyPlayersAboutOpponentsOnline}"><b>Next broadcast in ${daysToBroadcastRemain} days, ${hoursToBroadcastRemain} hours</b></c:if>
        <form action="gcm_save_players_notification_status.action" method="post">
            <input type="checkbox" name="notifyPlayersOnlineUsersStrVal" <c:if test="${notifyPlayersAboutOpponentsOnline}">checked</c:if>/>Notify users, who selected such option, once in
            <input type="text" size="1" name="notificationsPeriodHoursStrVal" value="${notificationsPeriodHoursStrVal}"/> hour(s) on appearing user online
            <input type="submit" value="Save"/>
        </form>


        <h3>GCM users total : (${numberOfRecords})</h3>
        <form name="input" action="gcm_sendallmessages.action" method="post">
            Message:<input type="text" size="65" name="paramGCMMsgText"/>
            <input type="submit" id="send_broadcast_btn" value="Send message to all registered GCM users"/>
        </form>

        <hr/>

        <table id="tablesorter-demo" class="tablesorter" border="1">
            <thead>
                <tr>
                    <th>No</th>
                    <th>GCM Registration ID</th>
                    <th>Player name</th>
                    <th>Email</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Action</th>
                </tr>
            </thead>

            <c:forEach var="entry" items="${gcmUsers}" varStatus="loop">
                <tr>
                    <td>${loop.index +1}</td>
                    <td>${entry.gcmRegistrationId}</td>
                    <td>${entry.name}</td>
                    <td>${entry.email}</td>
                    <td>${entry.timeStampDate}</td>
                    <td>${entry.timeStampTime}</td>                    
                    <td>
                        <form name="input" action="gcm_unregisteruser.action" method="post">
                            <input type="hidden" name="paramGcmRegId" value="${entry.gcmRegistrationId}"/>
                            <input type="submit" class="actionbutton" value="Unregister">
                        </form>
                    </td>
                </tr>
            </c:forEach>            


        </table>


    </body>
</html>
