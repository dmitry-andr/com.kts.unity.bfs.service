<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Edit Achievements
        </title>
        
        <%@include file="styles_scripts.jsp" %>

    </head>
    <body>
        <%@include file="header.jsp" %>
              
        <h3>Achievements</h3>

        <h4><s:property value="message" /></h4>

        <s:form action="saveachievementsconfigformdata" method="post" enctype="multipart/form-data" >
            <div class="config_entries_container">
                    <c:forEach var="entry" items="${config.entries}" varStatus="loop">                        
                        <h3>${loop.index +1} - Achievement - ${entry.name}</h3>
                            <div class="entry">
                                <div class="entrydisplname">ID</div>
                                <div class="entryval"><input name="id_value_${loop.index}" type="text" value="${entry.id}"/></div>                                
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">Name</div>
                                <div class="entryval"><input name="name_value_${loop.index}" type="text" value="${entry.name}" size="75"/></div>
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">Description</div>
                                <div class="entryval"><input name="achdescription_value_${loop.index}" type="text" value="${entry.description}" size="75"/></div>
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">Icon</div>
                                <div class="entryval"><input name="icon_value_${loop.index}" type="text" value="${entry.icon}" size="75"/></div>
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">Points</div>
                                <div class="entryval"><input name="points_value_${loop.index}" type="text" value="${entry.points}"/></div>
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">List order</div>
                                <div class="entryval"><input name="list_order_value_${loop.index}" type="text" value="${entry.listOrder}"/></div>
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">Awarding coins</div>
                                <div class="entryval"><input name="awrdcoins_order_value_${loop.index}" type="text" value="${entry.awardingCoins}"/></div>
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">State (hidden, revealed)</div>
                                <div class="entryval"><input name="state_value_${loop.index}" type="text" value="${entry.state}"/></div>
                            </div>
                            
                            
                    </c:forEach>
            </div>
            <s:submit value="Save"/>
        </s:form>

        <a href="editachievements.action">Cancel</a>

    </body>
</html>
