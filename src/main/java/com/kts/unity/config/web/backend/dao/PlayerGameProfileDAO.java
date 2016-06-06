package com.kts.unity.config.web.backend.dao;

import com.kts.unity.config.web.backend.utils.player.PlayerGameProfileTable;
import com.kts.unity.config.web.entities.player.PlayerGameProfile;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class PlayerGameProfileDAO extends SimpleJdbcDaoSupport {

    public int createPlayerProfile(PlayerGameProfile playerGameProfile) throws Exception {
        int status = 0;
        String playerProfileInsertSql = "INSERT INTO " + PlayerGameProfileTable.TBL_NAME + "("
                + PlayerGameProfileTable.PROFILE_ID + ","
                + PlayerGameProfileTable.COUNTRY_CODE + ","
                + PlayerGameProfileTable.SHIP_TYPE_ID + ","
                + PlayerGameProfileTable.WEAPON_TYPE_ID + ","
                + PlayerGameProfileTable.RANK + ","
                + PlayerGameProfileTable.SCORES
                + ") VALUES(?,?,?,?,?,?)";

        getSimpleJdbcTemplate().update(
                playerProfileInsertSql,
                playerGameProfile.getProfileId(),
                playerGameProfile.getCountryCode(),
                playerGameProfile.getShipType(),
                playerGameProfile.getWeaponId(),
                playerGameProfile.getRank(),
                playerGameProfile.getScores());
        status = 1;

        return status;
    }

    public int deletePlayerGameProfile(int playerId) {
        int status = 0;

        String sql = "DELETE FROM " + PlayerGameProfileTable.TBL_NAME + " WHERE " + PlayerGameProfileTable.PROFILE_ID + " = ?";
        getSimpleJdbcTemplate().update(sql, playerId);
        status = 1;

        return status;

    }

    public int updatePlayerGameProfile(PlayerGameProfile playerGameProfile) {

        int status = 0;
        String playerProfileUpdateSql = "UPDATE " + PlayerGameProfileTable.TBL_NAME + " SET "
                + PlayerGameProfileTable.COUNTRY_CODE + " = ?,"
                + PlayerGameProfileTable.SHIP_TYPE_ID + " = ?,"
                + PlayerGameProfileTable.WEAPON_TYPE_ID + " = ?,"
                + PlayerGameProfileTable.RANK + " = ?,"
                + PlayerGameProfileTable.SCORES + " = ?,"
                + PlayerGameProfileTable.PURCHASED_ITEMS + " = ?,"
                + PlayerGameProfileTable.EXPERIENCE_POINTS + " = ?,"
                + PlayerGameProfileTable.ACHIEVEMENTS + " = ?,"
                + PlayerGameProfileTable.AVATAR + " = ?,"
                + PlayerGameProfileTable.COINS_BALANCE + " = ?,"
                + PlayerGameProfileTable.NOTIFY_ABT_ONLINE_PLAYERS + " = ? "
                + "WHERE " + PlayerGameProfileTable.PROFILE_ID + " = ?";
        
        
        getSimpleJdbcTemplate().update(
                playerProfileUpdateSql,
                playerGameProfile.getCountryCode(),
                playerGameProfile.getShipType(),
                playerGameProfile.getWeaponId(),
                playerGameProfile.getRank(),
                playerGameProfile.getScores(),
                playerGameProfile.getPuchasedItemsList(),
                playerGameProfile.getExperiencePoints(),
                playerGameProfile.getAchievementsList(),
                playerGameProfile.getAvatar(),
                playerGameProfile.getCoinsBalance(),
                playerGameProfile.getNotifyAboutOnlinePlayers(),
                playerGameProfile.getProfileId());
        status = 1;

        return status;
    }

    public PlayerGameProfile getPlayerGameProfile(int profileId) {
        String sql = "SELECT * FROM " + PlayerGameProfileTable.TBL_NAME + " WHERE " + PlayerGameProfileTable.PROFILE_ID + " = ?";

        PlayerGameProfile playerGameProfile = null;

        try {
            playerGameProfile = (PlayerGameProfile) this.getJdbcTemplate().queryForObject(sql, new Object[]{profileId}, new PlayerGameProfileDAO.PlayerGameProfileRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
        }

        return playerGameProfile;
    }


    private class PlayerGameProfileRowMapper implements RowMapper {

        public PlayerGameProfileRowMapper() {
            super();
        }

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            PlayerGameProfile playerGameProfile = new PlayerGameProfile();
            playerGameProfile.setProfileId(rs.getInt(PlayerGameProfileTable.PROFILE_ID));
            playerGameProfile.setCountryCode(rs.getString(PlayerGameProfileTable.COUNTRY_CODE));
            playerGameProfile.setShipType(rs.getInt(PlayerGameProfileTable.SHIP_TYPE_ID));
            playerGameProfile.setWeaponId(rs.getInt(PlayerGameProfileTable.WEAPON_TYPE_ID));
            playerGameProfile.setRank(rs.getInt(PlayerGameProfileTable.RANK));
            playerGameProfile.setScores(rs.getInt(PlayerGameProfileTable.SCORES));
            playerGameProfile.setPuchasedItemsList(rs.getString(PlayerGameProfileTable.PURCHASED_ITEMS));
            //playerGameProfile.setNumberOfShopVisits(rs.getInt(PlayerGameProfileTable.NUMBER_OF_SHOP_VISITS));
            playerGameProfile.setExperiencePoints(rs.getInt(PlayerGameProfileTable.EXPERIENCE_POINTS));
            playerGameProfile.setAchievementsList(rs.getString(PlayerGameProfileTable.ACHIEVEMENTS));
            playerGameProfile.setAvatar(rs.getString(PlayerGameProfileTable.AVATAR));
            playerGameProfile.setCoinsBalance(rs.getInt(PlayerGameProfileTable.COINS_BALANCE));
            playerGameProfile.setNotifyAboutOnlinePlayers(rs.getInt(PlayerGameProfileTable.NOTIFY_ABT_ONLINE_PLAYERS));

            return playerGameProfile;
        }
    }
}
