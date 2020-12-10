package appconcept;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages="appconcept") //,appconcept.controller,appconcept.dao,appconcept.exceptions,appconcept.services,appconcept.aspects
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	private DataSource dataSource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		UserBuilder users = User.withDefaultPasswordEncoder();
//		
//		auth.inMemoryAuthentication().
//			withUser(users.username("user").password("nbusr123").roles("USER")).
//			withUser(users.username("exec").password("nbusr123").roles("EXEC")).
//			withUser(users.username("admin").password("nbusr123").roles("ADMIN","EXEC","EMPLOYEE"));
		
		auth.jdbcAuthentication().dataSource(dataSource);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	 	http.authorizeRequests()  
	 	.antMatchers("/css/**").permitAll()				//enable access to css without authentication
	 	.antMatchers("/api/**").permitAll()
	 	.antMatchers("/teamcreate").hasAnyRole("ADMIN","EXEC")
	 	.antMatchers("/addEmployee").hasAnyRole("ADMIN","EXEC")
        .anyRequest().authenticated()                   
        .and().formLogin()
	 	.loginPage("/login")                            
	 	.loginProcessingUrl("/authenticate") 
	 	.defaultSuccessUrl("/employees", true)
	 	.permitAll()                                   //permit everybody to see the login window
	 	.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	 	.permitAll()
	 	.and()
	 	.exceptionHandling().accessDeniedPage("/denied");
	}
	
	
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/api");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/css/**").addResourceLocations("/css/");
	}
	
	
}
