package appconcept.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TeamDAO {

	@Autowired
	private SessionFactory sessionFactory;

	// get Team from DB via id/DB PK
	public Team getTeam(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Team team = session.get(Team.class, id);
		session.getTransaction().commit();
		return team;
	}

	// get all Team records from DB
	public List<Team> getTeams() {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query<Team> query = session.createQuery("FROM Team", Team.class);
		List<Team> list = query.getResultList();
		session.getTransaction().commit();
		return list;
	}

	// persist Team
	public Team saveTeam(Team team) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		session.saveOrUpdate(team);
		session.getTransaction().commit();
		return team;
	}

	// remove Team - unimplemented
	public void removeTeam(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		Team team = session.get(Team.class,id);
		session.remove(team);
		session.getTransaction().commit();
	}
}
