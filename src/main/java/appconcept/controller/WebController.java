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

	//initial page Emps & Teams
	@GetMapping("/employees")
	public String getAllEmployees(Model model) {
		List<Employee> list = basicService.getEmployees();
		model.addAttribute("employees", list);
		List<Team> listTeam = basicService.getTeams();
		model.addAttribute("teams", listTeam);
		return "empindex";
	}

	//load Detail for Employee
	@GetMapping("/empdetail")
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
	
	//edit Detail - initialization (adding model attributes)
	@GetMapping("/detailupd")
	public String detailsUpdate(@RequestParam("detailid") int id, Model model) {
		Detail detail = basicService.getDetails(id);
		Employee emp = detail.getEmployee();
		model.addAttribute("detail", detail);
		model.addAttribute("id", emp.getId());
		model.addAttribute("employee", emp);
		return "detailupd";
	}
	
	//processing edited Detail
	@RequestMapping("/updatedet") 
	public String detailsUpdateJob(@ModelAttribute("detail") Detail detail, @RequestParam("empid") int id) {
		detail.setEmployee(basicService.getEmployee(id));
		basicService.saveDetails(detail);
		return "redirect:/employees";
	}
	
	//edit Employee - initialization
	@GetMapping("/empedit")
	public String editEmployee(@RequestParam("empid") int id, Model model) {
		Employee employee = basicService.getEmployee(id);
		model.addAttribute("employee", employee);
		model.addAttribute("id", String.valueOf(employee.getTeam().getId()));
		model.addAttribute("detailid", employee.getDetail().getId());
		List<Team> teams = basicService.getTeams();
		model.addAttribute("teams", teams);
		return "empedit";
	}
	
	//new Employee - initialization
	@GetMapping("/addemployee")
	public String addemployee(Model model) {
		Employee employee = new Employee();
		employee.setId(0);													//setting up "PK" for Employee before persisting via saveOrUpdate();
		model.addAttribute("teams", basicService.getTeams());
		model.addAttribute("employee", employee);
		model.addAttribute("detailid", 0);									//setting up "PK" for Detail (for the newly created Employee) before persisting via saveOrUpdate();
		return "empedit";
	}
	
	//processing edited Employee - update if existing | initialize object & set Team before persisting
	@RequestMapping("empupdate")
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
		
		//if no Detail assigned to Employee - persist Employee and move to definition of new Detail
		if (detailId==0) {
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
	
	//remove Employee, cascade.all for corresponding Detail
	@GetMapping("/empremove")
	public String removeEmployee(@RequestParam("empid") int id, Model model) {
		basicService.removeEmployee(id);
		return "redirect:/employees";
	}

	//new Team - initialization
	@RequestMapping("/teamcreate")
	public String createNewTeam(Model model) {
		Team team = new Team();
		team.setId(0);
		model.addAttribute("team", team);
		return "teamedit";
	}
	
	//Team - initialization
	@RequestMapping("/teamedit")
	public String editCreatedTeam(@RequestParam("teamid") int id, Model model) {
		model.addAttribute("team", basicService.getTeam(id));
		return "teamedit";
	}
	
	//processing edited Team
	@RequestMapping("/processteam")
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
			model.addAttribute("error", "Default team \"Unassigned\" can not be removed.");
			return "invalid-request";
		}
		basicService.removeTeam(id);
		return "redirect:/employees";	
	}
	
	//no permission for user account to access this page - redirect to title page
	@RequestMapping("/denied")
	public String denied() {
		return "redirect:/employees";
	}
	
}