package appconcept.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DetailDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	//get Detail from DB via Employee object
	public Detail getDetails(Employee emp) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query<Detail> query = session.createQuery("FROM Detail det WHERE det.employee=:emp", Detail.class);
		query.setParameter("emp", emp);
		Detail detail = query.getSingleResult();		// no try block, Detail ID handled internally by app
		session.getTransaction().commit();
		return detail;
	}
	
	//get Detail from DB via id/DB PK
	public Detail getDetails(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Detail detail = session.get(Detail.class, id);
		session.getTransaction().commit();
		return detail;
	}
	
	//persist Detail object
	public int saveDetails(Detail detail) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Detail temp = (Detail) session.merge(detail);
		session.getTransaction().commit();
		return temp.getId();
	}
	
}
