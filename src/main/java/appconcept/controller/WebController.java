package appconcept.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import appconcept.dao.Detail;
import appconcept.dao.Employee;
import appconcept.dao.Team;
import appconcept.services.BasicService;

@Controller
public class WebController {

	@Autowired
	private BasicService basicService;

	@GetMapping("/employees")												//initial page Emps & Teams
	public String getAllEmployees(Model model) {
		List<Employee> list = basicService.getEmployees();
		model.addAttribute("employees", list);
		List<Team> listTeam = basicService.getTeams();
		model.addAttribute("teams", listTeam);
		return "empindex";
	}

	@GetMapping("/empdetail")												//load Detail for Employee
	public String details(@RequestParam("empid") int id, Model model) {
		Employee emp = basicService.getEmployee(id);
		if (emp==null) {
			model.addAttribute("error","Employee with id "+id+" not found.");
			return "invalid-request";
		}
		model.addAttribute("employee", emp);
		Detail detail = basicService.getDetails(emp.getDetail().getId());
		model.addAttribute("detail", detail);
		return "empdetail";
	}
	
	@GetMapping("/detailupd")												//edit Detail - initialization (adding model attributes)
	public String detailsUpdate(@RequestParam("detailid") int id, Model model) {
		Detail detail = basicService.getDetails(id);
		Employee emp = detail.getEmployee();
		model.addAttribute("detail", detail);
		model.addAttribute("id", emp.getId());
		model.addAttribute("employee", emp);
		return "detailupd";
	}
	
	
	@RequestMapping("/updatedet")											//processing edited Detail 
	public String detailsUpdateJob(@ModelAttribute("detail") Detail detail, @RequestParam("empid") int id) {
		detail.setEmployee(basicService.getEmployee(id));
		basicService.saveDetails(detail);
		return "redirect:/employees";
	}
	
	@GetMapping("/empedit")													//edit Employee - initialization
	public String editEmployee(@RequestParam("empid") int id, Model model) {
		Employee employee = basicService.getEmployee(id);
		model.addAttribute("employee", employee);
		model.addAttribute("id", String.valueOf(employee.getTeam().getId()));
		model.addAttribute("detailid", employee.getDetail().getId());
		List<Team> teams = basicService.getTeams();
		model.addAttribute("teams", teams);
		return "empedit";
	}
	
	@GetMapping("/addemployee")												//NEW Employee - initialization
	public String addemployee(Model model) {
		Employee employee = new Employee();
		employee.setId(0);													//setting up "PK" for Employee before persisting via saveOrUpdate();
		model.addAttribute("teams", basicService.getTeams());
		model.addAttribute("employee", employee);
		model.addAttribute("detailid", 0);									//setting up "PK" for Detail (for the newly created Employee) before persisting via saveOrUpdate();
		return "empedit";
	}
	
	
	@RequestMapping("empupdate")											//processing edited Employee - update if existing | initialize object & set Team before persisting
	public String empupdate(@RequestParam("teamid") int teamId, @RequestParam("detailid") int detailId, @Valid @ModelAttribute("employee") Employee employee, BindingResult br, Model model) {
		if (br.hasErrors()) {
			if (employee.getId()==0) {										//new employee
				employee.setId(0);
				model.addAttribute("employee", employee);
				model.addAttribute("detailid", 0);
				model.addAttribute("teams", basicService.getTeams());
				return "empedit";
			}
			else {
				Employee newEmployee = basicService.getEmployee(employee.getId());
				model.addAttribute("employee", newEmployee);
				model.addAttribute("detailid", detailId);
				model.addAttribute("id", String.valueOf(newEmployee.getTeam().getId()));
				List<Team> teams = basicService.getTeams();
				model.addAttribute("teams", teams);
				return "empedit";
			}
		}
		
		if (detailId==0) {													//if no Detail assigned to Employee - persist Employee and move to definition of new Detail
			employee.setTeam(basicService.getTeam(teamId));
			int empId=basicService.saveEmployee(employee);
			Detail detail = new Detail();
			model.addAttribute("detail", detail);
			model.addAttribute("id",empId);
			return "detailupd";												//if new employee (ID==0), move to define Details
		}
		employee.setDetail(basicService.getDetails(detailId));
		employee.setTeam(basicService.getTeam(teamId));
		basicService.saveEmployee(employee);
		return "redirect:/employees";
	}
	
	
	@GetMapping("/empremove")												//remove Employee, cascade.all for corresponding Detail 
	public String removeEmployee(@RequestParam("empid") int id, Model model) {
		basicService.removeEmployee(id);
		return "redirect:/employees";
	}

	@RequestMapping("/teamcreate")											//NEW Team - initialization
	public String createNewTeam(Model model) {
		Team team = new Team();
		team.setId(0);
		model.addAttribute("team", team);
		return "teamedit";
	}
	
	@RequestMapping("/teamedit")											//Team - initialization
	public String editCreatedTeam(@RequestParam("teamid") int id, Model model) {
		model.addAttribute("team", basicService.getTeam(id));
		return "teamedit";
	}
	
	@RequestMapping("/processteam")											//processing edited Team
	public String processTeam(@Valid @ModelAttribute("team") Team team, BindingResult br, Model model) {
		if (br.hasErrors()) {
				if (team.getId()==0) {										//failed @Valid of a new team addition, returning empty Team with id 0
					model.addAttribute("team", team);
					return "teamedit";
				}
				else {														//failed @Valid when editing existing team, returning existing team to validate - will cause error response to not be shown
					model.addAttribute("team", team);
					return "teamedit";
				}
		}
		basicService.saveTeam(team);										//validation successful, persisting
		return "redirect:/employees";
	}
	
	
	@RequestMapping("/removeTeam")
	public String removeTeam(@RequestParam("teamId") int id, Model model) {
		if (id==12) {
			model.addAttribute("error", "Team Unassigned must not be removed.");
			return "invalid-request";
		}
		basicService.removeTeam(id);
		return "redirect:/employees";
		
	}
	
}