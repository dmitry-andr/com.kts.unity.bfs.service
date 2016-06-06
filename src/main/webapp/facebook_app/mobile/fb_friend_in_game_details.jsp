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
            FB Friend in game details
        </title>
        <link rel="stylesheet" href="https://ajax.aspnetcdn.com/ajax/jquery.mobile/1.1.1/jquery.mobile-1.1.1.min.css"/>
        <link rel="stylesheet" href="${mobileExternalResourcesUrl}/my.css" />
        <link rel="stylesheet" href="${mobileExternalResourcesUrl}/css/style.css" />

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="https://ajax.aspnetcdn.com/ajax/jquery.mobile/1.1.1/jquery.mobile-1.1.1.min.js"></script>
        <script src="${mobileExternalResourcesUrl}/jquery.mobile.lazyloader.min.js"></script>
        <script src="${mobileExternalResourcesUrl}/my.js"></script>
        <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#feedback").validate();
            });
        </script>
    </head>

    <body>
        <!-- Home -->
        <div data-role="page" data-theme="a" id="page1">
            <div data-theme="a" data-role="header">
                <h3>
                    Battlefield space
                </h3>
                <a data-role="button" data-direction="reverse" data-rel="back" data-transition="fade"
                   data-theme="a" href="#page2" class="ui-btn-left" data-icon="back" data-iconpos="left">
                    Back
                </a>
            </div>


            <c:if test="${friendGameData == null}">
                <h3>Oops, requested player was no found...</h3>
                <h3>Please drop us a line and we will take a look at the issue : admin@extline.com</h3>
                <h3>Sorry for inconvenience.</h3>
            </c:if>

            <c:if test="${friendGameData != null}">                
                <div data-role="content" style="padding: 15px">
                    <div class="ui-bar ui-bar-c player-block ui-corner-all">
                        <div class="playerName-wrapper">
                            <img src="${mobileExternalResourcesUrl}/img/flags/48/${fn:toLowerCase(friendGameData.gameProfile.countryCode)}.png" alt="${friendGameData.gameProfile.countryCode}"/>
                            <h2 class="playerName">
                                ${friendGameData.player.name}
                            </h2>
                        </div>
                        <div>
                            <img src="https://graph.facebook.com/${friendGameData.facebookId}/picture?width=120&height=120" alt="BFS Player" class="avatar"/>

                            <table class="player-table">
                                <tr>
                                    <td>Rank level:</td>
                                    <td><img src="${mobileExternalResourcesUrl}/img/ranks/${friendGameData.gameProfile.rank}-small.png" alt="Rank level ${friendGameData.gameProfile.rank}" class="player-ico-rank"/></td>
                                </tr>
                                <tr>
                                    <td>Scores:</td>
                                    <td><b>${friendGameData.gameProfile.scores}</b></td>
                                </tr>
                                <tr>
                                    <td>Next rank:</td>
                                    <td><b>67 %?</b></td>
                                </tr>
                            </table>

                        </div>
                        <br class="clear" />
                    </div>                
                    <br class="clear" />
                    <ul data-role="listview" data-divider-theme="b" data-inset="true">
                        <li data-role="list-divider" role="heading" style="text-align: center; ">
                            Battles statistics 
                        </li>
                        <li data-theme="c">
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
                                        <td>${friendGameData.gamesStatistics.killedrank[0]} / ${friendGameData.gamesStatistics.killedbyrank[0]}</td>
                                    </tr>
                                    <tr>
                                        <td>Player RANK II</td>
                                        <td>${friendGameData.gamesStatistics.killedrank[1]} / ${friendGameData.gamesStatistics.killedbyrank[1]}</td>
                                    </tr>
                                    <tr>
                                        <td>Player RANK III</td>
                                        <td>${friendGameData.gamesStatistics.killedrank[2]} / ${friendGameData.gamesStatistics.killedbyrank[2]}</td>
                                    </tr>
                                    <tr>
                                        <td>Player RANK IV</td>
                                        <td>${friendGameData.gamesStatistics.killedrank[3]} / ${friendGameData.gamesStatistics.killedbyrank[3]}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </li>
                    </ul>


                    <div>
                        <p style="text-align: center; ">
                            <b>
                                2012
                            </b>
                        </p>
                    </div>
                </div><%-- End of content page    --%>
            </c:if>


        </div>
        <script>
            //App custom javascript
        </script>
    </body>

</html>