package telran.cars.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static telran.cars.dto.RentCompanyConstants.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import telran.cars.dto.Car;
import telran.cars.dto.CarsReturnCode;
import telran.cars.dto.ClearFactor;
import telran.cars.dto.DatesForRent;
import telran.cars.dto.DatesForReturn;
import telran.cars.dto.Driver;
import telran.cars.dto.Model;
import telran.cars.dto.RecordsByDates;
import telran.cars.dto.RentRecord;
import telran.cars.model.IRentCompany;
import telran.cars.model.RentCompanyEmbedded;
import telran.utils.Persistable;

@RestController
@ManagedResource
public class RentCompanyController {

	@Value("${finePercent:15}")
	private int finePercent;
	@Value("${gasPrice:10}")
	private int gasPrice;
	
	
	@Autowired
	IRentCompany rentCompany;
	
	@PostConstruct
	public void stateParam() {
		setFinePercent(finePercent);
		setGasPrice(gasPrice);
	}	
	
	@ManagedAttribute	
	public int getFinePercent() {
		return rentCompany.getFinePercent();
	}
	@ManagedAttribute
	public void setFinePercent(int finePercent) {
		this.finePercent = finePercent;
		rentCompany.setFinePercent(finePercent);
	}
	@ManagedAttribute
	public int getGasPrice() {
		return rentCompany.getGasPrice();
	}
	@ManagedAttribute
	public void setGasPrice(int gasPrice) {
		this.gasPrice = gasPrice;
		rentCompany.setGasPrice(gasPrice);
	}

	@PostMapping(value = ADD_MODEL)
	public CarsReturnCode addModel(@RequestBody Model model) {
		
		return rentCompany.addModel(model);
	}
	
	@GetMapping(value = GET_MODEL + "/{modelName}" )
	public Model getModel(@PathVariable("modelName") String modelName) {
		return rentCompany.getModel(modelName);
	}
	
	@PostMapping(value = ADD_CAR)
	public CarsReturnCode addModel(@RequestBody Car car) {
		return rentCompany.addCar(car);
	}
		
	@GetMapping(value = GET_CAR + "/{carNumber}" )
	public Car getCar(@PathVariable("carNumber") String carNumber) {
		return rentCompany.getCar(carNumber);
	}
	
	@PostMapping(value = ADD_DRIVER)
	public CarsReturnCode addModel(@RequestBody Driver driver) {
		return rentCompany.addDriver(driver);
	}
		
	@GetMapping(value = GET_DRIVER + "/{licenseId}" )
	public Driver getDriver(@PathVariable("licenseId") Long licenseId) {
		return rentCompany.getDriver(licenseId);
	}
	
	@PostMapping(value = RENT_CAR)
	public CarsReturnCode rentCar(@RequestBody DatesForRent rent) {
		return rentCompany.rentCar(rent.getCarNumber(), rent.getDriverId(), rent.getRentDate(), rent.getDays());
	}
	
	@PostMapping(value = RETURN_CAR)
	public CarsReturnCode returnCar(@RequestBody DatesForReturn ret) {
		return rentCompany.returnCar(ret.getCarNumber(), ret.getReturnDate(), ret.getGasTankPercent(), ret.getDamages());
	}
	
	@DeleteMapping(value = REMOVE_CAR + "/{carNumber}")
	public CarsReturnCode removeCar(@PathVariable("carNumber") String carNumber) {
		return rentCompany.removeCar(carNumber);
	}
	
	@GetMapping(value = GET_ALL_DRIVERS)
	public List<Driver> getAllDrivers(){
		return rentCompany.getAllDrivers().collect(Collectors.toList());
	}
	
	@GetMapping(value = GET_ALL_MODELS)
	public List<String> getAllModelName(){
		return rentCompany.getAllModelNames();
	}
	
	@GetMapping(value = GET_ALL_CARS)
	public List<Car> getAllCars(){
		return rentCompany.getAllCars().collect(Collectors.toList());
	}
	
	@GetMapping(value = GET_CAR_DRIVERS + "/{carNumber}")
	public List<Driver> getCarDrivers(@PathVariable("carNumber") String carNumber){
		return rentCompany.getCarDrivers(carNumber);
	}
	
	@GetMapping(value = GET_DRIVER_CARS + "/{licenseId}")
	public List<Car> getDriverCars(@PathVariable("licenseId") long licenseId){
		return rentCompany.getDriverCars(licenseId);
	}
	
	@GetMapping(value = GET_ALL_RECORDS)
	public List<RentRecord> getAllRecords(){
		return rentCompany.getAllRecords().collect(Collectors.toList());
	}
	
	@GetMapping(value = GET_MODEL_PROFIT + "/{modelName}")
	public double getModelProfit(@PathVariable("modelName") String modelName) {
		return rentCompany.getModelProfit(modelName);
	}
	
	@GetMapping(value = GET_MOST_PROFITABLE_MODEL_NAMES)
	public List<String> getMostProfitModelNames(){
		return rentCompany.getMostPopularModelNames();
	}
	
	@GetMapping(value = GET_MOST_POPULAR_MODEL_NAMES)
	public List<String> getMostPopularModelNames(){
		return rentCompany.getMostPopularModelNames();
	}
	
	@PostMapping(value = CLEAR)
	public List<Car> clear(@RequestBody ClearFactor cf){
		return rentCompany.clear(cf.getCurrentDate(), cf.getDays());
	}
	
	@PostMapping(value = GET_RETURNED_RECORDS_BY_DATES)
	public List<RentRecord> getReturnedRecordsByDates(@RequestBody RecordsByDates data){
		return rentCompany.getReturnedRecordsByDates(data.getFrom(), data.getTo()).collect(Collectors.toList());
	}
	
	
	
	    
    
	
	
    
    
	
	
	
  
	
	
	
	
	

}
