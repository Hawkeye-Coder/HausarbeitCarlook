package org.carlook.services.db;

import com.vaadin.ui.Notification;
import org.carlook.process.exceptions.DatabaseException;
import org.carlook.services.util.SafeString;

import java.sql.*;
import java.util.Properties;

public class JDBCConnection {
    private static JDBCConnection connection = null;
    //Login Datenbank
    private String login = "mwinze2s";
    private String passwort = "mwinze2s";
    private String url = "jdbc:postgresql://dumbo.inf.h-brs.de/mwinze2s";
    private Connection conn = null;

    public static JDBCConnection getInstance() throws DatabaseException {
        if (connection == null) {
            connection = new JDBCConnection();
        }
        return connection;
    }

    private JDBCConnection() throws DatabaseException {
        this.initConnection();

    }

    public void initConnection() throws DatabaseException {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException throwables) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        }
        this.openConnection();
    }

    public void openConnection() throws DatabaseException {
        try {
            Properties props = new Properties();
            props.setProperty(SafeString.USER, login);
            props.setProperty(SafeString.PW, passwort);
            this.conn = DriverManager.getConnection(this.url, props);
        } catch (SQLException throwables) {
            throw new DatabaseException("Fehler bei Zugriff auf die DB! Sichere Verbindung vorhanden?");
        }
    }


    public PreparedStatement getPreparedStatement(String sql ) throws DatabaseException {
        try {
            if (this.conn.isClosed()) {
                this.openConnection();
            }
            return this.conn.prepareStatement(sql);
        } catch (SQLException throwables) {
            return null;
        }
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException throwables) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        }
    }

}