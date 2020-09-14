package org.carlook.model.dao;

import org.carlook.process.exceptions.DatabaseException;
import org.carlook.services.db.JDBCConnection;

import java.sql.PreparedStatement;

public class AbstractDAO {
    //Nur PreparedStatement genutzt da sicherer
    protected PreparedStatement getPreparedStatement(String sql) {
        PreparedStatement statement = null;

        try {
            statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return statement;
    }
}

