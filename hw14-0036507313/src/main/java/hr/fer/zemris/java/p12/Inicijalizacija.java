package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.p12.dao.sql.SQLConnectionProvider;
import hr.fer.zemris.java.p12.dao.sql.SQLDAO;

/**
 * Pri pokretanju poslužitelja inicijalizira bazen poveznica
 * sa slojem za prezistenciju podataka i potom provjerava jesu li
 * prisutni svi podaci o anketama, ako nisu onda ih inicijalizira.<br>
 * Pri gašenju poslužitelja zatvara bazen poveznica.
 * @author Luka Dragutin
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Properties p = new Properties();
		try {
			p.load(Files
					.newInputStream(Paths.get(sce.getServletContext().getRealPath("WEB-INF/dbsettings.properties"))));
		} catch (IOException e) {
			throw new RuntimeException("Error while loading properties file.", e);
		}

		
		String connectionURL = "jdbc:derby://" + p.getProperty("host") + ":" + p.getProperty("port") + "/"
				+ p.getProperty("name") + ";user=" + p.getProperty("user") + ";password=" + p.getProperty("password");

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);

		
		Connection conn = null;
		try {
			conn = cpds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Database not available.", e);
		}
		SQLConnectionProvider.setConnection(conn);

		
		SQLDAO sql = new SQLDAO();
		try {

			sql.initializeDatabase();
		} catch (SQLException e) {
			throw new RuntimeException("Database cannot be initialized", e);
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}