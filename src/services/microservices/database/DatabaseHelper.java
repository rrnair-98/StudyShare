package services.microservices.database;

import services.microservices.database.corefunction.DatabaseHandler;

import java.util.ArrayList;

/*
    author:jaynam
*/
public class DatabaseHelper

{
    /*
        verifyUser is use to check if the user has given the right passwod or not
    */
    public static boolean verifyUser(String email_id,long password)
    {
        return DatabaseHandler.createVerifyQuery("USERS",email_id,password);

    }

    /*
        it will add the user in user table
    */
    public static boolean addUser(String email_id,long password_hash)
    {
        Object[] values={email_id,password_hash};
        return DatabaseHandler.createInsertQuery("USERS", values);
    }

    /*
        it will add the batches in the roles table and create two more table
    */
    public static boolean addRoles(long role_hash,String role_name,String last_access)
    {
        Object[] values={role_hash,role_name,last_access};
        return DatabaseHandler.createInsertQuery("ROLES", values);
    }

    /*
        add the file paths in the table_name
    */
    public static boolean addFilePath(String table_name,long path_hash,String path)
    {
        Object[] detail={path_hash,path};
        return DatabaseHandler.createInsertQuery(table_name,detail);
    }

    /*
        add the users in batchUSERS
    */
    public static boolean addBatchUser(String table_name,String email_id)
    {
        Object[] detail={email_id};
        String tname=table_name+"USERS";
        return DatabaseHandler.createInsertQuery(tname,detail);
    }

    /*
        it will use to change the password of the user
    */
    public static boolean updateUserPassword(Object new_pasword,Object email_id)
    {
        return DatabaseHandler.createUpdateQuery("USERS","password",new_pasword,"email_id",email_id);
    }

    /*update last access*/
    public static boolean updateLastAccess(Object new_access,Object table_name)
    {
        return DatabaseHandler.createUpdateQuery("ROLES","lastaccess",new_access,"tablename",table_name);
    }
    /*
        it is use to delete the users
    */
    public static boolean deleteUser(String email_id)
    {
        return DatabaseHandler.createDeleteQuery("USERS","email_id",email_id);
    }

    /*
        deletion of path
    */
    public static boolean deleteFilePath(String table_name,Object id)
    {
        return DatabaseHandler.createDeleteQuery(table_name,"id",id.toString());
    }


    /*
        delete users in batchUSERS
    */
    public static boolean deleteBatchUser(String table_name,String email_id)
    {
        return DatabaseHandler.createDeleteQuery(table_name+"USERS","user_email_id",email_id);
    }

    /*delete role*/
    public static boolean deleteRoles(String role_name)
    {
        if(DatabaseHandler.createDeleteQuery("ROLES","tablename",role_name))
        {
            DatabaseHandler.createDropQuery(role_name);
            DatabaseHandler.createDropQuery(role_name+"USERS");
            return true;
        }
        else
            return false;
    }
    /*
        it is use to have the list of values of the given table_name
    */
    public  static ArrayList getValuesOfTable(String table_name)
    {
        if(DatabaseHandler.createSelectQuery(table_name,null,null))
            return DatabaseHandler.list;
        else
            return null;
    }

    public static ArrayList getFilePaths(String table_name)
    {
        ArrayList<String> paths = new ArrayList<String>();
        paths=DatabaseHandler.getFilePaths(table_name);
        return paths;

    }

    public static void main(String[] args)
    {
        try
        {
            /*if(addRoles(0x52e2,"java","12:12:12"))
                System.out.println("true");
            else
                System.out.println("false");
            if(addUser("abc123@gmail.com",0x123))
                System.out.println("true");
            else
                System.out.println("false");
            if(addUser("abc@gmail.com",0x456))
                System.out.println("true");
            else
                System.out.println("false");
            if(addBatchUser("java","abc@gmail.com"))
                System.out.println("true");
            else
                System.out.println("false");
            if(addBatchUser("java","abc123@gmail.com"))
                System.out.println("true");
            else
                System.out.println("false");

            if(addFilePath("java",0x563,"d:\\;"))
                System.out.println("true");
            else
                System.out.println("false");
            if(addFilePath("java",0x564,"e:\\;"))
                System.out.println("true");
            else
                System.out.println("false");*/

            ArrayList<String> paths=new ArrayList<String>();
            paths=getFilePaths("java");
            for(int i=0;i<paths.size();i++)
                System.out.println(paths.get(i));
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
