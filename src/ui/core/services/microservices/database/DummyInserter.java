package ui.core.services.microservices.database;

import javax.xml.crypto.Data;

public class DummyInserter {
    private static String[] users={
            "Jaynam","Dhanno","Ashu","Kaustubh","Vidhi"
,"Vishal","Rohan","Pratik"    };

    private static String password="helloWorld123";

    private static String[] sub={"java","dsu","oops","c","c++"};
    private static String filePath="C:\\Users\\Dhananjay\\Documents\\java";

    public DummyInserter(){}
    /* inserts users , adds subjects and filepaths to the db*/
    public void hitThatShit(){
        for(String x:DummyInserter.users)
            DatabaseHelper.addBatchUser(x,DummyInserter.password);

        for(String x:DummyInserter.sub)
            DatabaseHelper.addBatchIntoBATCHSTable(x);

        DatabaseHelper.addBATCHFILEANDFOLDER("java",DummyInserter.filePath);


    }

    public static void main(String arp[]){
        new DummyInserter().hitThatShit();
    }





}
