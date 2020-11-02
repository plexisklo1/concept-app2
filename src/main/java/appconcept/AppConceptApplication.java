package appconcept;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import appconcept.dao.Detail;
import appconcept.dao.Employee;
import appconcept.dao.Team;

@SpringBootApplication(scanBasePackages = "appconcept")
@PropertySource("classpath:db.prop")
public class AppConceptApplication implements WebMvcConfigurer {

	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(AppConceptApplication.class, args);
	}
		
	@Bean
	protected ViewResolver viewResolver() {
		InternalResourceViewResolver view = new InternalResourceViewResolver();
		view.setPrefix("/WEB-INF/view/");
		view.setSuffix(".jsp");
		return view;
	}
	
	@Bean
	public DataSource dataSource() {
		ComboPooledDataSource source = new ComboPooledDataSource();
		try {
		source.setDriverClass(env.getProperty("hibernate.connection.driver_class"));
		source.setContextClassLoaderSource(env.getProperty("hibernate.current_session_context_class"));
		System.out.println("data source try block");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		source.setUser(env.getProperty("hibernate.connection.username"));
		source.setPassword(env.getProperty("hibernate.connection.password"));
		source.setJdbcUrl(env.getProperty("hibernate.connection.url"));
		source.setInitialPoolSize(Integer.parseInt(env.getProperty("hibernate.connection.initialPoolSize")));
		source.setMaxIdleTime(Integer.parseInt(env.getProperty("hibernate.connection.maxIdleTime")));
		source.setMinPoolSize(Integer.parseInt(env.getProperty("hibernate.connection.minPoolSize")));
		source.setMaxPoolSize(Integer.parseInt(env.getProperty("hibernate.connection.maxPoolSize")));
		System.out.println("data source created");
		return source;
		
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFActory() {
		LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
		factory.setDataSource(dataSource());
		Properties props= new Properties();
		props.setProperty("hibernate.show_sql", "true");
		factory.setHibernateProperties(props);
		factory.setAnnotatedClasses(Employee.class,Team.class,Detail.class);
		
		System.out.println("session factory created");
		return factory;
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
	}
	
	

}
