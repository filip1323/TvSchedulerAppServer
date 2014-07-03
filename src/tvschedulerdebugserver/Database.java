/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tvschedulerdebugserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Filip
 */
public class Database {

    private SQLiteDB db;

    static private Database instance = null;

    public static Database getInstance() {

	if (instance == null) {
	    instance = new Database();

	}
	return instance;

    }

    private Database() {
	try {
	    db = new SQLiteDB();
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
    }

    private class SQLiteDB {

	private final Connection connection;
	private final Statement statement;

	public SQLiteDB() throws SQLException {

	    connection = DriverManager.getConnection("jdbc:sqlite:database.db");
	    statement = connection.createStatement();
	    statement.setQueryTimeout(30);

	}

	public ResultSet query(String query) {
	    try {
		if (query.contains("SELECT")) {
		    return statement.executeQuery(query);

		} else {
		    statement.execute(query);
		    return null;
		}
	    } catch (SQLException ex) {
		ex.printStackTrace();
	    }

	    return null;

	}

    }

    public void changeUserName(String macAddress, String name) {
	String query;
	if (userExistsByMacAddress(macAddress)) {
	    query = "UPDATE `users` SET `userName` = '" + name + "'   WHERE `macAddress` = '" + macAddress + "';";
	} else {
	    query = "INSERT INTO `users` (`macAddress`,`userName`) VALUES ('" + macAddress + "','" + name + "');";
	}
	db.query(query);
    }

    public boolean userExistsByMacAddress(String macAddress) {
	String query = "SELECT * FROM `users` WHERE `macAddress`='" + macAddress + "';";
	try {
	    return (db.query(query).next());
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	return false;
    }

    public String getUserNameByMacAddress(String macAddress) {
	String query = "SELECT * FROM `users` WHERE `macAddress`='" + macAddress + "';";
	try {
	    String result = db.query(query).getString("userName");
	    if (result == null) {
		return macAddress;
	    }
	    return result;
	} catch (SQLException ex) {
	    //ex.printStackTrace();
	}
	return null;
    }
}
