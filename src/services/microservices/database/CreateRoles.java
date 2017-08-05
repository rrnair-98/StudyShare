package services.microservices.database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

/*
	author:jaynam
*/

public class CreateRoles
{
	public static void main(String[] args)
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			Connection con=DriverManager.getConnection("jdbc:sqlite:client.db");
			System.out.println("Connected");
			Statement stmt;
			stmt=con.createStatement();
			
			/*query for creating db and roles table in it*/
			String query="CREATE TABLE ROLES("+
					      "id INTEGER PRIMARY KEY,"+
						  "tablename TEXT(1000),"+
						  "lastaccess TEXT(100)"+
						  ");";
		  	System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("table created");

			/*
				query for creating users table
				email_i and password are int as their hased value are stored
			 */
			query="CREATE TABLE USERS("+
				  "email_id TEXT(100),"+
				  "password INTEGER,"+
				  "roles text(1000),"+
				  "PRIMARY KEY (email_id)"+
				  ");";
			System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("table created");
			stmt.close();
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}