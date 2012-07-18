package no.steria.sqlserverconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.jtds.jdbc.Driver;
import net.sourceforge.jtds.jdbcx.JtdsDataSource;

public class ConnectorTester {
	public static void main(String[] args) throws Exception {
		String serverAddress = System.getProperty("ctest.serverAddress");
		String databaseName = System.getProperty("ctest.databaseName");;
		String user = System.getProperty("ctest.user");;
		String password = System.getProperty("ctest.password");;
		if (nullOrEmpty(serverAddress)) {
			System.out.println("The following system properties must be set (for example using -Dctest.ServerAddress=\"<ipaddr\"");
			System.out.println("ctest.serverAddress = ip address to server");
			System.out.println("ctest.databaseName = Database name");
			System.out.println("ctest.user = Database name");
			System.out.println("ctest.password = Database name");
			System.out.println("(optional) ctest.port = Database port - default 1433");
			return;
		}
		int port = 1433;
		String ports = System.getProperty("ctest.port");
		if (!nullOrEmpty(ports)) {
			port = Integer.parseInt(ports);
		}
		
		System.out.println("Connecting to '" + serverAddress + "' db '" + databaseName + "' port '" + port + "' user '" + user + "'");
		
		JtdsDataSource datasource = new JtdsDataSource();
		datasource.setServerType(Driver.SQLSERVER);
		datasource.setServerName(serverAddress);
		datasource.setPortNumber(port);
		datasource.setDatabaseName(databaseName);
		datasource.setUser(user);
		datasource.setPassword(password);
		
		try (Connection connection = datasource.getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement("select Name from Company")) {
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						System.out.println(resultSet.getString(1));
					}
				}
			}
		}

	}

	private static boolean nullOrEmpty(String value) {
		return value == null || value.isEmpty();
	}
}
