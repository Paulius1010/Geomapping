package lt.paulius.maps.controllers;

import com.google.maps.errors.ApiException;
import lt.paulius.maps.models.AddressByCityAndCountry;
import lt.paulius.maps.models.City;
import lt.paulius.maps.services.CSVImportService;
import lt.paulius.maps.services.CityService;
import lt.paulius.maps.threading.Lock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class ConsoleManager implements ApplicationRunner {


    private final CityService cityService;
    private final CSVImportService csvImportService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final Lock lock;

    @Autowired
    public ConsoleManager(CityService cityService, CSVImportService csvImportService, Lock lock) {
        this.cityService = cityService;
        this.csvImportService = csvImportService;
        this.lock = lock;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws IOException, InterruptedException, ApiException {

        System.out.println("Do you want to upload cities from list in CSV file? Y/N");
        String answer = reader.readLine();
        if (answer.equalsIgnoreCase("Y")) {
            System.out.println("Insert file name (file location should be src/main/resources/):");
            String fileName = reader.readLine();
            List<AddressByCityAndCountry> addressByCityAndCountryList =
                    csvImportService.uploadListOfAddressByCityAndCountryFromCSV(fileName);
            System.out.println("List uploaded");
            System.out.println("Insert apiKey to upload data and save in database:");
            String apiKey = reader.readLine();
            Long startTime;
            Long endTime;
            while (true) {
                System.out.println("Do you want to upload cities using single thread or multiple? (S/M)");
                String answer2 = reader.readLine();
                if (answer2.equalsIgnoreCase("S")) {
                    startTime = System.currentTimeMillis();
                    cityService.executeCityDataImportToDatabaseFromCSVFileUsingSingleThread(addressByCityAndCountryList, apiKey);
                    endTime = System.currentTimeMillis();
                    System.out.println("Cities imported using single thread in " + (endTime - startTime) + " ms.");
                    turnOnSearch();
                    return;
                } else if (answer2.equalsIgnoreCase("M")) {
                    startTime = System.currentTimeMillis();
                    cityService.executeCityDataImportToDatabaseFromCSVFileUsingMultipleThreads(addressByCityAndCountryList, lock, apiKey);
                    while (lock.getRunningThreadsNumber() > 0) {
                        synchronized (lock) {
                            lock.wait();
                        }
                    }
                    endTime = System.currentTimeMillis();
                    System.out.println("Cities imported using multiple threads in " + (endTime - startTime) + " ms.");
                    turnOnSearch();
                    return;
                }
            }
        }
            turnOnSearch();
            System.out.println("Bye!");
    }

    private void turnOnSearch() throws IOException {
        while (true) {
            System.out.println("Do you want to search city? Y/N");
            String answer = reader.readLine();
            if (answer.equalsIgnoreCase("Y")) {

            System.out.println("Insert latitude value:");
            String lat = reader.readLine();
            Double latitude = Double.parseDouble(lat);
            System.out.println("Insert longitude value:");
            String lng = reader.readLine();
            Double longitude = Double.parseDouble(lng);

            City city = cityService.findCityByCoordinates(latitude, longitude);
            if (city != null) {
                System.out.println("Coordinates in " + city.getFormattedAddress());
            } else {
                List<City> nearestCities = cityService.findTenNearestCitiesByCoordinates(latitude, longitude);
                if (!nearestCities.isEmpty()) {
                    System.out.println("Coordinates are near to these cities:");
                    nearestCities.forEach(System.out::println);
                }
            }
        } else {
                return;
            }
    }
    }
}


