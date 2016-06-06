<%@include file="jsp_header.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Gamification admin page
        </title>

        <%@include file="styles_scripts.jsp" %>

        <link href="${pageContext.request.contextPath}/css/table_style.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.tablesorter.js"></script>

        <script type="text/javascript">

            $(document).ready(function(){
                
                $("#tablesorter-demo").tablesorter();
                
                $(".actionbutton").click(function(){
                    if($(this).attr('title') == 'Deactivate'){
                        var answer = confirm("You selected to deactivate challenge.\nAll current challenge data will be deleted.\nPlease confirm.");
                        if (answer){
                            return true;
                        }
                        else{
                            return false;
                        }
                    }else{
                        return true;
                    }                    
                });
                
                
                
            });    
        </script>


    </head>
    <body>

        <%@include file="header.jsp" %>

        <h4>
            <s:property value="message" />
        </h4>

        <h4>Gamification admin page</h4>

        <hr/>

        Current server time and date : ${currentServerTimeAndDate}
        <hr/>

        <c:if test="${activeChallengeInGame == null}">
            <h4 style="background-color:red;">There are no challenges activated for usage in game !!!</h4>
        </c:if>
        <c:if test="${activeChallengeInGame != null}">
            <p style="background-color:greenyellow;">Challenge activated in game: ${activeChallengeInGame}</p>
        </c:if>

        <form action="gamification_update_active_challenge_in_game.action" method="post">            
            <select name ="selectedChallenge">
                <c:forEach var="entry" items="${challenges}" varStatus="loop">
                    <option <c:if test="${activeChallengeInGame == entry.id}">selected</c:if> value="${entry.id}">${entry.title}</option>
                </c:forEach>
            </select>
            <input type="submit" value="Set active in game"/>
        </form>
        <hr/>


        <c:if test="${tournamentEndTmistampMillis != null}">
            <img style="height: 12px;" src="${pageContext.request.contextPath}/facebook_app/resources/images/online_dot_green.png"/>
            Remains ${tournamentDaysRemain} day(s), ${tournamentHoursRemain} hour(s)
        </c:if>
        <br/>

        <form action="gamification_update_countdownsettings_for_tournament.action" method="post">
            <input type="checkbox" name="activeStatusTimerStrVal" <c:if test="${activeStatusTimer}">checked</c:if>/>
            Tournament end date <input type="date" value="${tournamentTimerEndDate}" name="endDateStrVal"/> and time
            <select name ="selectedEndTime">
                <c:forEach var="hoursentry" items="${timePickerHoursEntries}" varStatus="loop">
                    <option <c:if test="${tournamentTimerEndTime == hoursentry}">selected</c:if> value="${hoursentry}">${hoursentry}</option>
                </c:forEach>
            </select> : 00
            <input type="submit" value="Save"/>
        </form>

        <hr/>


        <c:if test="${weekEndTimeStampMillis != null}">
            <img style="height: 12px;" src="${pageContext.request.contextPath}/facebook_app/resources/images/online_dot_green.png"/>
            Remains ${weekChallengeDaysRemain} day(s), ${weekChallengeHoursRemain} hour(s)
        </c:if>
        <br/>
        <form action="gamification_update_finish_date_time_for_week_tournament.action" method="post">
            <input type="checkbox" name="activeStatusTimerStrVal" <c:if test="${activeWeekStatusTimer}">checked</c:if>/>
            Weekly challenge next award date <input type="date" value="${weekTimerEndDate}" name="endDateStrVal"/> and time
            <select name ="selectedEndTime">
                <c:forEach var="hoursentry" items="${timePickerHoursEntries}" varStatus="loop">
                    <option <c:if test="${weekTimerEndTime == hoursentry}">selected</c:if> value="${hoursentry}">${hoursentry}</option>
                </c:forEach>
            </select> : 00; 
            Repeat <select name ="selectedNumOfWeeksToRepeat">
                <c:forEach var="weekscounter" items="${weeksCounterPickerEntries}" varStatus="loop">
                    <option <c:if test="${weeksDurationCounter == weekscounter}">selected</c:if> value="${weekscounter}">${weekscounter}</option>
                </c:forEach>
            </select> times
            <input type="submit" value="Save"/>
        </form>

        <hr/>

        
        
        
        
        
        <c:if test="${dayEndDateTimeStampMillis != null}">
            <img style="height: 12px;" src="${pageContext.request.contextPath}/facebook_app/resources/images/online_dot_green.png"/>
            Remains ${dayChallengeDaysRemain} day(s), ${dayChallengeHoursRemain} hour(s)
        </c:if>
        <form action="gamification_update_finish_date_time_for_day_tournament.action" method="post">
            <input type="checkbox" name="activeStatusTimerStrVal" <c:if test="${activeDayStatusTimer}">checked</c:if>/>
            Daily challenge next award date <input type="date" value="${dayNextAwardingDate}" name="dayTimerNextDateDateStrVal"/> and time
            <select name ="selectedEndTime">
                <c:forEach var="hoursentry" items="${timePickerHoursEntries}" varStatus="loop">
                    <option <c:if test="${dayTimerEndTime == hoursentry}">selected</c:if> value="${hoursentry}">${hoursentry}</option>
                </c:forEach>
            </select> : 00;
            Repeat every <select name ="selectedNumOfDaysToDelay">
                <c:forEach var="dayscounter" items="${daysToDelayDailyRankingPickerEntries}" varStatus="loop">
                    <option <c:if test="${dayDaysDelayPeriod == dayscounter}">selected</c:if> value="${dayscounter}">${dayscounter}</option>
                </c:forEach> 
            </select> day(s),
            till <input type="date" value="${dayFinishDate}" name="finishDateStrVal"/>
            <input type="submit" value="Save"/>
        </form>

            
            
            
            
            
            
            
            


        <table id="tablesorter-demo" class="tablesorter" border="1">
            <thead>
                <tr>
                    <th>No</th>
                    <th>Challenge ID</th>
                    <th>Title</th>
                    <th>Intro Text</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>

            <c:forEach var="entry" items="${challenges}" begin="1" varStatus="loop">                
                <tr>
                    <td>${loop.index}</td>
                    <td>${entry.id}</td>
                    <td>${entry.title}</td>
                    <td>${entry.introText}</td>
                    <td>
                        <c:if test="${entry.isActive == 0}">
                            <img class="status_tick_icon" src="${pageContext.request.contextPath}/images/tick_not_ok.jpg"/> Inactive
                        </c:if>
                        <c:if test="${entry.isActive != 0}">
                            <img class="status_tick_icon" src="${pageContext.request.contextPath}/images/tick_ok.jpg"/> Active
                        </c:if>
                    </td>

                    <td>
                        <form name="input" action="gamification_update_active_status.action" method="post">
                            <input type="hidden" name="challengeId" value="${entry.id}"/>
                            <input type="submit" class="actionbutton" title="<c:if test='${entry.isActive != 0}'>Deactivate</c:if>"
                                   value="<c:if test='${entry.isActive == 0}'>Activate</c:if><c:if test='${entry.isActive != 0}'>Deactivate</c:if>"/>
                            </form>
                        </td>
                    </tr>
            </c:forEach>            


        </table>


    </body>
</html>
