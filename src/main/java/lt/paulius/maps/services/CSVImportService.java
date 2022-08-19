package lt.paulius.maps.services;

import lt.paulius.maps.models.AddressByCityAndCountry;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVImportService {

    public List<AddressByCityAndCountry> uploadListOfAddressByCityAndCountryFromCSV(String fileName) {
        List<AddressByCityAndCountry> addresses = new ArrayList<>();
        try {
            String locationOfFile = "src/main/resources/";
            String linkToFile = locationOfFile + fileName;
            FileReader reader = new FileReader(linkToFile);
            BufferedReader bufferedReader = new BufferedReader(reader);

//			bufferedReader.lines().forEach(System.out::println);

            String line = bufferedReader.readLine();
//			Skip first line
            line = bufferedReader.readLine();
//			Add lines to array
            while (line != null) {
                String[] addressLine = line.split(";");
                String city = addressLine[0];
                String country = addressLine[1];
                AddressByCityAndCountry addressByCityAndCountry = new AddressByCityAndCountry(city, country);
                addresses.add(addressByCityAndCountry);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    return addresses;
    }
}
