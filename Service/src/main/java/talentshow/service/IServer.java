package talentshow.service;


import talentshow.entities.Juriu;
import talentshow.entities.Participant;

import java.rmi.RemoteException;


public interface IServer {

    Juriu login(Juriu user, IClient client) throws ConfException;
    void logout(Juriu user) throws ConfException;
    Participant[] getParticipanti() throws ConfException;
    void adaugaNota(int idOrganizator, int idParticipant, int nota) throws ConfException, RemoteException;
    boolean verificaNota(int idO, int idP) throws ConfException; //returneaza true daca organizatorul idO i-a mai dat nota lui idP

    Juriu getOrganizator(String username, String password);

}
