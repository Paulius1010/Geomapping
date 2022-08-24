package lt.paulius.maps.services;

import lt.paulius.maps.models.AddressByCityAndCountry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVImportService {

    @Value("${your.file.location}")
    private String fileLocation;

    public List<AddressByCityAndCountry> uploadListOfAddressByCityAndCountryFromCSV() {
        List<AddressByCityAndCountry> addresses = new ArrayList<>();
        try {
            FileReader reader = new FileReader(fileLocation);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line = bufferedReader.readLine();
            line = bufferedReader.readLine();
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
