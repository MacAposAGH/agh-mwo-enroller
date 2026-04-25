package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll(Optional<String> sortBy, Optional<String> sortOrder, Optional<String> key) {
        String hql = "FROM Participant p ";
        if (key.isPresent()) {
            hql += "WHERE p.login LIKE %" + key.get() + "% ";
        }

        if (sortBy.isPresent()) {
            String order = sortOrder.map(String::toUpperCase)
                    .filter(o -> o.equals("ASC") || o.equals("DESC"))
                    .orElse("ASC");
            hql += "ORDER BY p." + sortBy.get() + " " + order;
        }

        Query<Participant> query = connector.getSession().createQuery(hql, Participant.class);
        return query.list();
    }

    public Collection<Participant> getAll() {
        String hql = "FROM Participant";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public Participant findByLogin(String login) {
        return connector.getSession().get(Participant.class, login);
    }

    public Participant addParticipant(Participant participant) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(participant);
        transaction.commit();
        return participant;
    }

    public void updateParticipant(String login, Participant participant) {
        participant.setLogin(login);
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(participant);
        transaction.commit();
    }

    public void deleteParticipant(Participant participant) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(participant);
        transaction.commit();
    }

}
