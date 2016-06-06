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
                
                $(".actionbutton").click(function(){                    
                    var answer = confirm("Are you sure you want to delete this record?\nPlease be aware that this is 'heavy' operation and cannot be undone !!!");
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

        <h3><a href="viewplayerslist.action">Back to list</a></h3>

        <h4>           
            <s:property value="message" />
        </h4>

        <h4>Player details</h4>

        <hr/>


        <c:if test="${(playerData == null)}">
            No data for player found
        </c:if>

        <c:if test="${(playerData != null)}">
            <table>
                <tr>
                    <td>ID</td>
                    <td>${playerData.player.id}</td>
                </tr>
                <tr>
                    <td>Name</td>
                    <td>${playerData.player.name}</td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td>${playerData.player.email}</td>
                </tr>
                <tr>
                    <td>Scores</td>
                    <td>${playerData.gameProfile.scores}</td>                    
                </tr>
                <tr>
                    <td>Purchased items</td>
                    <td>${playerData.gameProfile.puchasedItemsList}</td>
                </tr>
                <tr>
                    <td>Coins balance</td>
                    <td>${playerData.gameProfile.coinsBalance}</td>
                </tr>
                <tr>
                    <td>Notify about online players</td>
                    <td>${playerData.gameProfile.notifyAboutOnlinePlayers}</td>
                </tr>
                

            </table>

            <hr/>
            <form name="input" action="deleteuserfromconsole.action" method="post">
                <input type="hidden" name="playerIdStringVal" value="${playerData.player.id}"/>
                <input type="submit" class="actionbutton" value="Delete">
            </form>

        </c:if>

    </body>
</html>
