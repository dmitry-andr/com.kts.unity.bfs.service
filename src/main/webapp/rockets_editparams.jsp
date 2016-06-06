<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Edit Rockets
        </title>
        
        <%@include file="styles_scripts.jsp" %>

    </head>
    <body>
        <%@include file="header.jsp" %>
              
        <h3>Edit rockets here</h3>

        <h4><s:property value="message" /></h4>

        <s:form action="saverocketsconfigformdata" method="post" enctype="multipart/form-data" >
            <div class="config_entries_container"> 
               
                    <c:forEach var="entry" items="${config.entries}" varStatus="loop">                        
                        <h3>${loop.index +1} - Ship - ${entry.name}</h3>
                            <div class="entry">
                                <div class="entrydisplname">ID</div>
                                <div class="entryval"><input name="id_value_${loop.index}" type="text" value="${entry.id}"/></div>                                
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">Name</div>
                                <div class="entryval"><input name="name_value_${loop.index}" type="text" value="${entry.name}"/></div>
                            </div>                            
                            <div class="entry">
                                <div class="entrydisplname">shipType</div>
                                <div class="entryval"><input name="shipType_value_${loop.index}" type="text" value="${entry.shipType}"/></div>
                            </div>               
                            <div class="entry">
                                <div class="entrydisplname">thrust</div>
                                <div class="entryval"><input name="thrust_value_${loop.index}" type="text" value="${entry.thrust}"/></div>
                            </div>                            
                            <div class="entry">
                                <div class="entrydisplname">rotationSpeed</div>
                                <div class="entryval"><input name="rotationSpeed_value_${loop.index}" type="text" value="${entry.rotationSpeed}"/></div>
                            </div>                             
                            <div class="entry">
                                <div class="entrydisplname">mass</div>
                                <div class="entryval"><input name="mass_value_${loop.index}" type="text" value="${entry.mass}"/></div>
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">drag</div>
                                <div class="entryval"><input name="drag_value_${loop.index}" type="text" value="${entry.drag}"/></div>
                            </div>                            
                            <div class="entry">
                                <div class="entrydisplname">angularDrag</div>
                                <div class="entryval"><input name="angularDrag_value_${loop.index}" type="text" value="${entry.angularDrag}"/></div>
                            </div>                             
                            <div class="entry">
                                <div class="entrydisplname">cageLimit</div>
                                <div class="entryval"><input name="cageLimit_value_${loop.index}" type="text" value="${entry.cageLimit}"/></div>
                            </div>                            
                            <div class="entry">
                                <div class="entrydisplname">hitPoints</div>
                                <div class="entryval"><input name="hitPoints_value_${loop.index}" type="text" value="${entry.hitPoints}"/></div>
                            </div>                             
                            <div class="entry">
                                <div class="entrydisplname">reloadTime</div>
                                <div class="entryval"><input name="reloadTime_value_${loop.index}" type="text" value="${entry.reloadTime}"/></div>
                            </div>                            
                            <div class="entry">
                                <div class="entrydisplname">beamVelocity</div>
                                <div class="entryval"><input name="beamVelocity_value_${loop.index}" type="text" value="${entry.beamVelocity}"/></div>
                            </div>
                            <div class="entry">
                                <div class="entrydisplname">fuseTimer</div>
                                <div class="entryval"><input name="fuseTimer_value_${loop.index}" type="text" value="${entry.fuseTimer}"/></div>
                            </div>                            
                            <div class="entry">
                                <div class="entrydisplname">rank</div>
                                <div class="entryval"><input name="rank_value_${loop.index}" type="text" value="${entry.rank}"/></div>
                            </div>
                            
                            <div class="entry">
                                <div class="entrydisplname">price (coins)</div>
                                <div class="entryval"><input name="priceCoins_value_${loop.index}" type="text" value="${entry.priceCoins}"/></div>
                            </div>
                           
                            
                            
                    </c:forEach>
               
            </div>
            <s:submit value="Save"/>
        </s:form>

        <a href="editrockets.action">Cancel</a>

    </body>
</html>
