package appconcept.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import appconcept.dao.Detail;
import appconcept.dao.DetailDAO;
import appconcept.dao.Employee;
import appconcept.dao.EmployeeDAO;
import appconcept.dao.Team;
import appconcept.dao.TeamDAO;

@Service
public class BasicService {

	
	//handling DAO operations
	//
	@Autowired
	private EmployeeDAO employeeDao;
	
	@Autowired
	private DetailDAO detailDao;
	
	@Autowired
	private TeamDAO teamDao;
	
	public BasicService() {}

	
	// EMPLOYEE DB access methods
	//
	public Employee getEmployee(int id) {
		return employeeDao.getEmployee(id);
	}
	
	public int saveEmployee(Employee emp) {
		return employeeDao.saveEmployee(emp);
	}
	
	public void removeEmployee(int id) {
		employeeDao.removeEmployee(id);
	}
	
	public List<Employee> getEmployees() {
		return employeeDao.getEmployees();
	}
	

	
	// DETAIL DB access methods
	//
	public Detail getDetails(Employee emp) {
		return detailDao.getDetails(emp);
	}
	
	
	public Detail getDetails(int id) {
		return detailDao.getDetails(id);
	}
	
	
	public int saveDetails(Detail detail) {
		return detailDao.saveDetails(detail);
	}
	
	
	public void removeDetails(int id) {
		detailDao.removeDetails(id);
	}
	
	
	
	// TEAM DB access methods
	//
	
	public Team getTeam(int id) {
		return teamDao.getTeam(id);
	}
	
	
	public Team saveTeam(Team team) {
		return teamDao.saveTeam(team);
	}
	
	
	public void removeTeam(int id) {
		teamDao.removeTeam(id);
	}
	
	public List<Team> getTeams() {
		return teamDao.getTeams();
	}
	
}
