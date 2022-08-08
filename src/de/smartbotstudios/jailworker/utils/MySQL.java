package de.smartbotstudios.jailworker.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class MySQL {

    private String HOST = "";
    private String PORT = "";
    private String DATABASE = "";
    private String USER = "";
    private String PASSWORD = "";

    public Connection con;

    public MySQL(String host, String port, String database, String user, String password) {

        this.HOST = host;
        this.PORT = port;
        this.DATABASE = database;
        this.USER = user;
        this.PASSWORD = password;

        connect();
    }

    public void connect() {

        String Prefix = "JailWorker";

        try {
            con = (Connection) DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?autoReconnect=true&cmaxReconnets=5&initialTimeout=1", USER, PASSWORD);

            Bukkit.getConsoleSender().sendMessage(Prefix + "§aDie Verbindung mit MySQL wurde hergestellt!");
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(Prefix + "§cDie Verbindung mit MySQL ist fehlgeschlagen! §4Fehler: " + e.getMessage());
        }
    }

    public void close() {

        String Prefix = "JailWorker";

        try {
            if (con != null) {
                con.close();
                Bukkit.getConsoleSender().sendMessage(Prefix + "§aDie Verbindung mit MySQL wurde beendet!");
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(Prefix + "§cDie Verbindung mit MySQL konnte nicht beendet werden! §4Fehler: " + e.getMessage());
        }

    }

    public void update(String qre) {
        if (con != null) {
            try {
                Statement st = (Statement) con.createStatement();
                st.executeUpdate(qre);
                st.close();
            } catch (SQLException e) {
                connect();
                System.err.print(e);
            }
        }
    }

    public ResultSet query(String qre) {
        if (con != null) {
            ResultSet rs = null;

            try {
                Statement st = (Statement) con.createStatement();
                rs = st.executeQuery(qre);
            } catch (SQLException e) {
                connect();
                System.err.print(e);
            }
            return rs;
        }
        return null;
    }

    public static boolean isFilled(ResultSet rs) {
        boolean isEmpty = true;
        try {
            while (rs.next()) {
                isEmpty = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return !isEmpty;
    }

    public Long getCooldown(Player p) {
        Long i = 0l;

        ResultSet rs = query("SELECT UUID,Time FROM JailWorkerCooldowns WHERE UUID = '" + p.getUniqueId() + "';");
        if (isFilled(rs)) {
            try {
                return rs.getLong("Time");
            } catch (SQLException e) {
                e.printStackTrace();
                return i;
            }
        }

        return i;
    }
}
