package lt.paulius.maps.threading;

import com.google.maps.model.GeocodingResult;
import lt.paulius.maps.controllers.GeocodingService;
import lt.paulius.maps.models.AddressByCityAndCountry;
import lt.paulius.maps.models.City;
import lt.paulius.maps.repositories.CityRepository;

import java.util.Arrays;

public class CitySavingThread extends Thread {

    private final AddressByCityAndCountry addressByCityAndCountry;
    private final GeocodingService geocodingService;
    private final CityRepository cityRepository;
    private final Lock lock;
    private final String apiKey;

    public CitySavingThread(AddressByCityAndCountry addressByCityAndCountry, GeocodingService geocodingService, CityRepository cityRepository, Lock lock, String apiKey) {
        this.addressByCityAndCountry = addressByCityAndCountry;
        this.geocodingService = geocodingService;
        this.cityRepository = cityRepository;
        this.lock = lock;
        this.apiKey = apiKey;
    }

    public void run() {
        try {
            lock.addRunningThread();
            String address = addressByCityAndCountry.city() + ", " + addressByCityAndCountry.country();

            address = "Sarande, Albania";
            GeocodingResult geocodingResult = geocodingService.getGeocodeFromAddress(address, apiKey);
            City city = new City(
                    Arrays.stream(geocodingResult.addressComponents).toList(),
                    geocodingResult.formattedAddress,
                    geocodingResult.geometry,
                    geocodingResult.placeId,
                    Arrays.stream(geocodingResult.types).toList()
            );

                cityRepository.save(city);
            lock.removeRunningThread();

            synchronized (lock) {
                lock.notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
