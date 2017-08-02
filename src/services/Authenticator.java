package services;

import services.microservices.Comms;

import java.util.ArrayList;

public class Authenticator implements AuthenticatorQueMgr{


    private ArrayList<Comms> authenticatedUsers;

    public Authenticator(){
        this.authenticatedUsers=new ArrayList<>();
    }

    @Override
    public void addToAuthenticator(Comms comms){
        this.authenticatedUsers.add(comms);
    }

    @Override
    public void removeFromAuthenticator(Comms comms){
        this.authenticatedUsers.remove(comms);
    }


}
