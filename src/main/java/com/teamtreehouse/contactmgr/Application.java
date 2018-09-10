package com.teamtreehouse.contactmgr;

import com.teamtreehouse.contactmgr.model.Contact;
import com.teamtreehouse.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class Application {

    private static final SessionFactory sessionFactory = buildSessionFactiory();

    private static SessionFactory buildSessionFactiory(){
        final ServiceRegistry  registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {

        Contact contact = new ContactBuilder("Alex", "Kremnev")
                .withEmail("alkremn@gmail.com")
                .withPhone(3952218919L)
                .build();

        //int id = save(contact);


        fetchAllContacts().forEach(System.out::println);

        //Contact c = findContactById(id);

        //c.setFirstName("Ben");
        delete(contact);

        fetchAllContacts().forEach(System.out::println);

    }

    private static Contact findContactById(int id){

        Session session = sessionFactory.openSession();

        Contact contact = session.get(Contact.class, id);

        session.close();

        return contact;
    }

    private static void update(Contact contact){

        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.update(contact);


        session.getTransaction().commit();

        session.close();

    }

    private static void delete(Contact contact){
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.delete(contact);


        session.getTransaction().commit();

        session.close();
    }

    @SuppressWarnings("unchecked")
    public static List<Contact> fetchAllContacts(){

        Session session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);

        criteria.from(Contact.class);

        List<Contact> contacts = session.createQuery(criteria).list();
        session.close();

        return contacts;
    }


    public static int save(Contact contact){
        //Open a transaction
        Session session = sessionFactory.openSession();

        //Begin a transaction
        session.beginTransaction();

        //Use the session to save the contact
        int id = (int)session.save(contact);

        //Commit the transaction
        session.getTransaction().commit();

        //Close the session
        session.close();

        return id;
    }
}
