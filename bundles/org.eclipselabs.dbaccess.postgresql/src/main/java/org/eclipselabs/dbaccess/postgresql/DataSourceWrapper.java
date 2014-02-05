package org.eclipselabs.dbaccess.postgresql;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Configure Postgre connection to use PostGIS.
 * @author Nicolas Fortin
 */
public class DataSourceWrapper implements DataSource {
    private DataSource pgDataSource;

    /**
     * Constructor.
     * @param pgDataSource Instance of Postgre datasource
     */
    public DataSourceWrapper(DataSource pgDataSource) {
        this.pgDataSource = pgDataSource;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        pgDataSource.setLoginTimeout(seconds);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return configureConnection(pgDataSource.getConnection());
    }

    private Connection configureConnection(Connection connection) {
        if(connection instanceof org.postgresql.Connection) {
            ((org.postgresql.Connection) connection).addDataType("geometry", "org.postgis.PGgeometry");
            ((org.postgresql.Connection) connection).addDataType("box3d", "org.postgis.PGbox3d");
        }
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return configureConnection(pgDataSource.getConnection(username, password));
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return pgDataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        pgDataSource.setLogWriter(out);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return pgDataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return pgDataSource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return pgDataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return pgDataSource.isWrapperFor(iface);
    }
}
