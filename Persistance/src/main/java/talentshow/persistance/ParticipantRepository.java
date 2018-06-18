package talentshow.persistance;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;
import talentshow.entities.Participant;

import java.util.ArrayList;
import java.util.List;
@Component
public class ParticipantRepository {
    private SessionFactory sessionFactory;

    public ParticipantRepository() {
        try{
            initialize();
            System.out.println(sessionFactory);
        } catch (Exception e){
            close();
        }
    }

    public List<Participant> getAll() {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        List<Participant> rez = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            rez = session.createQuery("FROM Participant", Participant.class).list();
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx != null)
                tx.rollback();
        } finally {
            session.close();
        }
        return rez;
    }

    public Participant getById(int idP) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Participant p = session.get(Participant.class, idP);
            System.out.println("getById ParticipantRepository " + p);
            tx.commit();
            return p;
        }catch (RuntimeException ex){
            if(tx!=null)
                tx.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    private void initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
        }
    }

    private void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
