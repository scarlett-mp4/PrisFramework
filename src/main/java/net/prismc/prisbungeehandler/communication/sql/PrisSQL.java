package net.prismc.prisbungeehandler.communication.sql;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.prismc.prisbungeehandler.api.UtilApi;
import net.prismc.prisbungeehandler.events.pris.PrisFirstJoinEvent;
import net.prismc.prisbungeehandler.prisplayer.OfflinePrisPlayer;
import net.prismc.prisbungeehandler.prisplayer.PrisPlayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PrisSQL {

    public void importDatabase() throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            String sql = "CREATE TABLE IF NOT EXISTS `pris_players` ("
                    + "`id` INT(16) AUTO_INCREMENT, "
                    + "`uuid` VARCHAR(38) NOT NULL, "
                    + "`username` VARCHAR(16) NOT NULL, "
                    + "`lang` VARCHAR(38) NOT NULL, "
                    + "`lastseen` VARCHAR(256) NOT NULL, "
                    + "`firstjoined` VARCHAR(256) NOT NULL, "
                    + "`ip` VARCHAR(256) NOT NULL, "
                    + "`playtime` VARCHAR(256) NOT NULL, "
                    + "`level` VARCHAR(38) NOT NULL, "
                    + "`progress` VARCHAR(38) NOT NULL, "
                    + "`settings` VARCHAR(256) NOT NULL, "
                    + "`priscoins` VARCHAR(1024) NOT NULL, "
                    + "`rubies` VARCHAR(1024) NOT NULL, "
                    + "`karma` VARCHAR(1024) NOT NULL, "
                    + "PRIMARY KEY (`id`));"; // Primary Key
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS `pris_friends` ("
                    + "`one` INT NOT NULL, `two` INT NOT NULL, " +
                    "PRIMARY KEY (one,two));";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS `pris_achievements` ("
                    + "`key` VARCHAR(64) NOT NULL, "
                    + "`id` INT(16) NOT NULL, "
                    + "`achievement` VARCHAR(16) NOT NULL, "
                    + "`completed` VARCHAR(1024) NOT NULL, "
                    + "PRIMARY KEY (`key`));"; // Primary Key
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt.close();
        con.close();
    }

    public void firstJoin(ProxiedPlayer p) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            String sql = "INSERT INTO pris_players (id, uuid, username, lang, lastseen, firstjoined, ip, playtime, level, progress, settings, priscoins, rubies, karma) VALUES ("
                    + "NULL, " // Autoincrement
                    + "'" + p.getUniqueId().toString() + "', "
                    + "'" + p.getName() + "', "
                    + "'" + "english" + "', "
                    + "'" + UtilApi.getTime() + "', "
                    + "'" + UtilApi.getTime() + "', "
                    + "'" + p.getSocketAddress().toString() + "', "
                    + "'" + "0" + "', "
                    + "'" + "1" + "', "
                    + "'" + "0" + "', "
                    + "'" + "11112" + "', "
                    + "'" + "0" + "', "
                    + "'" + "0" + "', "
                    + "'" + "0" + "'"
                    + ");";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt.close();
        con.close();
        ProxyServer.getInstance().getPluginManager().callEvent(new PrisFirstJoinEvent(p));
    }

    public void anyJoin(PrisPlayer player) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            String sql = "UPDATE pris_players SET "
                    + "username" + "='" + player.getUsername() + "', "
                    + "ip" + "='" + player.getSocketAddress().toString() + "',"
                    + "level='" + player.toOfflinePlayer().getLevel() + "'" + ", "
                    + "progress='" + player.toOfflinePlayer().getProgress() + "'" + ", "
                    + "settings='" + player.toOfflinePlayer().getAllSettings() + "'"
                    + " WHERE "  // Where Statement
                    + "uuid" + "='" + player.getUniqueId().toString() + "';";
            stmt.execute(sql);

            player.toOfflinePlayer().setUsername(player.getUsername(), false);
            player.toOfflinePlayer().setIP(player.getSocketAddress().toString(), false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt.close();
        con.close();
    }

    public List<String> getFriends(OfflinePrisPlayer offlinePrisPlayer) throws SQLException {
        List<String> list = new LinkedList<>();
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery("SELECT one, two FROM "
                    + "pris_friends WHERE one='" + offlinePrisPlayer.getID() + "' OR two='" + offlinePrisPlayer.getID() + "'");
            while (rs.next()) {
                String friend1 = rs.getString("one");
                String friend2 = rs.getString("two");
                if (friend1.equals(String.valueOf(offlinePrisPlayer.getID())))
                    list.add(friend2);
                else list.add(friend1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rs != null) {
            rs.close();
        }
        stmt.close();
        con.close();

        return list;
    }

    public HashMap<String, String> getAllData(UUID uuid) throws SQLException {
        HashMap<String, String> resultMap = new HashMap<>();
        String[] prisPlayers = new String[]{"id", "uuid", "username", "lang", "lastseen", "firstjoined", "ip", "playtime", "level", "progress", "settings", "priscoins", "rubies", "karma"};
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM pris_players WHERE uuid='" + uuid.toString() + "'");
            if (rs.next()) {
                for (String s : prisPlayers) {
                    resultMap.put(s, rs.getString(s));
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt.close();
        con.close();
        return resultMap;
    }

    public HashMap<String, String> getAllData(String username) throws SQLException {
        HashMap<String, String> resultMap = new HashMap<>();
        String[] prisPlayers = new String[]{"id", "uuid", "username", "lang", "lastseen", "firstjoined", "ip", "playtime", "level", "progress", "settings", "priscoins", "rubies", "karma"};
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM pris_players WHERE username='" + username + "'");
            if (rs.next()) {
                for (String s : prisPlayers) {
                    resultMap.put(s, rs.getString(s));
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt.close();
        con.close();
        return resultMap;
    }

    public HashMap<String, String> getAllData(int id) throws SQLException {
        HashMap<String, String> resultMap = new HashMap<>();
        String[] prisPlayers = new String[]{"id", "uuid", "username", "lang", "lastseen", "firstjoined", "ip", "playtime", "level", "progress", "settings", "priscoins", "rubies", "karma"};
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM pris_players WHERE id='" + id + "'");
            if (rs.next()) {
                for (String s : prisPlayers) {
                    resultMap.put(s, rs.getString(s));
                }
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt.close();
        con.close();
        return resultMap;
    }

    public void updateData(PrisPlayer p) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            String sql = "UPDATE " + "pris_players" + " SET "
                    + "username='" + p.getUsername() + "'" + ", "
                    + "lang='" + p.toOfflinePlayer().getLanguage() + "'" + ", "
                    + "lastseen='" + UtilApi.getTime() + "'" + ", "
                    + "playtime='" + p.toOfflinePlayer().getPlayTime() + "'" + ", "
                    + "level='" + p.toOfflinePlayer().getLevel() + "'" + ", "
                    + "progress='" + p.toOfflinePlayer().getProgress() + "'" + ", "
                    + "settings='" + p.toOfflinePlayer().getAllSettings() + "'"
                    + " WHERE " + "uuid='" + p.toOfflinePlayer().getUniqueId().toString() + "';";
            stmt.execute(sql);

            p.toOfflinePlayer().setUsername(p.getUsername(), false);
            p.toOfflinePlayer().setLastSeen(UtilApi.getTime(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        stmt.close();
        con.close();
    }

    public boolean hasAchievement(PrisPlayer p, String achievement) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = Database.getHikari().getConnection();
            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM pris_achievements WHERE achievement = '" + achievement + "' AND id = '" + p.toOfflinePlayer().getID() + "' LIMIT 1");
            if (rs.next()) {
                rs.close();
                stmt.close();
                con.close();
                return true;
            }
            rs.close();
            stmt.close();
            con.close();

            return false;
        } catch (Exception e) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
            try {
                stmt.close();
            } catch (SQLException ignored) {
            }
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }

        return false;
    }

    public void addAchievement(PrisPlayer p, String achievement) throws SQLException {
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();

        try {
            String sql = "INSERT INTO pris_achievements VALUES ("
                    + "'" + p.toOfflinePlayer().getID() + "-" + achievement + "', "
                    + "'" + p.toOfflinePlayer().getID() + "', "
                    + "'" + achievement + "', "
                    + "'" + UtilApi.getTime() + "'"
                    + ");";
            stmt.execute(sql);
        } catch (Exception e) {
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(ChatColor.RED + p.getUsername() + " attempted to add duplicate achievements!"));
        }

        stmt.close();
        con.close();
    }

    public List<String> getAchievements(PrisPlayer p) throws SQLException {
        List<String> list = new LinkedList<>();
        Connection con = Database.getHikari().getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = null;

        try {
            rs = stmt.executeQuery("SELECT achievement FROM pris_achievements WHERE id='" + p.toOfflinePlayer().getID() + "'");
            while (rs.next()) {
                String achievement = rs.getString("achievement");
                list.add(achievement);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rs != null) {
            rs.close();
        }
        stmt.close();
        con.close();

        return list;
    }
}
