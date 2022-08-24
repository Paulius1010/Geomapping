package lt.paulius.maps.threading;

import lt.paulius.maps.controllers.GeocodingService;
import lt.paulius.maps.models.AddressByCityAndCountry;
import lt.paulius.maps.models.City;
import lt.paulius.maps.repositories.CityRepository;

import java.net.http.HttpResponse;

public class GoogleCitySavingThread extends Thread {

    private final AddressByCityAndCountry addressByCityAndCountry;
    private final GeocodingService geocodingService;
    private final CityRepository cityRepository;
    private final Lock lock;

    public GoogleCitySavingThread(AddressByCityAndCountry addressByCityAndCountry, GeocodingService geocodingService,
                                  CityRepository cityRepository, Lock lock) {
        this.addressByCityAndCountry = addressByCityAndCountry;
        this.geocodingService = geocodingService;
        this.cityRepository = cityRepository;
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            lock.addRunningThread();
            String address = addressByCityAndCountry.city() + ", " + addressByCityAndCountry.country();
//            GeocodingResult geocodingResult = geocodingService.getGeocodeFromAddress(address);
            HttpResponse<String> httpResponse = geocodingService.getGeocodeHttpXmlResponseFromAddress(address);


            if (httpResponse != null) {
//                for JSON
//                SimplifiedGoogleCity simplifiedGoogleCity = new SimplifiedGoogleCity(
//                        geocodingResult.formattedAddress,
//                        geocodingResult.geometry,
//                        geocodingResult.placeId
//                );
                City city = geocodingService.parseHttpXmlResponseToObject(httpResponse);

                if (city != null) {
                    cityRepository.save(city);
                }

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
