<!DOCTYPE html>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>
            Rct/FB mobile app
        </title>

        <jsp:include page="css_js_references.jsp">
            <jsp:param name="baseurl" value="${mobileExternalResourcesUrl}"/>
        </jsp:include>

        <script type="text/javascript">
            $(document).ready(function(){
                $("#feedback").validate();
                
                
                $(".btn_invite").click(function(){
                    var $button = $(this);                    
                    $button.html("Processing...").button("refresh");
 
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
                            $button.html("INVITED!").button("refresh");
                            $button.attr("disabled", "disabled");
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
                            $button.html("Try again !");
                        },
                        success: function(xml){
                            if(xml != null){
                                alert('Your friend was invited \n to join the game !');                                
                                $button.html("INVITED!").button("refresh");
                                $button.attr("disabled", "disabled");
                            }else{
                                alert("No response from the server");
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
                    window.location = "${mobileExternalResourcesUrl}";
                    return false;
                }); 



                
            });
            
            function getContOfTag(data, tagName){
                var elem1 = data.split("<" + tagName + ">")[1];
                var val = elem1.split("</" + tagName + ">")[0];
                return val;
            }
            
        </script>
    </head>

    <body>

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

        <div data-role="page" data-theme="a" id="page3">
            <div data-theme="a" data-role="header">
                <h3>
                    My Friends
                </h3>
            </div>

            <c:if test="${displayNavigationBar}">
                <div data-role="navbar" data-iconpos="left">
                    <ul>
                        <li>
                            <a href="${mobileExternalResourcesUrl}#page1" data-ajax="false" data-theme="" data-icon="">
                                Home
                            </a>
                        </li>
                        <li>
                            <a href="${mobileExternalResourcesUrl}/players.html" data-ajax="false" data-theme="" data-icon="">
                                Players
                            </a>
                        </li>
                        <li>
                            <a href="${friendsSiteUrl}" data-ajax="false" data-theme="" data-icon="" class="ui-btn-active ui-state-persist">
                                Friends
                            </a>
                        </li>
                        <li>
                            <a href="${mobileExternalResourcesUrl}#page4" data-ajax="false" data-theme="" data-icon="">
                                Feedback
                            </a>
                        </li>
                    </ul>
                </div>
                <br />
            </c:if> 

            <div data-role="content" style="padding: 15px 15px 0 15px">

                <div class="ui-bar ui-bar-c player-block ui-corner-all">
                    <div class="playerName-wrapper">
                        <c:if test="${(not_in_game_yet == null) && (playerGameData != null)}"><%-- Display flag - user is already in game  --%>
                            <img class="flag" src="${externalResourcesUrl}/img/flags/48/${fn:toLowerCase(playerGameData.gameProfile.countryCode)}.png" alt="${playerGameData.gameProfile.countryCode}" />
                        </c:if>
                        <c:if test="${(not_in_game_yet != null) && (not_in_game_yet == true)}"><%-- If user NOT logged in - display default flag  --%>
                            <img class="flag default" style="height: 42px;" src="${pageContext.request.contextPath}/facebook_app/resources/images/default_flag.jpg" alt="Flag" />
                        </c:if>
                        <h2 class="playerName">
                            ${user.name}<c:if test="${playerGameData != null}">(${playerGameData.player.name})</c:if>
                            </h2>
                        </div>
                        <img src="https://graph.facebook.com/${user.id}/picture?width=120&height=120" alt="${user.name}" class="avatar"/>

                    <c:if test="${not_in_game_yet}">
                        <div style="padding-left: 130px; text-align: center">
                            <button type="submit" class="login game" data-theme="a" data-inline="false" data-mini="true">Sign in</button>
                            or
                            <button type="submit" class="install game" data-theme="a" data-inline="false" data-mini="true">Install the game</button>
                        </div>
                    </c:if>

                    <c:if test="${playerGameData != null}">
                        <table class="player-table">
                            <tr>
                                <td>Rank level : </td>
                                <td><img src="${mobileExternalResourcesUrl}/img/ranks/${playerGameData.gameProfile.rank}-small.png" alt="Rank level ${playerGameData.gameProfile.rank}" class="player-ico-rank"/></td>
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



                    <br class="clear" />
                </div>
                <br class="clear" />

                <c:if test="${playerGameData != null}">
                    <div data-role="collapsible" data-content-theme="c" style="padding:0 15px">
                        <h3>Battles statistics</h3>
                        <ul data-role="listview" data-divider-theme="b" data-inset="true">
                            <li data-theme="c">
                                <table class="stat">
                                    <thead>
                                        <tr>
                                            <th>Player RANK</th>
                                            <th>Win / Defeat</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>I</td>
                                            <td>${playerGameData.gamesStatistics.killedrank[0]} / ${playerGameData.gamesStatistics.killedbyrank[0]}</td>
                                        </tr>
                                        <tr>
                                            <td>II</td>
                                            <td>${playerGameData.gamesStatistics.killedrank[1]} / ${playerGameData.gamesStatistics.killedbyrank[1]}</td>
                                        </tr>
                                        <tr>
                                            <td>III</td>
                                            <td>${playerGameData.gamesStatistics.killedrank[2]} / ${playerGameData.gamesStatistics.killedbyrank[2]}</td>
                                        </tr>
                                        <tr>
                                            <td>IV</td>
                                            <td>${playerGameData.gamesStatistics.killedrank[3]} / ${playerGameData.gamesStatistics.killedbyrank[3]}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </li>
                        </ul>
                    </div>
                </c:if>
                <br class="clear" />
            </div>




            <div data-role="content" style="padding:0 15px 15px 15px">

                <ul data-role="listview" data-divider-theme="b" data-inset="true" id="playersList">
                    <li data-role="list-divider" role="heading">
                        Friends statistics(multiplayer)
                    </li>
                    <c:if test="${friendsgame == null}">                              
                        <li data-theme='c'>
                            Invite your friends and play with them online !!!
                        </li>
                    </c:if>
                    <c:if test="${friendsgame != null}">                              
                        <c:forEach var="friendgame" items="${friendsgame}" varStatus="loop">
                            <li data-theme='c'>
                                <a href='${mobileExternalResourcesUrl}/players.html?name=${friendgame.gameData.player.name}' data-ajax="false" data-transition='slide'><%-- to call plugin method use command <a href='?command=viewgamestat&friend=${friendgame.fbUser.id}'> FriendName</a> --%>
                                    <img src='https://graph.facebook.com/${friendgame.fbUser.id}/picture?width=120&height=120' alt='${friendgame.fbUser.name}' class='ui-li-thumb' />
                                    <h3 class='ui-li-heading'>${friendgame.fbUser.name} (${friendgame.gameData.player.name})</h3>
                                    <p class='ui-li-desc'>
                                        <c:if test="${friendgame.online}">
                                            <img class="flag" style="height: 10px;" src="${pageContext.request.contextPath}/facebook_app/resources/images/online_dot_green.png"/>
                                        </c:if>
                                        <img src='${mobileExternalResourcesUrl}/img/flags/16/${fn:toLowerCase(friendgame.gameData.gameProfile.countryCode)}.png' alt='${friendgame.gameData.gameProfile.countryCode}' class='ui-li-flag'/>
                                        <img src='${mobileExternalResourcesUrl}/img/ranks/${friendgame.gameData.gameProfile.rank}-small.png' class='ui-li-rank' alt='RANK ${friendgame.gameData.gameProfile.rank}'/></p>
                                    <span class='ui-li-count'>
                                        ${friendgame.gameData.gameProfile.scores}
                                    </span>
                                </a>     
                            </li>
                        </c:forEach>
                    </c:if>
                </ul>





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
                        <ul data-role="listview" data-divider-theme="b" data-inset="true">
                            <li data-role="list-divider" role="heading">
                                Week Top Players
                            </li>
                            <c:forEach var="challengeDashboardEntry" items="${challengeleadersdashboard}" varStatus="loop">
                                <li data-theme='c'>
                                    <a href='${mobileExternalResourcesUrl}/player.html?name=${challengeDashboardEntry.player.name}' data-transition='slide'>

                                        <c:choose>
                                            <c:when test="${challengeDashboardEntry.facebookId != null}">
                                                <img src="https://graph.facebook.com/${challengeDashboardEntry.facebookId}/picture?width=120&height=120" alt="${user.name}" class="largeUserImg" />
                                            </c:when>
                                            <c:otherwise>
                                                <img src='${mobileExternalResourcesUrl}/img/pilot.jpg' alt='${challengeDashboardEntry.player.name}' class='ui-li-thumb' />
                                            </c:otherwise>
                                        </c:choose>

                                        <h3 class='ui-li-heading'>${challengeDashboardEntry.player.name}</h3>
                                        <p class='ui-li-desc'><img src='${mobileExternalResourcesUrl}/img/flags/16/${fn:toLowerCase(challengeDashboardEntry.gameProfile.countryCode)}.png' alt='${challengeDashboardEntry.gameProfile.countryCode}' class='ui-li-flag'> <img src='${mobileExternalResourcesUrl}/img/ranks/${challengeDashboardEntry.gameProfile.rank}-small.png' class='ui-li-rank' alt='RANK ${challengeDashboardEntry.gameProfile.rank}'></p>
                                        <span class='ui-li-count'>
                                            ${challengeDashboardEntry.challengeData.scores}
                                        </span>
                                    </a>
                                </li>
                            </c:forEach>                            
                        </ul>
                    </c:if>


                    <c:if test="${announcementText != null}">
                        <ul data-role="listview" data-divider-theme="c" data-inset="true">
                            <li data-role="list-divider" role="heading">
                                Announcement
                            </li>                            
                            <li data-theme="c">
                                ${announcementText} <script type="text/javascript">renderAndLocalizeAnnouncementDate('${announcementDate}');</script>
                            </li>                                                      
                        </ul>
                    </c:if>


                </div>






                <ul data-role="listview" data-divider-theme="b" data-inset="true">
                    <li data-role="list-divider" role="heading">
                        Invite friends
                    </li>

                    <c:forEach var="fbfriend" items="${fbfriends}" varStatus="loop">
                        <li data-theme="c">
                            <img src='https://graph.facebook.com/${fbfriend.id}/picture?width=120&height=120' alt='${fbfriend.name}' class='ui-li-thumb' />
                            <b>${fbfriend.name}</b>
                            <button type="submit" class="btn_invite" id="${fbfriend.id}#${fbfriend.name}" data-theme="a" data-inline="false" data-mini="true">INVITE</button>
                        </li>                                      
                    </c:forEach>                    
                </ul>
                <div>
                    <p style="text-align: center; ">
                        <b>
                            2013-2015
                        </b>
                    </p>
                </div>
            </div>
        </div>




    </body>

</html>