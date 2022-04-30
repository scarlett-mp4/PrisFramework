package net.prismc.prisbungeehandler.communication.sql;

import com.zaxxer.hikari.HikariDataSource;
import net.prismc.prisbungeehandler.PrisBungeeHandler;

import java.sql.SQLException;

public class Database {
    public static HikariDataSource hikari;
    private final PrisBungeeHandler instance;

    public Database(PrisBungeeHandler instance) throws SQLException {
        this.instance = instance;
        this.configureDatabase();
    }

    public static HikariDataSource getHikari() {
        return hikari;
    }

    public static void closeConnection() throws SQLException {
        hikari.close();
    }

    public void configureDatabase() {
        hikari = new HikariDataSource();

        hikari.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        hikari.addDataSourceProperty("port", instance.config.getConfig().getString("MySQL.Port"));
        hikari.addDataSourceProperty("user", instance.config.getConfig().getString("MySQL.Username"));
        hikari.addDataSourceProperty("serverName", instance.config.getConfig().getString("MySQL.Host"));
        hikari.addDataSourceProperty("password", instance.config.getConfig().getString("MySQL.Password"));
        hikari.addDataSourceProperty("databaseName", instance.config.getConfig().getString("MySQL.Database"));

        hikari.addDataSourceProperty("cachePrepStmts", instance.config.getConfig().getBoolean("MySQL.cachePrepStmts"));
        hikari.addDataSourceProperty("prepStmtCacheSize", instance.config.getConfig().getInt("MySQL.prepStmtCacheSize"));
        hikari.addDataSourceProperty("prepStmtCacheSqlLimit", instance.config.getConfig().getInt("MySQL.prepStmtCacheSqlLimit"));
        hikari.addDataSourceProperty("useServerPrepStmts", instance.config.getConfig().getBoolean("MySQL.useServerPrepStmts"));
        hikari.addDataSourceProperty("useLocalSessionState", instance.config.getConfig().getBoolean("MySQL.useLocalSessionState"));
        hikari.addDataSourceProperty("rewriteBatchedStatements", instance.config.getConfig().getBoolean("MySQL.rewriteBatchedStatements"));
        hikari.addDataSourceProperty("cacheResultSetMetadata", instance.config.getConfig().getBoolean("MySQL.cacheResultSetMetadata"));
        hikari.addDataSourceProperty("cacheServerConfiguration", instance.config.getConfig().getBoolean("MySQL.cacheServerConfiguration"));
        hikari.addDataSourceProperty("elideSetAutoCommits", instance.config.getConfig().getBoolean("MySQL.elideSetAutoCommits"));
        hikari.addDataSourceProperty("maintainTimeStats", instance.config.getConfig().getBoolean("MySQL.maintainTimeStats"));
        hikari.addDataSourceProperty("useSSL", false);
        hikari.setMaximumPoolSize(instance.config.getConfig().getInt("MySQL.maximumPoolSize"));
        hikari.setLeakDetectionThreshold(instance.config.getConfig().getInt("MySQL.leakDetectionThreshold"));
    }
}
