import interfaces.ServerRequestConstants;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class CommsDriver{
    public  void main(String[] args) throws Exception{
        Socket user=new Socket("192.168.0.146",44444);
        Comms c = new Comms(user.getOutputStream(), user.getInputStream());


                c.checkAuthentication("pratik", "291");
                List ls = new ArrayList();
                ls.add("/Users/damianmandrake/Projects/libs/client.db");
                c.setSelectedFilesListToBeRequested(ls);
                c.setServerRequest(ServerRequestConstants.SENDING_LIST);
        BooleanProperty accessible=new SimpleBooleanProperty();
        while(!Comms.downloadStatus);
    }
}
