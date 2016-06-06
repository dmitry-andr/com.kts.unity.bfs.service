<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Log (Remote)
        </title>

        <%@include file="styles_scripts.jsp" %>

        <link href="${pageContext.request.contextPath}/css/table_style.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/js/jquery.tablesorter.js"></script>

        <script type="text/javascript">

            $(document).ready(function(){
                
                $("#tablesorter-demo").tablesorter();
                
                
                $("#clearLogsButton").click(function(){
                    var answer = confirm("You selected to clean logs.\nThis operation cannot be undone.\nPlease confirm.");
                    if (answer){
                        return true;
                    }
                    else{
                        return false;
                    }
                });
                
                
                $(".actionbutton").click(function(){
                    
                    alert("Don't press it !!!!");           


                    /*                    
                    var answer = confirm("Are you sure you want to delete this record?\n This cannot be undone !!!");
                    if (answer){
                        return true;
                    }
                    else{
                        return false;
                    }
                     */                    
                });
                
                
            });    
        </script>


    </head>
    <body>

        <%@include file="header.jsp" %>

        <h4>
            <s:property value="message" />
        </h4>

        <h4>Log viewer admin page</h4>

        <hr/>


        <form action="log_update_status_notify_admins_on_logs_health.action" method="post">
            <input type="checkbox" name="notifyAdminsStatusStrVal" <c:if test="${notifyAdminsOnLogsIssues}">checked</c:if>/>
            Notify admins about issues when too much logs reported
            <input type="submit" value="Save"/>
        </form>

        <hr/>

        <h3>Log records(${numberOfRecords})</h3>
        <h4>Show all (5000 max)</h4>
        <s:form action="log_viewremotelogs">
            <s:hidden name="showAll" value="true"/>            
            <s:submit value="Show All"/>
        </s:form>

        <hr/>



        <form action="log_clearremotelogs.action" method="post">
            Keep last
            <select name ="selectedNumOfRecToKeep">
                <option value="100">100</option>
                <option value="200">200</option>
                <option value="500">500</option>
            </select>
            records
            <input type="submit" id="clearLogsButton" value="Clear"/>
        </form>




        <table id="tablesorter-demo" class="tablesorter" border="1">
            <thead>
                <tr>
                    <th>No</th>                    
                    <th>Player Name</th>
                    <th>App version</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Text</th>

                </tr>
            </thead>

            <c:forEach var="entry" items="${logRecordsList}" varStatus="loop">
                <tr>
                    <td>${loop.index +1}</td>                    
                    <td>${entry.playerName}</td>
                    <td>${entry.remoteAppVersion}</td>
                    <td>${entry.transactionDate}</td>
                    <td>${entry.transactionTime}</td>
                    <td>${entry.logRegordText}</td>                    
                </tr>

            </c:forEach>            



        </table>


    </body>
</html>