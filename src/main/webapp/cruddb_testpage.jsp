<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin console</title>
        <%@include file="styles_scripts.jsp" %>
    </head>
    <body>
        <%@include file="header.jsp" %>
        <h4>STATUS : <s:property value="message" /></h4>
        
        
        <h3>Test create admin account DB operation</h3>
        <s:form action="runbdoperationtest">
            <s:textfield name="adminName" label="Admin Name"/>
            <s:textfield name="adminPassword" label="Password"/>
            <s:submit/>
        </s:form>
        
        <h4>___________________________________________________________</h4>
        <h5>Create Player test</h5>
        <s:form action="runbdoperationtest">
            <s:textfield name="playerName" label="Player Name"/>
            <s:textfield name="playerPassword" label="Password"/>
            <s:textfield name="playerEmail" label="Email"/>
            <s:submit/>
        </s:form>
        
    </body>
</html>
