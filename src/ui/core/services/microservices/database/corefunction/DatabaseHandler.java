package ui.core.services.microservices.database.corefunction;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import ui.core.services.microservices.utilities.logger.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/*
    @author:jaynam
*/


public class  DatabaseHandler
{
    private static Connection con;

    public DatabaseHandler(String DatabaseName)
    {
        /*
            this is constructor which accept the database name and load the drivers and
            connect to the database name proided
        */
        try
        {
            String databasePath="jdbc:sqlite:"+DatabaseName;
            DatabaseHandler.con = DriverManager.getConnection(databasePath);

        }
        catch(Exception e)
        {
            //Logger.wtf
            Logger.wtf(e.getStackTrace().toString());
        }
    }

    private static Statement getStatement()
    {
        /*
            this methode will be use by the other method of this class
            return the Statement object which is use to fire query
        */
        try
        {
            Statement stmt =DatabaseHandler.con.createStatement();
            return stmt;
        }
        catch (Exception e)
        {
            Logger.wtf(e.getStackTrace().toString());
            return null;
        }
    }

    private boolean createTable(String tableName,String[] columnName,String[] columnType,boolean isForeign,String firstWhichColumn,String secondWhichColumn,String firstFromWhichTable,String secondFromWhichTable)
    {
        /*
            this will create table where first column pass would pe PRIMARY KEY and if isForeign is true then FOREIGN KEY would be applied
        */
        try
        {
            if(columnName.length == columnType.length)
            {
                Statement stmt = DatabaseHandler.getStatement();
                int length = columnName.length;
                if (stmt != null)
                {
                    String query = "CREATE TABLE " + tableName + "("+ columnName[0] + " "+columnType[0] + " "+ "PRIMARY KEY,";
                    for (int i = 1; i < length; i++)
                    {
                        query += columnName[i] + " ";
                        query += columnType[i] + " ";
                         if (i == length - 1)
                            query += "";
                         else
                            query += ",";
                    }
                    if(isForeign)
                        query +=",FOREIGN KEY("+columnName[1]+") REFERENCES "+firstFromWhichTable+"("+firstWhichColumn+"),FOREIGN KEY ("+columnName[2]+") REFERENCES "+secondFromWhichTable+"("+secondWhichColumn+")";
                    query += ");";
                    System.out.println(query);
                    return stmt.execute(query);
                }
                else
                    return false;
            }
            else
            {
                System.out.println("Length doesnt match");
                return  false;
            }
        }
        catch (Exception e)
        {
            //logger.wtf
            Logger.wtf(e.getStackTrace().toString());
            return false;
        }
    }

    public int cudOperation(String query)
    {
        /*
            its the main function where all the funcion will happen
            c-insert
            u-update
            d-delete
            all the 3 operation would happen in this ,we just need to pass the query
        */
        try
        {
            Statement stmt = DatabaseHandler.getStatement();
            if (stmt != null)
            {
                System.out.println(query);
                return stmt.executeUpdate(query);
            }
            else
                return 0;
        }
        catch (Exception e)
        {
            Logger.wtf(e.getStackTrace().toString());
            return 0;
        }
    }



    public ArrayList readValues(String query)
    {
        /*
            it is use to read the value from the database and put it into arraylist.
            we just need to pass the query
        */
        ArrayList list=new ArrayList();
        try
        {
            Statement stmt = DatabaseHandler.getStatement();
            if (stmt != null)
            {
                System.out.println(query);
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (rs.next())
                {
				    for (int i = 1; i <= columnsNumber; i++)
                    {
                        String columnValue = rs.getString(i);
                        list.add(columnValue);
                    }
                }
                return list;
            }
            else
             return null;
        }
        catch(Exception e)
        {
            Logger.wtf(e.getStackTrace().toString());
            return null;
        }
    }

    public static void main(String[] args)
    {
        DatabaseHandler dh = new DatabaseHandler("client.db");
        String[] columnName1={"id","emailId","password","addOn","updateOn"};
        String[] columnType1={"INTEGER","TEXT","INTEGER","TEXT","TEXT"};
        dh.createTable("USERS",columnName1,columnType1,false,null,null,null,null);

        String[] columnName2={"id","path"};
        String[] columnType2={"INTEGER","TEXT"};
        dh.createTable("FILEPATHS",columnName2,columnType2,false,null,null,null,null);

        String[] columnName3={"id","name","addOn"};
        String[] columnType3={"INTEGER","TEXT","TEXT"};
        dh.createTable("BATCHS",columnName3,columnType3,false,null,null,null,null);

        String[] columnName4={"id","batchName","userName"};
        String[] columnType4={"INTEGER","TEXT","TEXT"};
        dh.createTable("BATCHUSERS",columnName4,columnType4,true,"name","emailId","BATCHS","USERS");

        String[] columnName5={"id","batchName","filePathName"};
        String[] columnType5={"INTEGER","TEXT","TEXT"};
        dh.createTable("BATCHFILESANDFOLDERS",columnName5,columnType5,true,"name","path","BATCHS","FILEPATHS");

    }

}
