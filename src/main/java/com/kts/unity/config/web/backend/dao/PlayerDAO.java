package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.gamification.challenges.weektopplayers.WeekRankingTable;
import com.kts.unity.config.gamification.common.ScoresRankingEntry;
import com.kts.unity.config.web.backend.utils.GameUsageStatisticsTable;
import com.kts.unity.config.web.backend.utils.LevelsProgressTable;
import com.kts.unity.config.web.backend.utils.Settings;
import com.kts.unity.config.web.backend.utils.player.PlayerGameProfileTable;
import com.kts.unity.config.web.entities.player.Player;
import com.kts.unity.config.web.backend.utils.player.PlayerTable;
import com.kts.unity.config.web.backend.utils.player.PlayersOnlineTable;
import com.kts.unity.config.web.entities.LevelsProgress;
import com.kts.unity.config.web.entities.player.PlayerGameProfile;
import com.kts.unity.config.web.entities.player.PlayerOnlineData;
import com.kts.unity.config.web.entities.player.PlayerWithGameProfAndStatisticsAndChallengeData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class PlayerDAO extends SimpleJdbcDaoSupport {

    public int createPlayer(Player player) throws Exception {
        int status = 0;
        String playerInsertQryStr = "INSERT INTO " + PlayerTable.TBL_NAME + "(" + PlayerTable.NAME + "," + PlayerTable.PASSWORD + "," + PlayerTable.EMAIL + ") VALUES(?,?,?)";

        getSimpleJdbcTemplate().update(
                playerInsertQryStr,
                player.getName(),
                player.getPassword(),
                player.getEmail());
        status = 1;

        return status;
    }

    public Player getPlayerByName(String playerName) {

        String sql = "SELECT * FROM " + PlayerTable.TBL_NAME + " WHERE " + PlayerTable.NAME + " = ?";

        Player player = null;
        try {
            player = (Player) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerName}, new PlayerDAO.PlayerRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            //add logging here
        }

        return player;
    }

    public Player getPlayerById(int playerId) throws Exception {

        String sql = "SELECT * FROM " + PlayerTable.TBL_NAME + " WHERE " + PlayerTable.ID + " = ?";

        Player player = null;
        try {
            player = (Player) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new PlayerDAO.PlayerRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            //add logging here
        }

        return player;
    }

    public int deletePlayer(int playerId) {
        int status = 0;

        String sql = "DELETE FROM " + PlayerTable.TBL_NAME + " WHERE " + PlayerTable.ID + " = ?";
        getSimpleJdbcTemplate().update(sql, playerId);
        status = 1;

        return status;
    }

    public int updatePlayer(Player player) throws Exception {
        int status = 0;
        String playerUpdateQryStr = "UPDATE " + PlayerTable.TBL_NAME + " SET "
                + PlayerTable.NAME + " = ? ,"
                + PlayerTable.PASSWORD + " = ? ,"
                + PlayerTable.EMAIL + " = ? "
                + " WHERE " + PlayerTable.ID + " = ?";

        getSimpleJdbcTemplate().update(
                playerUpdateQryStr,
                player.getName(),
                player.getPassword(),
                player.getEmail(),
                player.getId());
        status = 1;

        return status;
    }

    public int createPlayerOnlineRecord(PlayerOnlineData onlineData) {
        int status = 0;
        String playerOnlineInsertQryStr = "INSERT INTO " + PlayersOnlineTable.TBL_NAME + "(" + PlayersOnlineTable.PLAYER_ID + "," + PlayersOnlineTable.ACTIVITY_TIME_MILLIS + ") VALUES(?,?)";

        getSimpleJdbcTemplate().update(
                playerOnlineInsertQryStr,
                onlineData.getPlayerId(),
                onlineData.getLastActivityTime());
        status = 1;

        return status;
    }

    public boolean isPlayerOnline(int playerId) {

        String sql = "SELECT COUNT(" + PlayersOnlineTable.PLAYER_ID + ") FROM " + PlayersOnlineTable.TBL_NAME
                + " WHERE " + PlayersOnlineTable.TBL_NAME + "." + PlayersOnlineTable.PLAYER_ID + " = " + playerId;

        int numOfPlayerRecords = this.getJdbcTemplate().queryForInt(sql);

        if (numOfPlayerRecords > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int updatePlayerOnlineData(PlayerOnlineData onlineData) {
        int status = 0;
        String playerOnlineStatusUpdateQryStr = "UPDATE " + PlayersOnlineTable.TBL_NAME + " SET "
                + PlayersOnlineTable.ACTIVITY_TIME_MILLIS + " = ? "
                + " WHERE " + PlayersOnlineTable.PLAYER_ID + " = ?";

        getSimpleJdbcTemplate().update(
                playerOnlineStatusUpdateQryStr,
                onlineData.getLastActivityTime(),
                onlineData.getPlayerId());
        status = 1;

        return status;
    }

    public Map<Integer, PlayerOnlineData> getPlayersOnline() {

        String sql = "SELECT " + PlayersOnlineTable.TBL_NAME + ".* FROM " + PlayersOnlineTable.TBL_NAME;

        List<Map<String, Object>> playersOnlineDBData = this.getJdbcTemplate().queryForList(sql);

        if (playersOnlineDBData != null) {
            Map<Integer, PlayerOnlineData> playersOnlineData = new HashMap<Integer, PlayerOnlineData>();
            for (Map row : playersOnlineDBData) {
                PlayerOnlineData tmpData = new PlayerOnlineData();
                tmpData.setPlayerId((Integer) row.get(PlayersOnlineTable.PLAYER_ID));
                tmpData.setLastActivityTime((Long) row.get(PlayersOnlineTable.ACTIVITY_TIME_MILLIS));

                long currentTimeMillis = (new Date()).getTime();
                if ((currentTimeMillis - tmpData.getLastActivityTime()) < Settings.getTimeToSeePlayerOnline()) {
                    playersOnlineData.put(tmpData.getPlayerId(), tmpData);
                } else {//online status is obsolete - delete it
                    String sqlDelete = "DELETE FROM " + PlayersOnlineTable.TBL_NAME + " WHERE " + PlayersOnlineTable.PLAYER_ID + " = ?";
                    getSimpleJdbcTemplate().update(sqlDelete, tmpData.getPlayerId());
                }
            }

            return playersOnlineData;
        } else {
            return null;
        }
    }

    public int createPlayerLevelProgressRecord(LevelsProgress levelProgressData) {
        int status = 0;
        String createQry = "INSERT INTO " + LevelsProgressTable.TBL_NAME + "(" + LevelsProgressTable.PLAYER_ID + "," + LevelsProgressTable.LEVELS_PROGRESS_DATA + "," + LevelsProgressTable.SINGLE_PLAYER_RANKING_POINTS + ") VALUES(?,?,?)";
        getSimpleJdbcTemplate().update(
                createQry,
                levelProgressData.getPlayerId(),
                levelProgressData.getLevelsProgressData(),
                levelProgressData.getLevelsProgressRankingPoints());
        status = 1;

        return status;
    }

    /*sample query to get line number of the row
     SELECT id FROM (SELECT *, @rownum:=@rownum + 1 AS id FROM levelprogress, (SELECT @rownum:=0) r ORDER BY single_player_ranking_points DESC) d WHERE d.playerid=20;
     */
    public int getPlayerPositionBySinglePlayerRanking(int playerId) {
        String sql = "SELECT id FROM (SELECT *, @rownum:=@rownum + 1 AS id FROM " + LevelsProgressTable.TBL_NAME + ", (SELECT @rownum:=0) r ORDER BY " + LevelsProgressTable.SINGLE_PLAYER_RANKING_POINTS + " DESC) d WHERE d." + LevelsProgressTable.PLAYER_ID + "=" + playerId;

        //System.out.println("SQL getPlayerPositionBySinglePlayerRanking : " + sql);
        int playerPosition = -1;
        try {
            playerPosition = this.getJdbcTemplate().queryForInt(sql);
        } catch (IncorrectResultSizeDataAccessException ex) {
            playerPosition = -1;
        }

        return playerPosition;
    }

    public int deletePlayerLevelProgressRecord(int playerId) {
        int status = 0;

        String sql = "DELETE FROM " + LevelsProgressTable.TBL_NAME + " WHERE " + LevelsProgressTable.PLAYER_ID + " = ?";
        getSimpleJdbcTemplate().update(sql, playerId);
        status = 1;

        return status;
    }

    public int updatePlayerLeveProgressRecord(LevelsProgress levelProgressData) throws Exception {
        int status = 0;
        String playerLevelStatUpdateQryStr = "UPDATE " + LevelsProgressTable.TBL_NAME + " SET "
                + LevelsProgressTable.LEVELS_PROGRESS_DATA + " = ? ,"
                + LevelsProgressTable.SINGLE_PLAYER_RANKING_POINTS + " = ? "
                + " WHERE " + LevelsProgressTable.PLAYER_ID + " = ?";

        getSimpleJdbcTemplate().update(
                playerLevelStatUpdateQryStr,
                levelProgressData.getLevelsProgressData(),
                levelProgressData.getLevelsProgressRankingPoints(),
                levelProgressData.getPlayerId());
        status = 1;

        return status;
    }

    public LevelsProgress getPlayerLevelProgress(int playerId) throws Exception {

        String sql = "SELECT * FROM " + LevelsProgressTable.TBL_NAME + " WHERE " + LevelsProgressTable.PLAYER_ID + " = ?";

        LevelsProgress levelsData = null;
        try {
            levelsData = (LevelsProgress) this.getJdbcTemplate().queryForObject(sql, new Object[]{playerId}, new PlayerDAO.LevelProgressDataRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            //add logging here
        }

        return levelsData;
    }

    public Map<Integer, LevelsProgress> getLevelsProgressForPlayersList(ArrayList<Integer> playersIds) {
        StringBuilder sqlWhere = new StringBuilder();
        for (int k = 0; k < playersIds.size(); k++) {
            sqlWhere.append(playersIds.get(k));
            if (k != (playersIds.size() - 1)) {
                sqlWhere.append(" OR ");
            }
        }

        String sql = "SELECT " + LevelsProgressTable.TBL_NAME + ".* FROM " + LevelsProgressTable.TBL_NAME + " WHERE " + sqlWhere;

        List<Map<String, Object>> playersLevelsData = this.getJdbcTemplate().queryForList(sql);
        if (playersLevelsData != null) {
            Map<Integer, LevelsProgress> playersDataList = new HashMap<Integer, LevelsProgress>();
            for (Map row : playersLevelsData) {
                LevelsProgress tmpData = new LevelsProgress();
                tmpData.setPlayerId((Integer) row.get(LevelsProgressTable.PLAYER_ID));
                tmpData.setLevelsProgressData((String) row.get(LevelsProgressTable.LEVELS_PROGRESS_DATA));
                tmpData.setLevelsProgressRankingPoints((Integer) row.get(LevelsProgressTable.SINGLE_PLAYER_RANKING_POINTS));

                playersDataList.put(tmpData.getPlayerId(), tmpData);
            }
            return playersDataList;
        } else {
            return null;
        }
    }

    public int getNumberOfPlayersInSystem() {
        String sql = "SELECT COUNT(" + PlayerTable.TBL_NAME + "." + PlayerTable.ID + ") FROM " + PlayerTable.TBL_NAME;
        int numberOfPlayers = -1;
        try {
            numberOfPlayers = this.getJdbcTemplate().queryForInt(sql);
        } catch (IncorrectResultSizeDataAccessException ex) {
            numberOfPlayers = -1;
        }

        return numberOfPlayers;
    }

    public ArrayList<Integer> playerIdsSortedByMultiplayerScores() {
        /*
         SELECT DISTINCT player.id, player.name, player_game_profile.scores
         FROM player
         LEFT JOIN player_game_profile ON player.id = player_game_profile.profile_id
         RIGHT JOIN game_usage_stat ON player.name=game_usage_stat.player_name
         ORDER BY player_game_profile.scores DESC        
         */

        String sql = "SELECT DISTINCT " + PlayerTable.TBL_NAME + "." + PlayerTable.ID
                + " FROM " + PlayerTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " RIGHT JOIN " + GameUsageStatisticsTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.NAME + " = " + GameUsageStatisticsTable.TBL_NAME + "." + GameUsageStatisticsTable.PLAYER_NAME
                + " ORDER BY " + PlayerGameProfileTable.SCORES + " DESC";

        List<Map<String, Object>> playerIds = this.getJdbcTemplate().queryForList(sql);

        if (playerIds != null) {
            ArrayList<Integer> playerIdsList = new ArrayList<Integer>();
            for (Map row : playerIds) {
                playerIdsList.add((Integer) row.get(PlayerTable.ID));
            }
            return playerIdsList;
        } else {
            return null;
        }
    }

    public ArrayList<Integer> getPlayerIdsSortedByPointsInSinglePlayerMode() {

        String sql = "SELECT DISTINCT " + PlayerTable.TBL_NAME + "." + PlayerTable.ID
                + " FROM " + PlayerTable.TBL_NAME
                + " LEFT JOIN " + LevelsProgressTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + LevelsProgressTable.TBL_NAME + "." + LevelsProgressTable.PLAYER_ID
                + " RIGHT JOIN " + GameUsageStatisticsTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.NAME + " = " + GameUsageStatisticsTable.TBL_NAME + "." + GameUsageStatisticsTable.PLAYER_NAME
                + " ORDER BY " + LevelsProgressTable.SINGLE_PLAYER_RANKING_POINTS + " DESC";

        List<Map<String, Object>> playerIds = this.getJdbcTemplate().queryForList(sql);

        if (playerIds != null) {
            ArrayList<Integer> playerIdsList = new ArrayList<Integer>();
            for (Map row : playerIds) {
                playerIdsList.add((Integer) row.get(PlayerTable.ID));
            }
            return playerIdsList;
        } else {
            return null;
        }
    }

    public ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> getPlayersWithGameProfile(int maxNumberOfRecords) {
        /*
         SELECT player.name, player.id, player_game_profile.number_of_shop_visits, levelprogress.levelsprogress
         FROM player
         LEFT JOIN player_game_profile ON player.id = player_game_profile.profile_id
         LEFT JOIN levelprogress ON player.id = levelprogress.playerid
         */

        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*, " + LevelsProgressTable.TBL_NAME + "." + LevelsProgressTable.LEVELS_PROGRESS_DATA
                + " FROM " + PlayerTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + "=" + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " LEFT JOIN " + LevelsProgressTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + "=" + LevelsProgressTable.PLAYER_ID
                + " ORDER BY " + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.EXPERIENCE_POINTS + " DESC"
                + " LIMIT " + maxNumberOfRecords;

        List<Map<String, Object>> playersData = this.getJdbcTemplate().queryForList(sql);

        if (playersData != null) {
            ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersDataList = new ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData>();
            for (Map row : playersData) {
                PlayerWithGameProfAndStatisticsAndChallengeData tmpData = new PlayerWithGameProfAndStatisticsAndChallengeData();

                Player playerTmpData = new Player();
                playerTmpData.setId((Integer) row.get(PlayerTable.ID));
                playerTmpData.setName((String) row.get(PlayerTable.NAME));
                tmpData.setPlayer(playerTmpData);

                PlayerGameProfile playerTmpGameProfile = new PlayerGameProfile();
                playerTmpGameProfile.setScores((Integer) row.get(PlayerGameProfileTable.SCORES));
                playerTmpGameProfile.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                playerTmpGameProfile.setCountryCode((String) row.get(PlayerGameProfileTable.COUNTRY_CODE));
                //playerTmpGameProfile.setNumberOfShopVisits((Integer) row.get(PlayerGameProfileTable.NUMBER_OF_SHOP_VISITS));
                playerTmpGameProfile.setExperiencePoints((Integer) row.get(PlayerGameProfileTable.EXPERIENCE_POINTS));
                playerTmpGameProfile.setAchievementsList((String) row.get(PlayerGameProfileTable.ACHIEVEMENTS));
                playerTmpGameProfile.setCoinsBalance((Integer) row.get(PlayerGameProfileTable.COINS_BALANCE));
                tmpData.setGameProfile(playerTmpGameProfile);

                LevelsProgress levelsProgress = new LevelsProgress();
                levelsProgress.setLevelsProgressData((String) row.get(LevelsProgressTable.LEVELS_PROGRESS_DATA));
                tmpData.setLevelsProgress(levelsProgress);

                playersDataList.add(tmpData);
            }
            return playersDataList;

        } else {
            return null;
        }
    }

    public ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> getPlayersWithGameProfAndStatisticsSortedByScores(ArrayList<Integer> playersIds) {

        StringBuilder sqlInValues = new StringBuilder();

        sqlInValues.append("(");
        for (int k = 0; k < playersIds.size(); k++) {
            sqlInValues.append(playersIds.get(k));
            if (k != (playersIds.size() - 1)) {
                sqlInValues.append(",");
            }
        }
        sqlInValues.append(")");

        /*        
         SELECT player.*, player_game_profile.* 
         FROM player 
         LEFT JOIN player_game_profile ON player.id = player_game_profile.profile_id
         WHERE player.id IN (184,521,342,20,171,421,381,515,164,192,324,299,394,293,219,540,232,449,258,295,276,236,385,400,204,168,288,606,470,568,250,466,554,300,334)
         ORDER BY player_game_profile.scores DESC
         */
        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*"
                + " FROM " + PlayerTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " WHERE " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " IN " + sqlInValues.toString()
                + " ORDER BY " + PlayerGameProfileTable.SCORES + " DESC";

        List<Map<String, Object>> playersData = this.getJdbcTemplate().queryForList(sql);

        if (playersData != null) {
            ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersDataList = new ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData>();
            for (Map row : playersData) {
                PlayerWithGameProfAndStatisticsAndChallengeData tmpData = new PlayerWithGameProfAndStatisticsAndChallengeData();

                Player playerTmpData = new Player();
                playerTmpData.setId((Integer) row.get(PlayerTable.ID));
                playerTmpData.setName((String) row.get(PlayerTable.NAME));
                tmpData.setPlayer(playerTmpData);

                PlayerGameProfile playerTmpGameProfile = new PlayerGameProfile();
                playerTmpGameProfile.setScores((Integer) row.get(PlayerGameProfileTable.SCORES));
                playerTmpGameProfile.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                playerTmpGameProfile.setCountryCode((String) row.get(PlayerGameProfileTable.COUNTRY_CODE));
                //playerTmpGameProfile.setNumberOfShopVisits((Integer) row.get(PlayerGameProfileTable.NUMBER_OF_SHOP_VISITS));
                playerTmpGameProfile.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                tmpData.setGameProfile(playerTmpGameProfile);

                playersDataList.add(tmpData);
            }
            return playersDataList;

        } else {
            return null;
        }
    }

    public ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> getPlayersWithGameProfAndStatisticsSortedBySinglePlayerPoints(ArrayList<Integer> playersIds) {

        StringBuilder sqlInValues = new StringBuilder();

        sqlInValues.append("(");
        for (int k = 0; k < playersIds.size(); k++) {
            sqlInValues.append(playersIds.get(k));
            if (k != (playersIds.size() - 1)) {
                sqlInValues.append(",");
            }
        }
        sqlInValues.append(")");

        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*, " + LevelsProgressTable.TBL_NAME + ".*"
                + " FROM " + PlayerTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " LEFT JOIN " + LevelsProgressTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + LevelsProgressTable.TBL_NAME + "." + LevelsProgressTable.PLAYER_ID
                + " WHERE " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " IN " + sqlInValues.toString()
                + " ORDER BY " + LevelsProgressTable.SINGLE_PLAYER_RANKING_POINTS + " DESC";

        List<Map<String, Object>> playersData = this.getJdbcTemplate().queryForList(sql);

        if (playersData != null) {
            ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersDataList = new ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData>();
            for (Map row : playersData) {
                PlayerWithGameProfAndStatisticsAndChallengeData tmpData = new PlayerWithGameProfAndStatisticsAndChallengeData();

                Player playerTmpData = new Player();
                playerTmpData.setId((Integer) row.get(PlayerTable.ID));
                playerTmpData.setName((String) row.get(PlayerTable.NAME));
                tmpData.setPlayer(playerTmpData);

                PlayerGameProfile playerTmpGameProfile = new PlayerGameProfile();
                playerTmpGameProfile.setScores((Integer) row.get(PlayerGameProfileTable.SCORES));
                playerTmpGameProfile.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                playerTmpGameProfile.setCountryCode((String) row.get(PlayerGameProfileTable.COUNTRY_CODE));
                playerTmpGameProfile.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                tmpData.setGameProfile(playerTmpGameProfile);

                LevelsProgress levelsProgressData = new LevelsProgress();
                levelsProgressData.setLevelsProgressData((String) row.get(LevelsProgressTable.LEVELS_PROGRESS_DATA));
                levelsProgressData.setLevelsProgressRankingPoints((Integer) row.get(LevelsProgressTable.SINGLE_PLAYER_RANKING_POINTS));
                tmpData.setLevelsProgress(levelsProgressData);

                playersDataList.add(tmpData);
            }
            return playersDataList;

        } else {
            return null;
        }
    }

    public ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> getPlayersWithGameProfAndStatAndWeekChallengeData(List<Integer> playersIds) {
        StringBuilder sqlInValues = new StringBuilder();

        sqlInValues.append("(");
        for (int k = 0; k < playersIds.size(); k++) {
            sqlInValues.append(playersIds.get(k));
            if (k != (playersIds.size() - 1)) {
                sqlInValues.append(",");
            }
        }
        sqlInValues.append(")");

        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*, " + WeekRankingTable.TBL_NAME + ".*"
                + " FROM " + PlayerTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                + " LEFT JOIN " + WeekRankingTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + WeekRankingTable.TBL_NAME + "." + WeekRankingTable.PLAYER_ID
                + " WHERE " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " IN " + sqlInValues.toString()
                + " ORDER BY " + WeekRankingTable.WEEK_SCORES + " DESC";

        List<Map<String, Object>> playersData = this.getJdbcTemplate().queryForList(sql);

        if (playersData != null) {
            ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> playersDataList = new ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData>();
            for (Map row : playersData) {
                PlayerWithGameProfAndStatisticsAndChallengeData tmpData = new PlayerWithGameProfAndStatisticsAndChallengeData();

                Player playerTmpData = new Player();
                playerTmpData.setId((Integer) row.get(PlayerTable.ID));
                playerTmpData.setName((String) row.get(PlayerTable.NAME));
                tmpData.setPlayer(playerTmpData);

                PlayerGameProfile playerTmpGameProfile = new PlayerGameProfile();
                playerTmpGameProfile.setScores((Integer) row.get(PlayerGameProfileTable.SCORES));
                playerTmpGameProfile.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                playerTmpGameProfile.setCountryCode((String) row.get(PlayerGameProfileTable.COUNTRY_CODE));
                playerTmpGameProfile.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                tmpData.setGameProfile(playerTmpGameProfile);

                ScoresRankingEntry challengeData = new ScoresRankingEntry();
                challengeData.setPlayerId((Integer) row.get(WeekRankingTable.PLAYER_ID));
                challengeData.setScores((Integer) row.get(WeekRankingTable.WEEK_SCORES));
                tmpData.setChallengeData(challengeData);

                playersDataList.add(tmpData);
            }
            return playersDataList;

        } else {
            return null;
        }
    }

    public PlayerWithGameProfAndStatisticsAndChallengeData getPlayerWithGameProfle(int playerId) {

        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*"
                + " FROM " + PlayerTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                //+ " LEFT JOIN " + WeekRankingTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + WeekRankingTable.TBL_NAME + "." + WeekRankingTable.PLAYER_ID
                + " WHERE " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + "=" + playerId;

        List<Map<String, Object>> playersData = this.getJdbcTemplate().queryForList(sql);

        if (playersData != null) {
            for (Map row : playersData) {

                PlayerWithGameProfAndStatisticsAndChallengeData tmpData = new PlayerWithGameProfAndStatisticsAndChallengeData();

                Player playerTmpData = new Player();
                playerTmpData.setId((Integer) row.get(PlayerTable.ID));
                playerTmpData.setName((String) row.get(PlayerTable.NAME));
                tmpData.setPlayer(playerTmpData);

                PlayerGameProfile playerTmpGameProfile = new PlayerGameProfile();
                playerTmpGameProfile.setScores((Integer) row.get(PlayerGameProfileTable.SCORES));
                playerTmpGameProfile.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                playerTmpGameProfile.setCountryCode((String) row.get(PlayerGameProfileTable.COUNTRY_CODE));
                playerTmpGameProfile.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                playerTmpGameProfile.setPuchasedItemsList((String) row.get(PlayerGameProfileTable.PURCHASED_ITEMS));
                playerTmpGameProfile.setCoinsBalance((Integer) row.get(PlayerGameProfileTable.COINS_BALANCE));
                playerTmpGameProfile.setNotifyAboutOnlinePlayers((Integer) row.get(PlayerGameProfileTable.NOTIFY_ABT_ONLINE_PLAYERS));
                tmpData.setGameProfile(playerTmpGameProfile);

                /*
                 ScoresRankingEntry challengeData = new ScoresRankingEntry();
                 challengeData.setPlayerId((Integer) row.get(WeekRankingTable.PLAYER_ID));
                 challengeData.setScores((Integer) row.get(WeekRankingTable.WEEK_SCORES));
                 tmpData.setChallengeData(challengeData);
                 */
                return tmpData;

            }
        } else {
            return null;
        }
        return null;
    }

    public ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> getPlayersListWithGameProfleWithOnlinePlayersNotificationEnabled() {

        String sql = "SELECT " + PlayerTable.TBL_NAME + ".*, " + PlayerGameProfileTable.TBL_NAME + ".*"
                + " FROM " + PlayerTable.TBL_NAME
                + " LEFT JOIN " + PlayerGameProfileTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.PROFILE_ID
                //+ " LEFT JOIN " + WeekRankingTable.TBL_NAME + " ON " + PlayerTable.TBL_NAME + "." + PlayerTable.ID + " = " + WeekRankingTable.TBL_NAME + "." + WeekRankingTable.PLAYER_ID
                + " WHERE " + PlayerGameProfileTable.TBL_NAME + "." + PlayerGameProfileTable.NOTIFY_ABT_ONLINE_PLAYERS + "!= 0";

        List<Map<String, Object>> playersData = this.getJdbcTemplate().queryForList(sql);

        if (playersData != null) {

            ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData> data = new ArrayList<PlayerWithGameProfAndStatisticsAndChallengeData>();

            for (Map row : playersData) {

                PlayerWithGameProfAndStatisticsAndChallengeData tmpData = new PlayerWithGameProfAndStatisticsAndChallengeData();

                Player playerTmpData = new Player();
                playerTmpData.setId((Integer) row.get(PlayerTable.ID));
                playerTmpData.setName((String) row.get(PlayerTable.NAME));
                tmpData.setPlayer(playerTmpData);

                PlayerGameProfile playerTmpGameProfile = new PlayerGameProfile();
                playerTmpGameProfile.setScores((Integer) row.get(PlayerGameProfileTable.SCORES));
                playerTmpGameProfile.setRank((Integer) row.get(PlayerGameProfileTable.RANK));
                playerTmpGameProfile.setCountryCode((String) row.get(PlayerGameProfileTable.COUNTRY_CODE));
                playerTmpGameProfile.setAvatar((String) row.get(PlayerGameProfileTable.AVATAR));
                playerTmpGameProfile.setPuchasedItemsList((String) row.get(PlayerGameProfileTable.PURCHASED_ITEMS));
                playerTmpGameProfile.setCoinsBalance((Integer) row.get(PlayerGameProfileTable.COINS_BALANCE));
                playerTmpGameProfile.setNotifyAboutOnlinePlayers((Integer) row.get(PlayerGameProfileTable.NOTIFY_ABT_ONLINE_PLAYERS));
                tmpData.setGameProfile(playerTmpGameProfile);

                data.add(tmpData);

                /*
                 ScoresRankingEntry challengeData = new ScoresRankingEntry();
                 challengeData.setPlayerId((Integer) row.get(WeekRankingTable.PLAYER_ID));
                 challengeData.setScores((Integer) row.get(WeekRankingTable.WEEK_SCORES));
                 tmpData.setChallengeData(challengeData);
                 */
            }

            return data;
        } else {
            return null;
        }

    }

    private class PlayerRowMapper implements RowMapper {

        public PlayerRowMapper() {
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            Player player = new Player();

            player.setId(rs.getInt(PlayerTable.ID));
            player.setName(rs.getString(PlayerTable.NAME));
            player.setPassword(rs.getString(PlayerTable.PASSWORD));
            player.setEmail(rs.getString(PlayerTable.EMAIL));

            return player;
        }
    }

    private class LevelProgressDataRowMapper implements RowMapper {

        public LevelProgressDataRowMapper() {
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            LevelsProgress levelsProgress = new LevelsProgress();

            levelsProgress.setPlayerId(rs.getInt(LevelsProgressTable.PLAYER_ID));
            levelsProgress.setLevelsProgressData(rs.getString(LevelsProgressTable.LEVELS_PROGRESS_DATA));
            levelsProgress.setLevelsProgressRankingPoints(rs.getInt(LevelsProgressTable.SINGLE_PLAYER_RANKING_POINTS));

            return levelsProgress;
        }
    }
}
