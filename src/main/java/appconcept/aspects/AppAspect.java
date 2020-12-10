package appconcept.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import appconcept.dao.Employee;
import appconcept.dao.Team;

@Aspect
@Component
public class AppAspect {
	
	@Pointcut("execution(* save*(..))")
	public void newOrUpdatePointcut() {}
	
	@Before("newOrUpdatePointcut()")
	public void newEmployeeAdded(JoinPoint jp) {
		Object[] args = jp.getArgs();
		if (args[0] instanceof Team) {
			System.out.println("@Before aspect caught saving of a Team");
		}
		else if (args[0] instanceof Employee) {
			System.out.println("@Before aspect caught saving of an Employee");
		}
	}
	
	
	@AfterReturning(pointcut="execution(* delete*(..))", returning = "result")
	public void logRemoval(JoinPoint jp, Object result) {
		
	}
	
	
}
