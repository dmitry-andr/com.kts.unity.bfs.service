<!DOCTYPE html>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Friends - BFS-Multiplayer</title>


        <jsp:include page="header_references.jsp">
            <jsp:param name="baseurl" value="${externalResourcesUrl}"/>
        </jsp:include>

        <!-- Custom JavaScript goes here -->
        <script type="text/javascript">
            $(document).ready(function(){
                                
                
                $(".btn.btn_invite").click(function(){
                    var self = $(this);
                    self.html("Processing...");
                    $(".btn.btn_invite").attr("disabled", "disabled");
                    
                    FB.ui(
                    {
                        method: 'feed',
                        name: 'Hey ' + $(this).attr("id").split('#')[1].split(' ')[0] + ', join me at cool online multiplayer game!',
                        to: $(this).attr("id").split('#')[0],
                        link: '${shareLinkData.link}',
                        picture: '${shareLinkData.pictureURL}',
                        caption: 'Online multiplayer game',
                        description: '${shareLinkData.description}'
                    },
                    function(response) {
                        if (response && response.post_id) {
                            alert('Post was published.');
                            self.html("Invited!");
                            $(".btn.btn_invite").attr("disabled", "");
                        } else {
                            alert('Post was not published.');
                        }
                    }
                );
                    
                    
                    
                    /*                    
                    $.ajax({
                        type: "GET",
                        url: "${facebookAppServlet}",
                        data: ({command:'invite', friend:$(this).attr("id"), ajrequest:'true'}),
                        error: function(){
                            alert('Error on the server while processing ajax request');
                            $(".btn.btn_invite").attr("disabled", "");
                            self.html("Try again !");
                        },
                        success: function(xml){
                            if(xml != null){
                                $(".btn.btn_invite").attr("disabled", "");
                                alert('Your friend was invited \n to join the game !');                                
                                self.html("INVITED!");
                                self.attr("disabled", "disabled");
                            }else{
                                alert("No response from the server\n Please try again later");
                            }
                        }
                    });
                     */                    
                    
                    
                    return false;
                });
                
                
                
                $(".login.game").click(function(){
                    window.location = "?command=getsecurelinkatemail&initial=true";
                    return false;
                });
                
                $(".install.game").click(function(){
                    window.location = "${externalResourcesUrl}";
                    return false;
                }); 

                
            });
            
           
            


            
        </script>


    </head>

    <c:choose><%--  If mobile param was not initialized call js function init  --%>
        <c:when test="${mobile != null}">
            <body>

                <%-- Facebook initialization area --%>
                <div id="fb-root"></div>         
                <script>
                    window.fbAsyncInit = function() {
                        // init the FB JS SDK
                        FB.init({
                            appId      : '${fb_app_id}',                        // App ID from the app dashboard
                            channelUrl : '${friendsSiteUrl}', // Channel file for x-domain comms
                            status     : false,                                 // Check Facebook Login status
                            xfbml      : false                                  // Look for social plugins on the page
                        });

                        // Additional initialization code such as adding Event Listeners goes here
                    };

                    // Load the SDK asynchronously
                    (function(){
                        // If we've already installed the SDK, we're done
                        if (document.getElementById('facebook-jssdk')) {return;}

                        // Get the first script element, which we'll use to find the parent node
                        var firstScriptElement = document.getElementsByTagName('script')[0];

                        // Create a new script element and set its id
                        var facebookJS = document.createElement('script'); 
                        facebookJS.id = 'facebook-jssdk';

                        // Set the new script's source to the source of the Facebook JS SDK
                        facebookJS.src = '//connect.facebook.net/en_US/all.js';

                        // Insert the Facebook JS SDK into the DOM
                        firstScriptElement.parentNode.insertBefore(facebookJS, firstScriptElement);
                    }());
                </script>





                <div id="wrapper">
                    <div id="header">
                        <img src="${externalResourcesUrl}/img/logo.png" alt="Cosmobasters" id="logo" />
                        <c:if test="${displayNavigationBar}">
                            <ul id="mainMenu">
                                <li ><a target="_top" href="${externalResourcesUrl}">Home</a></li>
                                <li><a target="_top" href="${externalResourcesUrl}/players.html">Top players</a></li>
                                <li class="act"><a target="_top" href="${friendsSiteUrl}">My Friends</a></li>
                                <li ><a target="_top" href="${externalResourcesUrl}/contact.html">Contact</a></li>
                            </ul>
                        </c:if>
                    </div>


                    <div id="content">

                        <div class="player-block">
                            <h1 class="name">
                                <c:if test="${(not_in_game_yet == null) && (playerGameData != null)}"><%-- Display flag - user is already in game  --%>
                                    <img class="flag" src="${externalResourcesUrl}/img/flags/64/${fn:toLowerCase(playerGameData.gameProfile.countryCode)}.png" alt="${playerGameData.gameProfile.countryCode}" />
                                </c:if>
                                <c:if test="${(not_in_game_yet != null) && (not_in_game_yet == true)}"><%-- If user NOT logged in - display default flag  --%>
                                    <img class="flag default"  style="height: 55px;" src="${pageContext.request.contextPath}/facebook_app/resources/images/default_flag.jpg" alt="Flag" />
                                </c:if>

                                ${user.name}<c:if test="${playerGameData != null}">(${playerGameData.player.name})</c:if>
                            </h1>


                            <img src="https://graph.facebook.com/${user.id}/picture?width=120&height=120" alt="${user.name}" class="largeUserImg" />


                            <c:if test="${not_in_game_yet}">
                                <table class="player-table">
                                    <tr>
                                        <td>
                                            <button type="submit" class="login game" data-theme="a" data-inline="false" data-mini="true">Log in game</button>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            or
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <button type="submit" class="install game" data-theme="a" data-inline="false" data-mini="true">Install the game</button>
                                        </td>
                                    </tr>                                    
                                </table>                                
                            </c:if>


                            <c:if test="${playerGameData != null}">
                                <table class="player-table">
                                    <tr>
                                        <td>Rank level : </td>
                                        <td><img src="${externalResourcesUrl}/img/ranks/${playerGameData.gameProfile.rank}-small.png" alt="Rank level ${playerGameData.gameProfile.rank}" class="player-ico-rank"></td>
                                    </tr>
                                    <tr>
                                        <td>Ranking Scores : </td>
                                        <td><b>${playerGameData.gameProfile.scores}</b></td>
                                    </tr>
                                    <tr>
                                        <td>To Next Rank : </td>
                                        <td><b>${playerGameData.nextRankProgress}%</b></td>
                                    </tr>
                                    <tr>
                                        <td>Scores (singleplayer) : </td>
                                        <td><b>${playerGameData.levelsProgress.levelsProgressRankingPoints}</b></td>
                                    </tr>
                                </table>
                            </c:if>

                        </div>
                        <div class="end"></div>


                        <c:if test="${playerGameData != null}">
                            <h4 style="text-align: center;">Detailed statistics</h4>
                            <table class="stat">
                                <thead>
                                    <tr>
                                        <th>RANK</th>
                                        <th>Win / Defeat</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>Player RANK I</td>
                                        <td>${playerGameData.gamesStatistics.killedrank[0]} / ${playerGameData.gamesStatistics.killedbyrank[0]}</td>
                                    </tr>
                                    <tr>
                                        <td>Player RANK II</td>
                                        <td>${playerGameData.gamesStatistics.killedrank[1]} / ${playerGameData.gamesStatistics.killedbyrank[1]}</td>
                                    </tr>
                                    <tr>
                                        <td>Player RANK III</td>
                                        <td>${playerGameData.gamesStatistics.killedrank[2]} / ${playerGameData.gamesStatistics.killedbyrank[2]}</td>
                                    </tr>
                                    <tr>
                                        <td>Player RANK IV</td>
                                        <td>${playerGameData.gamesStatistics.killedrank[3]} / ${playerGameData.gamesStatistics.killedbyrank[3]}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </c:if>

                        <br/>




                        <div class="single_player_ranking_list">

                            <c:if test="${playerGameData != null}">
                                <c:if test="${challengeRatingPosition != -1}">
                                    <h2>Your position is # ${challengeRatingPosition}
                                    </h2>
                                </c:if>
                                <c:if test="${challengeRatingPosition == -1}">
                                    <h2>Join game and become a leader !!!
                                    </h2>
                                </c:if>
                            </c:if>



                            <c:if test="${challengeleadersdashboard != null}">

                                <div class="invitation_lbl">Top Players of the week</div>

                                <table class="topList">
                                    <thead>
                                        <tr>
                                            <th class="col_pos">Position</th>
                                            <th class="col_userImg"></th>
                                            <th>Name</th>
                                            <th>Ranking Points</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="challengeDashboardEntry" items="${challengeleadersdashboard}" varStatus="loop">
                                            <tr>
                                                <td>
                                                    ${loop.index +1}						
                                                </td>
                                                <td class="col_userImg">
                                                    <c:choose>
                                                        <c:when test="${challengeDashboardEntry.facebookId != null}">
                                                            <img src="https://graph.facebook.com/${challengeDashboardEntry.facebookId}/picture?width=50&height=50" alt="${user.name}" class="largeUserImg" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src='${externalResourcesUrl}/img/pilot-small.jpg' alt='${challengeDashboardEntry.player.name}' class='ui-li-thumb' />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>                                                    
                                                    <img src='${externalResourcesUrl}/img/flags/16/${fn:toLowerCase(challengeDashboardEntry.gameProfile.countryCode)}.png' alt='${challengeDashboardEntry.gameProfile.countryCode}'/> <a target="_top" href='${externalResourcesUrl}/player.html?name=${challengeDashboardEntry.player.name}'>${challengeDashboardEntry.player.name}</a>                                                    
                                                </td>					
                                                <td>
                                                    ${challengeDashboardEntry.challengeData.scores}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                            </c:if>



                            <c:if test="${announcementText != null}">
                                <br/>
                                <table class="topList">
                                    <thead>
                                        <tr>
                                            <th class="col_pos">Announcement</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td class="active">
                                                ${announcementText} <script type="text/javascript">renderAndLocalizeAnnouncementDate('${announcementDate}');</script>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table> 
                            </c:if>

                        </div>                        



                        <br/>
                        <c:if test="${friendsgame == null}">
                            <div class="invitation_lbl">
                                Invite your friends and play with them online !!!
                            </div>
                        </c:if>
                        <c:if test="${friendsgame != null}">
                            <div class="invitation_lbl">
                                Friends statistics(multiplayer)
                            </div>
                            <table class="topList">
                                <thead>
                                    <tr>
                                        <th class="col_pos">Position</th>
                                        <th class="col_userImg"></th>
                                        <th>Name</th>
                                        <th class="col_userRank">Rank</th>
                                        <th>Scores</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="friendgame" items="${friendsgame}" varStatus="loop">
                                        <tr>
                                            <td>
                                                ${loop.index +1}						
                                            </td>
                                            <td class="col_userImg">
                                                <img src="https://graph.facebook.com/${friendgame.fbUser.id}/picture?width=50&height=50" alt="${friendgame.fbUser.name}" class="userImg" />
                                            </td>
                                            <td>
                                                <c:if test="${friendgame.online}">
                                                    <img class="flag" style="height: 10px;" src="${pageContext.request.contextPath}/facebook_app/resources/images/online_dot_green.png"/>
                                                </c:if>                                                
                                                <img class="flag" src="${externalResourcesUrl}/img/flags/16/${fn:toLowerCase(friendgame.gameData.gameProfile.countryCode)}.png" alt="${friendgame.gameData.gameProfile.countryCode}" /> <a href="${externalResourcesUrl}/player.html?name=${friendgame.gameData.player.name}">${friendgame.fbUser.name} (${friendgame.gameData.player.name})</a>
                                            </td>
                                            <td class="col_userRank">
                                                <img src="${externalResourcesUrl}/img/ranks/${friendgame.gameData.gameProfile.rank}-small.png" alt="RANK ${friendgame.gameData.gameProfile.rank}" />
                                            </td>
                                            <td>${friendgame.gameData.gameProfile.scores}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>

                        <br/>

                        <%-- Facebook friends list with invite button  --%>
                        <table class="topList">  
                            <thead>
                                <tr>
                                    <th class="col_pos"></th>
                                    <th class="col_userImg"></th>
                                    <th></th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="fbfriend" items="${fbfriends}" varStatus="loop">
                                    <tr>
                                        <td>
                                            ${loop.index +1}
                                        </td>
                                        <td>
                                            <img src="https://graph.facebook.com/${fbfriend.id}/picture" alt="${fbfriend.name}" class="userImg"/>
                                        </td>
                                        <td>
                                            <a href="#">${fbfriend.name}</a>
                                        </td>                                
                                        <td class="col_userRank">
                                            <a class="btn btn_invite" id="${fbfriend.id}#${fbfriend.name}" href="?command=invite&friend=${fbfriend.id}">Invite</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>



                </div><%-- <div id="wrapper"> --%>


            </c:when>
            <c:otherwise>
            <body>                
                <h2>&nbsp; Loading Facebook app, please wait a second...</h2>
                <script type="text/javascript">
              
                    var deviceType = detectDeviceType();
                    if(deviceType != 1){
                        window.location.href ="${facebookAppServlet}" + "?mobile=true";
                    }else{
                        window.location.href ="${facebookAppServlet}" + "?mobile=false";
                    }
           


                </script>  
            </c:otherwise>
        </c:choose>                


    </body>
</html>
