package telran.cars.configuration;

import telran.cars.model.*;
import telran.utils.Persistable;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import telran.cars.dto.*;
public class RandomRentCompanyCreation {

	private static final int N_MODELS = 20;
	private static final int N_CARS = 100;
	private static final int N_DRIVERS = 1000;
	private static final int PRICE_DAY_MAX = 50;
	private static final int N_COUNTRIES = 10;
	private static final int N_COMPANIES = 10;
	private static final int GAZ_TANK_MAX = 100;
	private static final int MIN_DRIVER_YEAR = 1950;
	private static final int MAX_DRIVER_YEAR = 2000;
	private static LocalDate DATE_ONE = LocalDate.of(2018, 10, 21);
	private static final LocalDate LAST_DATE = LocalDate.of(2023, 10, 21);
	private static final int MIN_RENT_DAYS = 1;
	private static final int MAX_RENT_DAYS = 14;
	private static final int RENTS_EVERY_DAY = 10;
	private static final int CHANCE_TO_DELAY = 10;
	private static final int CHANCE_TO_FULL_TANK=80;
	private static final int MIN_PERCENT_RETURN_GAS_TANK = 0;
	private static final int MAX_PERCENT_RETURN_GAS_TANK = 90;
	private static final int N_COLORS = 10;
	private static HashMap<LocalDate,List<String>> shouldReturned=
			new HashMap<>();
	
	static Random gen=new Random();
	private static IRentCompany rentCompany= new RentCompanyEmbedded();
//	public static void main(String[] args) {
//		
//		fillModelsCarsDrivers();
//		RentCompanyImmitation();
//		
//		
//		saveToFile();
//		//rentCompany.getAllDrivers().forEach(System.out::println);
//
//	}
	
	public static RentCompanyEmbedded getRandomCompany() {
		fillModelsCarsDrivers();
		RentCompanyImmitation();
		return (RentCompanyEmbedded) rentCompany;
	}

	private static void saveToFile() {
		if(rentCompany instanceof Persistable)
			((Persistable)rentCompany)
			.saveToFile("company.data");
		
	}

	private static void RentCompanyImmitation
	() 
	{
		LocalDate current=DATE_ONE;
		while(current.isBefore(LAST_DATE)) {
			rentCarsDay(current);
			returnCarsDay(current);
			current=current.plusDays(1);
		}
    }

		private static void returnCarsDay(LocalDate current) {
		List<String> carsToReturn=shouldReturned.get(current);
		if(carsToReturn!=null) {
			for(String carNumber:carsToReturn) {
				returnCar(carNumber,current);
			}
		}
		
	}

		private static void returnCar(String carNumber,
				LocalDate current) {
			int gasTankPercent=getRandomGasTank();
			int damages=getRandomDamage
					(rentCompany.getCar(carNumber).getState());
			rentCompany.returnCar
			(carNumber, current, gasTankPercent, damages);
			
		}

		private static void rentCarsDay(LocalDate current) {
		int nRentsDay=getRentsToday();
		for(int i=0;i<nRentsDay;i++) {
			rentCar(current);
		}
		
	}

		private static void rentCar(LocalDate current) {
			String carNumber=getRandomCarNumber();
			long licenseId=getRandomLicenseId();
			int rentDays=getRandomRentDays();
			rentCompany.rentCar
			(carNumber, licenseId, current, rentDays);
			addCarReturnded(carNumber,current,rentDays);
			
		}

		private static void addCarReturnded
		(String carNumber, LocalDate current, int rentDays) {
			LocalDate returnedDate=getReturnedDate(current,rentDays);
			shouldReturned.putIfAbsent
			(returnedDate,new ArrayList<>());
			shouldReturned.get(returnedDate).add(carNumber);
					
			
		}

		private static LocalDate getReturnedDate(LocalDate current, int rentDays) {
			LocalDate returnedDate=current.plusDays(rentDays);
			if(chance(CHANCE_TO_DELAY)) {
				returnedDate.plusDays(getRandomNumber(1,5));
			}
			return returnedDate;
		}

