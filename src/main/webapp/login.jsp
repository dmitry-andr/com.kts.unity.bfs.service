<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin console</title>
        
        <%@include file="styles_scripts.jsp" %>
        
    </head>
    <body>
        <h4>           
            <s:property value="message" />
        </h4>
        <h3>Please login</h3>
        
        <s:form action="login">
            <s:textfield name="userName" label="User Name"/>
            <s:password name="password" label="Password"/>
            <s:submit value="Login"/>
        </s:form>
    </body>
</html>
