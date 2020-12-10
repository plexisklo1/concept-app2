package appconcept.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import appconcept.dao.Detail;
import appconcept.dao.Employee;
import appconcept.dao.Team;
import appconcept.exceptions.EmployeeNotFoundException;
import appconcept.exceptions.TeamNotFoundException;
import appconcept.services.BasicService;

@RestController
@RequestMapping("/api")
public class RestControllerOwn {

	@Autowired
	private BasicService basicService;
	
	//retrieving of existing teams via HTTP GET
	@GetMapping("/teams")
	public List<Team> getTeamsRest() {
		return basicService.getTeams();
	}
	
	//retrieving of an existing team via HTTP GET
	@GetMapping("/teams/{idTeam}")
	public Team getTeamRest(@PathVariable("idTeam") int id) {
		Team team = basicService.getTeam(id);
		if (team == null) {
			throw new TeamNotFoundException();
		}
		return team;
	}
	
	//inserting of a new team via HTTP POST
	@PostMapping("/teams")
	public Team addTeam(@Valid @RequestBody Team team, BindingResult br) {
		if (br.hasErrors()) {
			return null;
		}
		team.setId(0);
		basicService.saveTeam(team);
		return team;
	}
	
	//editing of an existing team via HTTP PUT
	@PutMapping("/teams")
	public Team updateTeamRest(@Valid @RequestBody Team team, BindingResult br) {
		if (br.hasErrors()) {
			return null;
		}
		basicService.saveTeam(team);			
		return team;
	}
	
//	------------- Employee REST
	
	//retrieving of existing employees via HTTP GET
	@GetMapping("/employees")
	public List<Employee> getEmployeesRest() {
		return basicService.getEmployees();
	}
	
	//retrieving of an existing employee via HTTP GET
	@GetMapping("/employees/{empId}")
	public Employee getEmployeeRest(@PathVariable("empId") int id) {
		Employee emp = basicService.getEmployee(id);
		if (emp==null) {
			throw new EmployeeNotFoundException("Employee id:"+id+" not found.");
		}
		return basicService.getEmployee(id);
	}
	
	//editing of an existing employee via HTTP PUT
	@PutMapping("/employees")
	public Employee updateEmployeeRest(@Valid @RequestBody Employee emp, BindingResult br) {
		if (br.hasErrors()) {
			return null;
		}
		basicService.saveEmployee(emp);
		return basicService.getEmployee(emp.getId());
	}
	
	//inserting of a new employee via HTTP POST
	@PostMapping("/employees")					//has to include team_id FK, Detail
	public Employee addNewEmployeeRest(@Valid @RequestBody Employee emp, BindingResult br) {
		if (br.hasErrors()) {
			return null;
		}
		emp.setTeam(basicService.getTeam(12));		//no team set in JSON -> throw into default "Unassigned" team
		Detail detail = emp.getDetail();				//assign Employee to created Detail before persisting
		detail.setEmployee(emp);
		Employee tempEmp = basicService.getEmployee(basicService.saveEmployee(emp));
		return tempEmp;
	}
	
	
}
