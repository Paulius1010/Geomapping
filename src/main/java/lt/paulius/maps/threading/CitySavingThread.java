package lt.paulius.maps.threading;

import com.google.maps.model.GeocodingResult;
import lt.paulius.maps.controllers.GeocodingService;
import lt.paulius.maps.models.AddressByCityAndCountry;
import lt.paulius.maps.models.CityDAO;
import lt.paulius.maps.repositories.CityRepository;

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

    @Override
    public void run() {
        try {
            lock.addRunningThread();
            String address = addressByCityAndCountry.city() + ", " + addressByCityAndCountry.country();
            GeocodingResult geocodingResult = geocodingService.getGeocodeFromAddress(address, apiKey);
            if (geocodingResult != null) {
                CityDAO cityDAO = new CityDAO(
                        geocodingResult.formattedAddress,
                        geocodingResult.geometry,
                        geocodingResult.placeId
                );

                cityRepository.save(cityDAO);

                synchronized (lock) {
                    lock.notifyAll();
                }
            }
            lock.removeRunningThread();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
