package ui.core.services;

import ui.core.services.microservices.Comms;
/*
* @author Rohan
* This interface will be implemented by the class which manages the AuthenticatorQueue(Authenticator).
* This interface has 2 functions
*   1.addToAuthenticator
*       places a comms object in the authenticator list
*   2.removeFromAuthenticator
*       removes a comms from the list
* */
public interface AuthenticatorQueMgr {
    public void addToAuthenticator(Comms comms, ClientsToBeServed clientToBeRemoved);
    public void removeFromAuthenticator(Comms client);

}
