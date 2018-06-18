package talentshow.service;


import talentshow.entities.Participant;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IClient extends Remote {
    void notaAdaugata(Participant[] par) throws ConfException, RemoteException;
}
