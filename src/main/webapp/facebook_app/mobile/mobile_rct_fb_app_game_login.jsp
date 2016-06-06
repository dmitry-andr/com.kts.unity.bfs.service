<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login page - Mobile</title>

        <%@include file="css_js_references.jsp" %>

        <script type="text/javascript">
            $(document).ready(function(){
                $("#feedback").validate();
                
                $(".submit").click(function(){
                    var playerName = $.trim($("#getlink_for_playername").val());
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
                    var $button = $(this);
                    $button.attr("value", "Please wait ...").button("refresh");
                    return true;
                });
                
                
                
            
            });
            
            


            
        </script>        

    </head>
    <body>

        <div data-role="page" data-theme="a" id="page3">
            <div data-theme="a" data-role="header">
                <h3>
                    Sign in
                </h3>
                <a data-role="button" data-ajax="false" data-theme="a" href="${homeurl}" class="ui-btn-left" data-icon="back" data-iconpos="left">
                    Back
                </a>
            </div>

            <c:if test="${message != null}">
                <ul>
                    <li>
                        <h3>${message}</h3>
                    </li>
                </ul>                
            </c:if>

            <div data-role="content" style="padding:0 15px 15px 15px">
                <form data-ajax="false" method="POST">
                    <input type="hidden" name="command" value="getsecurelinkatemail"/>
                    <div data-role="fieldcontain">
                        <fieldset data-role="controlgroup">
                            <label for="login_playername">Name in game:</label>
                            <input type="text" id="getlink_for_playername" name="getlink_for_playername"/>
                        </fieldset>
                    </div>
                    <div data-role="fieldcontain">
                        <fieldset data-role="controlgroup">
                            <label for="login_password">Email:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            <input type="text" id="email_to_get_secure_link_to" name="email_to_get_secure_link_to"/>
                        </fieldset>
                    </div>
                    <input class="submit" type="submit" value="Get authorization link"/>
                </form>

                <div>
                    <p style="text-align: center; ">
                        <b>
                            2013-2014
                        </b>
                    </p>
                </div>
            </div>


        </div>
    </body>
</html>
