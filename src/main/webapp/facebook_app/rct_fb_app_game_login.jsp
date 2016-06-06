<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login - BFS-Multiplayer</title>

        <jsp:include page="header_references.jsp">
            <jsp:param name="baseurl" value="${externalResourcesUrl}"/>
        </jsp:include>

        <script type="text/javascript">
            $(document).ready(function(){
                
                $(".submit").click(function(){
                    var playerName = $("#getlink_for_playername").val();                    
                    var email = $.trim($("#email_to_get_secure_link_to").val());                    
                    if((playerName == null) ||(email == null) || (playerName == '') || (email == '')){
                        alert('Please fill in required fields');
                        return false;
                    }else{
                        if(!IsEmail(email)){
                            alert('Email is wrong please check!');
                            return false;
                        }
                    }
                    
                    var button = $(this);
                    button.attr("value", "Please wait ...");
                    return true;
                });
                

                
            });
            
      
            
        </script>        

    </head>
    <body>

        <div id="wrapper">
            <div id="header">
                <img src="${externalResourcesUrl}/img/logo.png" alt="Cosmobasters" id="logo" />
            </div>

            <div id="content">

                <c:if test="${message != null}">
                    <h3>${message}</h3>
                </c:if>


                <form id="commentForm" method="post" action="">
                    <input type="hidden" name="command" value="getsecurelinkatemail"/>
                    <fieldset>
                        <legend>Please login using your game credentials</legend>
                        <p>
                            <label for="getlink_for_playername">Name in game:</label>
                            <input id="getlink_for_playername" type="text" name="getlink_for_playername" class="required" minlength="2" />
                        </p>
                        <p>
                            <label for="email_to_get_secure_link_to">Email</label>
                            <input id="email_to_get_secure_link_to" type="text" name="email_to_get_secure_link_to" />
                        </p>
                        <p>
                            <input class="submit" type="submit" value="Get authorization link"/>
                        </p>
                    </fieldset>
                </form>

                <br/>
                <a class="btn" href="${homeurl}">Back</a>

            </div>
        </div>
    </body>
</html>
