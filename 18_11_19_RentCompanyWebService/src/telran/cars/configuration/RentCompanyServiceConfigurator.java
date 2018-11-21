package telran.cars.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedResource;

import telran.cars.model.IRentCompany;
import telran.cars.model.RentCompanyEmbedded;
import telran.utils.Persistable;

@Configuration
@ManagedResource
public class RentCompanyServiceConfigurator {
	
	@Value(("${nameFile:rentcompany.data}"))
	private String nameFile;
	
		
	@Autowired
	IRentCompany rentCompany;
	
	@Bean
	public IRentCompany getRentCompanyService() {
//		return RandomRentCompanyCreation.getRandomCompany();
		return RentCompanyEmbedded.restoreFromFile(nameFile);
		 
	}
	
		
	@PreDestroy
	void saveToFileRentCompany() {
		if(rentCompany instanceof Persistable)
		((RentCompanyEmbedded) rentCompany).saveToFile(nameFile);
	}
	
	
	

}
