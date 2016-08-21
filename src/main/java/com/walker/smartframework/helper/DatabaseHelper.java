package com.walker.smartframework.helper;

import com.walker.smartframework.ConfigConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by wk on 2015/11/28.
 */
public final class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);
    private static ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();

    public static DataSource getDataSource(){

        return null;
    }

    public static void beginTransaction() {
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);

        } catch (Exception e) {
            LOGGER.error("begin transaction failure", e);
        }
    }

    public static void commitTransaction() {
        try {
            Connection conn = getConnection();
            conn.commit();
            closeConnection();
        } catch (Exception e) {
            LOGGER.error("commit transaction failure", e);
        }
    }

    public static void rollbackTransaction() {
        try {
            Connection conn = getConnection();
            conn.rollback();
            closeConnection();
        } catch (Exception e) {
            LOGGER.error("roll back failure", e);
        }
    }

    private static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (null == conn) {
            try {
                Class.forName(ConfigConstant.JDBC_DRIVER);
                conn = DriverManager.getConnection(ConfigConstant.JDBC_URL, ConfigConstant.JDBC_USERNAME, ConfigConstant.JDBC_PASSWORD);
                CONNECTION_HOLDER.set(conn);
            } catch (Exception e) {
                LOGGER.error("get connection failure", e);
            }
        }
        return conn;
    }

    private static void closeConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (null != conn) {
            try {
                conn.close();
                CONNECTION_HOLDER.remove();
            } catch (Exception e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }
}
