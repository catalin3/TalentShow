package talentshow.server;


import talentshow.entities.Juriu;
import talentshow.entities.Nota;
import talentshow.entities.Participant;
import talentshow.persistance.JuriuRepository;
import talentshow.persistance.NotaRepository;
import talentshow.persistance.ParticipantRepository;
import talentshow.service.ConfException;
import talentshow.service.IClient;
import talentshow.service.IServer;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dana on 15-Jun-17.
 */

public class ServerImpl implements IServer {

    private Map<String, IClient> loggedClients;
    private JuriuRepository organizatorRepository;
    private ParticipantRepository participantRepository;
    private NotaRepository noteRepository;
    private String username;
    private Juriu userLogat;

    ServerImpl(JuriuRepository organizatorRepository, ParticipantRepository participantRepository, NotaRepository noteRepository){
        this.organizatorRepository=organizatorRepository;
        this.participantRepository=participantRepository;
        this.noteRepository = noteRepository;

        loggedClients=new ConcurrentHashMap<>();
    }

    @Override
    public Juriu login(Juriu user, IClient client) throws ConfException {
        List<Juriu> users=organizatorRepository.getAll();
        for(Juriu u:users){
            if(u.getUsername().equals(user.getUsername())&&u.getPassword().equals(user.getPassword())){
                if (loggedClients.get(user.getUsername()) != null)
                    throw new ConfException("User already logged in.");
                loggedClients.put(user.getUsername(), client);
                username = user.getUsername();
                userLogat = u;
                System.out.println(u.toString());
                return u;
            }
        }
        throw new ConfException("Authentication failed.");
    }

    @Override
    public void logout(Juriu user) throws ConfException {
        loggedClients.remove(user.getUsername());
    }


    @Override
    public Participant[] getParticipanti() throws ConfException {
        return participantRepository.getAll().toArray(new Participant[1]);
    }

    @Override
    public void adaugaNota(int idOrganizator, int idParticipant, int nota) throws ConfException, RemoteException {
        Nota note = new Nota(idOrganizator, idParticipant, nota);
        noteRepository.adaugaNota(note);
        notifyO();
    }

    @Override
    public boolean verificaNota(int idO, int idP) throws ConfException {
        return noteRepository.verificaNota(idO, idP);
    }

    @Override
    public Juriu getOrganizator(String username, String password) {
        List<Juriu> organizatorList = organizatorRepository.getAll();
        for (Juriu organizator : organizatorList){
            if (organizator.getUsername().equals(username) && organizator.getPassword().equals(password))
                return organizator;
        }
        return null;
    }

    private void notifyO() throws ConfException, RemoteException {
        for (String usernamePers : loggedClients.keySet()){
            this.loggedClients.get(usernamePers).notaAdaugata(getParticipanti());
            System.out.println(" am notificat pe " + usernamePers);
        }
    }

}
