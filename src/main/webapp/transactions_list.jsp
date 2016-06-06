<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Transactions list
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

        <h3>Transactions(${numberOfRecords})</h3>
        <h4>Show all (5000 max)</h4>
        <s:form action="viewtransactions">
            <s:hidden name="showAll" value="true"/>            
            <s:submit value="Show All"/>
        </s:form>

        <table id="tablesorter-demo" class="tablesorter" border="1">
            <thead>
                <tr>
                    <th>No</th>
                    <th>Player name</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Operation</th>
                    <th>Item</th>
                    <th>Coins change</th>

                    <th>Actions</th>
                </tr>
            </thead>

            <c:forEach var="entry" items="${transactionsList}" varStatus="loop">
                <tr>
                    <td>${loop.index +1}</td>
                    <td>${entry.player.name}</td>
                    <td>${entry.purchaseData.transactionDate}</td>
                    <td>${entry.purchaseData.transactionTime}</td>
                    <td>${entry.purchaseData.operationType}</td>
                    <td>${entry.purchaseData.purchasedItem}</td>
                    <td>
                        <c:if test="${entry.purchaseData.coinsBalanceChange > 0}">
                            +${entry.purchaseData.coinsBalanceChange}
                        </c:if>
                        <c:if test="${entry.purchaseData.coinsBalanceChange < 0}">
                            ${entry.purchaseData.coinsBalanceChange}
                        </c:if>
                    </td>


                    <td>
                        <form name="input" action="deletepurchasetransaction.action" method="post">
                            <input type="hidden" name="transactionIdStrVal" value="${entry.purchaseData.id}"/>
                            <input type="submit" class="actionbutton" value="Delete">
                        </form>
                    </td>

                </tr>

            </c:forEach>            



        </table>


    </body>
</html>