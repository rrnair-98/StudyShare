package services.microservices.database.corefunction;

import java.sql.*;
import java.util.ArrayList;
import org.sqlite.JDBC;

interface CONSTANT
{
	String CLASSDRIVER="org.sqlite.JDBC";
	String DATABASEPATH="jdbc:sqlite:/Users/damianmandrake/Projects/libs/client.db";

}
/*
	@author:jaynam
*/

public class DatabaseHandler implements CONSTANT
{
	/*
		this class will load the driver and establish the connection with db and return statement obj
	*/
	private static Connection con;
	public static ArrayList list = new ArrayList();
	static
	{
		try
		{
			Class.forName(CLASSDRIVER);
			DatabaseHandler.con=DriverManager.getConnection(DATABASEPATH);

		}
		catch(Exception e)
		{
			/* Logger.wtf(e.toString());*/
			e.printStackTrace();
		}
	}
	private static Statement getStatement()
	{
		try
		{
			Statement stmt =DatabaseHandler.con.createStatement();
			return stmt;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}


	public static boolean createInsertQuery(String table_name,Object[] values)
	{
		/*
			table_name will take the name of the table and the value of it
			we will find the length of value array and if it matches with no.of.colomns then we will insert it or else throw exception
		*/
		try
		{
			int i;
			String tname = table_name;
			int length = values.length;

			Statement stmt = DatabaseHandler.getStatement();
			if(stmt != null)
			{
				String sql = "select * from " + tname + ";";
				System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				int colnumber = rsmd.getColumnCount();
				if (length == colnumber)
				{

					/*
						first we will insert into the table and then check if its ROLES then we will fire Triggered Condition
					*/
					sql = "INSERT INTO " + tname + " (";
					for (i = 1; i <= length; i++)
					{
						System.out.println(rsmd.getColumnName(i));
						if(i == length)
							sql += rsmd.getColumnName(i);
						else
							sql+=rsmd.getColumnName(i)+",";
						System.out.println(rsmd.getColumnTypeName(i));
					}
					sql += ")" + " VALUES (";
					for (i = 1; i <= length; i++)
						if(i == length)
							sql += "?";
						else
							sql += "?,";
					sql += ");";
					System.out.println(sql);
					PreparedStatement preparedStmt = con.prepareStatement(sql);
					for(i=1;i<=length;i++)
					{
						System.out.println(rsmd.getColumnTypeName(i));
						if(rsmd.getColumnTypeName(i).equals("INTEGER"))
						{

							System.out.println("i="+i+" value:"+values[i-1]);
							preparedStmt.setLong(i,Long.parseLong(values[i-1].toString()));
						}
						else if(rsmd.getColumnTypeName(i).equals("TEXT"))
						{
							System.out.println("i="+i+" value:"+values[i-1]);
							preparedStmt.setString(i,values[i-1].toString());
						}
					}
					preparedStmt.execute();
					if (tname == "ROLES") {
						/*
							two more table will be created
							first will be the table which will havename of the subject added and it will contain path of files
							second will be having same name but it will have users added in it
						*/
						System.out.println(values[1]);
						createTrigger(values[1].toString(), stmt);
					}
				}
				else
				{
					System.out.println("no of parameter passed are not equal as req");
					return false;
				}
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private static void createTrigger(String table_name, Statement stmt)
	{
		try
		{
			System.out.println(table_name);
			String query="CREATE TABLE "+table_name +"("+
					"id INTEGER PRIMARY KEY ,"+
					"filepath TEXT(1000)"+
					");";
			System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("table created");
			table_name+="USERS";
			System.out.println(table_name);
			query="CREATE TABLE "+table_name +"("+
					"user_email_id TEXT(1000),"+
					"PRIMARY KEY (user_email_id),"+
					"FOREIGN KEY (user_email_id) REFERENCES USERS(email_id)"+
					");";
			System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("table created");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean createUpdateQuery(String table_name,String column,Object value,String columnforcondition,Object valueforcondition)
	{
		/*
			table_name would be pass in which update is needed.
			UPDATE COMPANY SET ADDRESS = 'Texas' WHERE ID = 6;
			here
			COMPANY is the table_name
			ADDRESS is the column
			ID is columnforcondition
			6 is the valueforcondition
			Texas is value


		*/
		try
		{
			String tname = table_name;
			String sql=null;
			Statement stmt = DatabaseHandler.getStatement();
			if(stmt != null)
			{
				sql = "UPDATE "+tname+
						" SET "+column +" = "+"'"+value+"'"+
						" WHERE "+columnforcondition+" = "+"'"+valueforcondition+"'"+";";
			}
			System.out.println(sql);
			PreparedStatement preparedStmt = con.prepareStatement(sql);
			if (createSelectQuery(tname, columnforcondition, valueforcondition.toString())) {
				preparedStmt.executeUpdate();
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return  false;
		}
	}

	public static boolean createDeleteQuery(String table_name,String colforcondition,String valueforcondition)
	{
		try {
			String tname = table_name;
			String sql = null;
			Statement stmt = DatabaseHandler.getStatement();
			if (stmt != null) {
				sql = "DELETE FROM " + tname +
						" WHERE " + colforcondition + " = '" + valueforcondition + "';";
				System.out.println(sql);
			}
			PreparedStatement preparedStmt = con.prepareStatement(sql);
			if (createSelectQuery(tname, colforcondition, valueforcondition)) {
				preparedStmt.executeUpdate();
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createDropQuery(String table_name)
	{
		try
		{
			String tname = table_name;
			String sql = null;
			Statement stmt = DatabaseHandler.getStatement();
			if (stmt != null)
			{
				sql = "DROP TABLE " + tname +" ;";
				System.out.println(sql);
			}
			PreparedStatement preparedStmt = con.prepareStatement(sql);
			preparedStmt.executeUpdate();
			return true;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return false;
		}
	}

	public static boolean createVerifyQuery(String table_name,String userid,long password)
	{
		try
		{
			String sql="SELECT * FROM "+table_name+" WHERE email_id ='"+userid+"' AND password= "+password;
			System.out.println(sql);
			Statement stmt = DatabaseHandler.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next())
				return true;
			else
				return false;
		}

		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}
	}

	public static ArrayList getFilePaths(String table_name)
	{
		String tname=table_name;
		String sql=null;
		ArrayList<String> paths=new ArrayList<String>();
		try
		{
			sql = "select filepath from " + tname +";";
			System.out.println(sql);
			Statement stmt = DatabaseHandler.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			String path="";
			while (rs.next())
			{
				paths.add(rs.getString("filepath"));
			}

			return paths;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return null;
		}

	}

	public static boolean createSelectQuery(String table_name,String colforcondition,String valueforcondition)
	{
		/*
			We will check if colforcond or valueforcond is null then we will bring the whole table
			else
			the specific part
		 */
		String tname=table_name;
		String sql=null;

		try {
			if (colforcondition != null && valueforcondition != null) {
				sql = "select * from " + tname + " WHERE "+colforcondition+" = "+"'"+valueforcondition+"';";
				System.out.println(sql);
			}
			else
			{
				sql = "select * from " + tname +";";
				System.out.println(sql);
			}
			Statement stmt = DatabaseHandler.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			int count=0;
			for(int i=1;i<=columnsNumber;i++)
				list.add(rsmd.getColumnName(i));
			while (rs.next())
			{
				/*
					will put the value in arraylist here
				*/
				count++;

				for (int i = 1; i <= columnsNumber; i++)
				{
					String columnValue = rs.getString(i);
					list.add(columnValue);
				}
			}
			if(count==0)
				return false;
			else
				return true;

		}
		catch (Exception e)
		{
			System.out.println(e);
			return false;
		}
	}
}