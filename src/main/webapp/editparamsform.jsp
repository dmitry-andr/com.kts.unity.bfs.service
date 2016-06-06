<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Edit RCT config params
        </title>
        
        <%@include file="styles_scripts.jsp" %>
       
    </head>
    <body>
        
        <%@include file="header.jsp" %>
        
        <h3>Edit application parameters using form below</h3>
        
        <h4><s:property value="message" /></h4>
        
        <s:form action="saveconfigformdata" method="post" enctype="multipart/form-data" >
            <div class="config_entries_container">            
                <c:forEach var="entry" items="${config.entries}" varStatus="loop">
                    <div class="entry">
                        <div class="entrydisplname">${loop.index +1} - ${entry.displayName}</div>
                        <div class="entryval"><input name="entry_value_${loop.index}" type="text" size="25" value="${entry.value}"/></div>
                        <div class="entryname">${entry.name}</div>
                    </div>

                </c:forEach>
            </div>
            <s:submit value="Save"/>
        </s:form>

        <a href="editparams.action">Cancel</a>

    </body>
</html>
