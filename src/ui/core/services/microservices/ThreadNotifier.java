package ui.core.services.microservices;
/*
*@author Rohan
*This interface will be used by all classes that want to do something whenever the Comms,FileReaderRunnables thread gets scheduled for execution
* */
public interface ThreadNotifier {
    public void onPreExecute(String username);
    public void onPostExecute(Object[] object);

}
