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
    public static boolean addUser(Object[] values)
    {
       return DatabaseHandler.createInsertQuery("USERS", values);
    }

    /*
        it will add the batches in the roles table and create two more table
    */
    public static boolean addRoles(String email_id,long password,String roles)
    {
        String[] values={email_id,password+"",roles};
        return DatabaseHandler.createInsertQuery("ROLES", values);
    }

    /*
        it will use to change the password of the user
    */
    public static boolean updateUserPassword(String oldvalue,String newvalue)
    {
        return DatabaseHandler.createUpdateQuery("USERS","password","email_id",oldvalue,newvalue);
    }

    /*
        it is use to delete the users
    */
    public static boolean deleteUser(String valueforcondition)
    {
        return DatabaseHandler.createDeleteQuery("USERS","email_id",valueforcondition);
    }

    /*
        it is use to have the list of values of the given table_name
    */
    public  static ArrayList getValuesOfTable(String table_name)
    {
        if(DatabaseHandler.createSeleteQuery(table_name,null,null))
            return DatabaseHandler.list;
        else
            return null;
    }

    /*
        add the file paths in the table_name
    */
    public static boolean addFilePath(String table_name,long id,String path)
    {
        String patharray[]={id+"",path};
        return DatabaseHandler.createInsertQuery(table_name,patharray);
    }
    /*
        deletion of path
    */
    public static boolean deleteFilePath(String table_name,long id)
    {
        return DatabaseHandler.createDeleteQuery(table_name,"id",id+"");
    }

    /*
        add the users in batchUSERS
    */
    /*
        remove users in batchUSERS
    */
    /*
        update roles in user
    */

    public static void main(String[] args)
    {
        try
        {
            /*if(verifyUser("aaa"))
                System.out.println("user is vaid");
            else
                System.out.println("user is not valid");
            String abc[] = {"1234", "ghi","abc"};
            if(addUser(abc))
                System.out.println("Succesfully added");
            else
                System.out.println("not Succesfully added");
            if(updateUserPassword("aaa","abcde"))
                System.out.println("Successfully updated");
            else
                System.out.println("not Successfully updated");
            if(deleteUser("aaa"))
            System.out.println("Successfully deleted");
            else
            System.out.println("not Successfully deleted");
            String[] abc ={"html1","123"};
            if(addRoles(abc))
                System.out.println("true");
            else
                System.out.println("false");
            if(addFilePath("html1","c:"))
                System.out.println("true");
            else
                System.out.println("false");
            ArrayList list = new ArrayList();
            list = getValuesOfFilePath("html1");
            for(int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            if(deletePath("html1",2))
                System.out.println("true");
            else
                System.out.println("false");*/
            /*if(addFilePath("html1",123456789,"c:\\"))
                System.out.println("true");
            else
                System.out.println("false");*/
            /*Object abc[] = {"jaynam1234", 0xe123a,"abc"};
            if(addUser(abc))
                System.out.println("Succesfully added");
            else
                System.out.println("not Succesfully added");*/
            if(verifyUser("jaynam1234",0xe123a))
                System.out.println("Succesfully verified");
            else
                System.out.println("not Succesfully verified");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
