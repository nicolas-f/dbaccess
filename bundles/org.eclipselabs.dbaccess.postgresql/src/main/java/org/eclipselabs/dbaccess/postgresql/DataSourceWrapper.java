package org.eclipselabs.dbaccess.postgresql;

import org.postgis.PGbox2d;
import org.postgis.PGbox3d;
import org.postgis.PGgeometry;
import org.postgis.jts.JtsGeometry;
import org.postgresql.PGConnection;

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

    private Connection configureConnection(Connection connection) throws SQLException {
        if(connection instanceof PGConnection) {
            ((org.postgresql.PGConnection) connection).addDataType("geometry", JtsGeometry.class);
            ((org.postgresql.PGConnection) connection).addDataType("box3d", PGbox3d.class);
            ((org.postgresql.PGConnection) connection).addDataType("box2d", PGbox2d.class);
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
        throw new SQLException("Unsupported operation");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