		private static boolean chance(int probability) {
			
			return getRandomNumber(1,100)<probability;
		}

		private static long getRandomLicenseId() {
			return getRandomNumber(1,N_DRIVERS);
		}

		private static String getRandomCarNumber() {
			
			return "car"+getRandomNumber(1,N_CARS);
		}

		private static int getRandomDamage(State state) {
		  if(state == State.BAD) {
		   return getRandomNumber(100, 310) / 10;
		  }
		  if(state == State.GOOD) {
		   return getRandomNumber(10, 310) / 10;
		  }
		  return getRandomNumber(0, 310) / 10;
		 }

		 private static int getRandomReturnGasTank() {
			 if(getRandomNumber(0,100)<CHANCE_TO_FULL_TANK)
				 return 100;
		  return getRandomNumber(MIN_PERCENT_RETURN_GAS_TANK, MAX_PERCENT_RETURN_GAS_TANK);
		 }

		 private static int getRentsToday() {
		 return N_CARS*RENTS_EVERY_DAY/100;
		 }

		 

		 private static int getRandomRentDays() {
		  return getRandomNumber(MIN_RENT_DAYS, MAX_RENT_DAYS);
		 }

		 private static long getRandomDriver() {
		  return getRandomNumber(0, N_DRIVERS-1);
		 }
		
	

	private static void fillModelsCarsDrivers
	() {
		for(int i=1; i<=N_MODELS; i++) {
			rentCompany.addModel(getRandomModel(i));
		}

		for(int i=1; i<=N_CARS; i++) {
			rentCompany.addCar(getRandomCar(i));
		}

		for(int i=1; i<=N_DRIVERS; i++) {
			rentCompany.addDriver(getRandomDriver(i));
		}
		
	}
	private static Model getRandomModel(int modelNumber) {

		String modelName = "model"+modelNumber;
		int gasTank = getRandomGasTank();
		String company = getRandomCompanyName();
		String country = getRandomCountryName();
		int priceDay = getRandomPriceDay();
		return new Model(modelName, gasTank, company, country, priceDay);
	}

private static Car getRandomCar(int carNumber) {
		String regNumber = "car"+carNumber;
		String color = getRandomColor();
		String modelName = getRandomModelName();
		return new Car(regNumber, color, modelName);
	}

	private static Driver getRandomDriver(int id) {

		long licenseId = id;
		String name = getRandomDriverName();
		int birthYear = getRandomBirthYear();
		String phone = getRandomPhone();
		return new Driver(licenseId, name, birthYear, phone);
	}
	private static String getRandomColor() {
		return "Color"+getRandomNumber(1, N_COLORS);
	}

	private static int getRandomBirthYear() {
		return getRandomNumber(MIN_DRIVER_YEAR, MAX_DRIVER_YEAR);
	}

	

	private static int getRandomNumber(int min, int max) {
		return gen.ints(1,min,max+1).findFirst().getAsInt();
	}

	private static int getRandomPriceDay() {
		return getRandomNumber(1, PRICE_DAY_MAX);
	}

	private static String getRandomCountryName() {
		return "CountryName"+getRandomNumber(1, N_COUNTRIES);
	}

	private static String getRandomCompanyName() {
		return "CompanyName"+getRandomNumber(1, N_COMPANIES);
	}

	private static int getRandomGasTank() {
		return getRandomNumber(1, GAZ_TANK_MAX);
	}

	private static String getRandomModelName() {
		return "model"+getRandomNumber(1, N_MODELS);
	}

	private static String getRandomDriverName() {
		return "Driver"+getRandomNumber(1, N_DRIVERS);
	}

	




	private static String getRandomPhone() {
		String[]prefixes= {"050","051","052","053",
				"054","055","058"};
		return String.format("%s-%d",
				prefixes
						[getRandomNumber(0,prefixes.length-1)],
				getRandomNumber(1000000,9999999));
	}

}
