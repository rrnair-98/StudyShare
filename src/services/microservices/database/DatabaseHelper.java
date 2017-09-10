package services.microservices.database;

import services.microservices.database.corefunction.DatabaseHandler;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import services.microservices.utilities.Housekeeper;

/*
    author:jaynam
*/

/*
    return 1 means true(all happen successfully)
    and return 0 means false(something went wrong)

    to run the program you need to first call init methode and from then you can use any methode

    -all the method are public static

    ----------------return type int--------------------------------------------
    1-addUserIntoUSERSTable(String email,long password)
    2-updateUserPassword(String email,long oldPassword,long newPassword)
    3-deleteUserFromUSERSTable(String emailId)
    4-addBatchIntoBATCHSTable(String name)
    5-deleteBatchFromBATCHSTable(String name)
    6-addPathIntoFILEPATHSTable(String path)
    7-deletePathFromFILEPATHSTable(String path)
    8-addBatchUser(String batchName,String userName)
    9-deleteUserFromBATCHUSERS(String userName)
    10-addBATCHFILEANDFOLDER(String batchName,String filePathName)
    11-deletePathFromBATCHFILEANDFOLDER(String path)
    12-verifyUser(String emailId,long password)

    ---------return type arraylist-------------------------------------------
    13-readBatchName()
    14-readUsersName()
    15-readUsersName(String batchName)

*/
public class DatabaseHelper
{
    private  static DatabaseHandler dh;
    private  static boolean isInited=false;

    public static void init(String databasePath)
    {
        /*
            this methode is use for initialization
            it should be called once before execution
        */
        if(DatabaseHelper.isInited)
            throw  new IllegalStateException("init has already been called");
        DatabaseHelper.isInited=true;
        DatabaseHelper.dh=new DatabaseHandler(databasePath);
    }

    public static int addUserIntoUSERSTable(String email,long password)
    {
        /*
            it will add user into its table,you just need to pass the emalId and password
        */
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        System.out.println(df.format(dateobj));
        String key=email+password;
        Long id=Housekeeper.getMD5(key);
        String query="INSERT INTO USERS(id,emailId,password,addOn,updateOn) VALUES("+id+",\'"+email+"\',"+password+",\'"+df.format(dateobj)+"\',\'"+df.format(dateobj)+"\');";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static int updateUserPassword(String email,long oldPassword,long newPassword)
    {
        /*
            it will update the password of the user, you just need to pass emailId,olPasswordHashed and newPasswordHashed
        */
            String tempquery="UPDATE USERS SET password=" + newPassword +" WHERE password="+oldPassword+ ";";
            String query="";
            if(DatabaseHelper.dh.cudOperation(tempquery) == 1)
            {
                DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                Date dateobj = new Date();
                query="UPDATE USERS SET updateOn=\'" + df.format(dateobj) +"\' WHERE password="+newPassword+ ";";
            }
            return DatabaseHelper.dh.cudOperation(query);

    }

    public static int deleteUserFromUSERSTable(String emailId)
    {
        /*
            it will delete the user , you just need to pass emailId
        */
        String query = "DELETE FROM USERS WHERE emailId=\'"+emailId+"\';";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static int addBatchIntoBATCHSTable(String name)
    {
        /*
            it will add batch to its table,you just need to pass name of the batch
        */
        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date dateobj = new Date();
        String key=name;
        Long id=Housekeeper.getMD5(key);
        String query="INSERT INTO BATCHS(id,name,addOn) VALUES("+id+",\'"+name+"\',\'"+df.format(dateobj)+"\');";
        return DatabaseHelper.dh.cudOperation(query);
    }


    public static int deleteBatchFromBATCHSTable(String name)
    {
        /*
            it will delete the batch , you just need to pass the name of batch
        */
        String query = "DELETE FROM BATCHS WHERE name=\'"+name+"\';";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static int addPathIntoFILEPATHSTable(String path)
    {
        /*
            it will all path,you need to pass the path
        */
        String key=path;
        Long id=Housekeeper.getMD5(key);
        String query="INSERT INTO FILEPATHS(id,path) VALUES("+id+",\'"+path+"\');";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static int deletePathFromFILEPATHSTable(String path)
    {
        /*
            it will delete the path from the table, you need to give the path
        */
        String query = "DELETE FROM FILEPATHS WHERE path=\'"+path+"\';";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static int addBatchUser(String batchName,String userName)
    {
        /*
            it will map USER with its Batch,need to pass batchName and userName
        */
        String key=batchName+userName;
        Long id=Housekeeper.getMD5(key);
        String query="INSERT INTO BATCHUSERS(id,batchName,userName) VALUES("+id+",\'"+batchName+"\',\'"+userName+"\');";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static int deleteUserFromBATCHUSERS(String userName)
    {
        /*
            it is use for deletion,need to pass the userName
        */
        String query = "DELETE FROM BATCHUSERS WHERE userName=\'"+userName+"\';";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static int addBATCHFILEANDFOLDER(String batchName,String filePathName)
    {
        /*
            it is use to map FilePath with Batch, need to pass batchName and path
        */
        String key=batchName+filePathName;
        Long id=Housekeeper.getMD5(key);
        String query="INSERT INTO BATCHFILESANDFOLDERS(id,batchName,filePathName) VALUES("+id+",\'"+batchName+"\',\'"+filePathName+"\');";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static int deletePathFromBATCHFILEANDFOLDER(String path)
    {
        /*
            it is use for deletion,need to pass the path
        */
        String query = "DELETE FROM BATCHFILESANDFOLDERS WHERE filePathName=\'"+path+"\';";
        return DatabaseHelper.dh.cudOperation(query);
    }

    public static ArrayList readBatchName()
    {
        /*
            it is you to provide all the batch name
        */
        String query="SELECT name from BATCHS;";
        return DatabaseHelper.dh.readValues(query);
    }

    public static ArrayList readUsersName()
    {
        /*
            it is use to provide all the emailId registerd
        */
        String query="SELECT emailId from USERS;";
        return DatabaseHelper.dh.readValues(query);
    }

    public static ArrayList readUsersName(String batchName)
    {
        /*
            it is use to provide to enroll name for that batchName,you just need to provide batchName
        */
        String query="SELECT userName from BATCHUSERS WHERE batchName=\'"+batchName+"\';";
        return DatabaseHelper.dh.readValues(query);
    }

    public static int verifyUser(String emailId,long password)
    {
        /*
            it is use to verify the user,need to pass emialId and hashed password
        */
        try
        {
            String query = "SELECT count(*) FROM USERS WHERE emailId='" + emailId + "' AND password=" + password + ";";
            ArrayList verify= DatabaseHelper.dh.readValues(query);

            if (((String)verify.get(0)).equals("0"))
                return 0;
            else
                return 1;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public static void main(String[] args)
    {
        try
        {
            init("client.db");
            //addUserIntoUSERSTable("jaynam",0x123);
            //addBatchIntoBATCHSTable("java");
            //addBatchIntoBATCHSTable("dsu");
            //addBatchIntoBATCHSTable("oop");
            //addPathIntoFILEPATHSTable("c:\\def.xyz");
            //addBatchUser("java","jaynam");
            //addBATCHFILEANDFOLDER("java","c:\\def.xyz");
            //deleteUserFromUSERSTable("jaynam");
            //deleteBatchFromBATCHSTable("java");
            //deletePathFromFILEPATHSTable("c:\\def.xyz");
            //deleteUserFromBATCHUSERS("jaynam");
            //deletePathFromBATCHFILEANDFOLDER("c");
            //updateUserPassword("jaynam",0x12,0x123);
            //verifyUser("jaynam",0x123);




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
