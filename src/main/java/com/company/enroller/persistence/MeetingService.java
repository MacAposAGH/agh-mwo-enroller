package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query<Meeting> query = connector.getSession().createQuery(hql, Meeting.class);
        return query.list();
    }

    public boolean exists(Meeting meeting) {
        if (findById(meeting.getId()) == null) {
            return false;
        }
        String hql = "FROM Meeting m WHERE m.title = ?1 AND m.date = ?2";
        Query<Meeting> query = connector.getSession().createQuery(hql, Meeting.class);
        query.setParameter(1, meeting.getTitle());
        query.setParameter(2, meeting.getDate());
        return query.uniqueResult() != null;
    }

    public Meeting findById(Long id) {
        return connector.getSession().get(Meeting.class, id);
    }

    public Meeting findById(String id) {
        return findById(Long.parseLong(id));
    }

    public Meeting addMeeting(Meeting meeting) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(meeting);
        transaction.commit();
        return meeting;
    }

    public void updateMeeting(String id, Meeting participant) {
        participant.setId(Long.parseLong(id));
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(participant);
        transaction.commit();
    }

    public void deleteMeeting(Meeting participant) {
        Session session = connector.getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(participant);
        transaction.commit();
    }

}
