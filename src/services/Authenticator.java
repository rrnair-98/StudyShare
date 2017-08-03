package services;

import services.microservices.Comms;

import java.net.Socket;
import java.util.ArrayList;

public class Authenticator implements AuthenticatorQueMgr{


    private ArrayList<ClientsToBeServed> clientsToBeServed;
    private ArrayList<Comms> authenticatedUsers;

    public Authenticator(){
        this.authenticatedUsers=new ArrayList<>();
        this.clientsToBeServed=new ArrayList<>();
        Comms.setQueMgr(this);
    }



    public void addClient(Socket socket){
        ClientsToBeServed clientsToBeServed=new ClientsToBeServed(socket,Authenticator.this);
        this.clientsToBeServed.add(clientsToBeServed);
    }

    public ArrayList<ClientsToBeServed> getClientsToBeServed() {
        return this.clientsToBeServed;
    }


    public ArrayList<Comms> getAuthenticatedUsers() {
        return this.authenticatedUsers;
    }

    /* Overriding AuthenticatorQueMgr methods
        * */
    @Override
    public void addToAuthenticator(Comms comms,ClientsToBeServed clientToBeRemoved){
        this.authenticatedUsers.add(comms);
        this.clientsToBeServed.remove(clientToBeRemoved);
    }

    @Override
    public void removeFromAuthenticator(Comms comms){
        this.authenticatedUsers.remove(comms);
    }


}
