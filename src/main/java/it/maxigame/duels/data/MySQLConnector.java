package it.maxigame.duels.data;

import it.maxigame.duels.Duels;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQLConnector {

    private final String TABLE = "duels";
    private final String CONNECTION_STRING;

    private Connection connection;

    public MySQLConnector(String address, int port, String database, String username, String password) {
        this.CONNECTION_STRING = "jdbc:mariadb://" + address + ":" + port + "/" + database + "?autoReconnect=true&user=" + username + "&password=" + password;
        try {
            System.out.println("§eConnecting to database...");
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(CONNECTION_STRING);
            Statement stm = connection.createStatement();
            stm.executeUpdate("CREATE TABLE IF NOT EXISTS `" + TABLE + "` (" +
                    "  `username` varchar(16) NOT NULL," +
                    "  `wins` int(10) NOT NULL," +
                    "  `loses` int(10) NOT NULL," +
                    "  PRIMARY KEY (`username`)" +
                    ")");
            System.out.println("§eDone!");
        } catch (Exception e) {
            System.out.println("§cError while establishing MariaDB connection.");
            System.out.println("§cDisabling plugin...");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(Duels.getInstance());
        }
    }

    public boolean existPlayer(String username) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT username FROM " + TABLE + " WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void addPlayer(String username) {
        try {
            PreparedStatement stat = connection.prepareStatement("INSERT INTO " + TABLE + " (username, wins, loses) values (?,?,?)");
            stat.setString(1, username);
            stat.setInt(2, 0);
            stat.setInt(3, 0);
            stat.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("§cGiocatore già presente nel database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getWins(String username) {
        int wins = 0;
        Statement st;
        try {
            st = connection.createStatement();
            String query = ("SELECT wins FROM " + TABLE + " WHERE username='" + username + "';");
            ResultSet rs = st.executeQuery(query);
            if (rs.next())
                wins = rs.getInt("wins");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wins;
    }
    public void setWins(String username, int wins) {
        try {
            PreparedStatement stat = connection.prepareStatement("UPDATE `" + TABLE + "` SET wins=" + wins + " WHERE username='" + username + "'");
            stat.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("§cGiocatore insesistente!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getLoses(String username) {
        int loses = 0;
        Statement st;
        try {
            st = connection.createStatement();
            String query = ("SELECT loses FROM " + TABLE + " WHERE username='" + username + "';");
            ResultSet rs = st.executeQuery(query);
            if (rs.next())
                loses = rs.getInt("loses");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loses;
    }
    public void setLoses(String username, int loses) {
        try {
            PreparedStatement stat = connection.prepareStatement("UPDATE `" + TABLE + "` SET loses=" + loses + " WHERE username='" + username + "'");
            stat.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("§cGiocatore inesistente!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
