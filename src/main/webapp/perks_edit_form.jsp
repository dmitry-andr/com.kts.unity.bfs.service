<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Edit Perks
        </title>
        
        <%@include file="styles_scripts.jsp" %>

    </head>
    <body>
        <%@include file="header.jsp" %>
              
        <h3>View/Edit perks params</h3>

        <h4><s:property value="message" /></h4>
        
        
        
        <s:form action="saverocketsconfigformdata" method="post" enctype="multipart/form-data" >
            <div class="config_entries_container"> 
               
                    <c:forEach var="entry" items="${config.entries}" varStatus="loop">                        
                        <h3>${loop.index +1} - Perk - ${entry.name}</h3>
                            <div class="entry">
                                <div class="entrydisplname">ID</div>
                                <div class="entryval"><input name="id_value_${loop.index}" type="text" value="${entry.id}"/></div>                                
                            </div>
                            
                            <div class="entry">
                                <div class="entrydisplname">Description</div>
                                <div class="entryval"><input name="description_value_${loop.index}" type="text" value="${entry.description}"/></div>                                
                            </div>
                            
                            <div class="entry">
                                <div class="entrydisplname">Min rank required</div>
                                <div class="entryval"><input name="name_value_${loop.index}" type="text" value="${entry.minrank}"/></div>
                            </div>                            
                            <div class="entry">
                                <div class="entrydisplname">Price (coins)</div>
                                <div class="entryval"><input name="priceCoins_value_${loop.index}" type="text" value="${entry.priceCoins}"/></div>
                            </div>
                    </c:forEach>
               
            </div>
            <%--
            <s:submit value="Save(nor implemented yet !!!)"/>
            --%>
        </s:form>

        <a href="editperks.action">Cancel</a>
        
 
        
        
    </body>
</html>