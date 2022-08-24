package lt.paulius.maps.services;

import com.google.maps.errors.ApiException;
import lt.paulius.maps.controllers.GeocodingService;
import lt.paulius.maps.models.AddressByCityAndCountry;
import lt.paulius.maps.models.City;
import lt.paulius.maps.repositories.CityRepository;
import lt.paulius.maps.threading.GoogleCitySavingThread;
import lt.paulius.maps.threading.Lock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final GeocodingService geocodingService;

    @Autowired
    public CityService(CityRepository cityRepository, GeocodingService geocodingService) {
        this.cityRepository = cityRepository;
        this.geocodingService = geocodingService;
    }

    public void executeCityDataImportToDatabaseFromCSVFileUsingSingleThread(
            List<AddressByCityAndCountry> addressByCityAndCountryList)
            throws IOException, InterruptedException, ApiException, ParserConfigurationException, SAXException {
        for (AddressByCityAndCountry addressByCityAndCountry : addressByCityAndCountryList) {
            String address = addressByCityAndCountry.city() + ", " + addressByCityAndCountry.country();
            saveCityByGivenAddress(address);
        }
    }

    public void executeCityDataImportToDatabaseFromCSVFileUsingMultipleThreads(
            List<AddressByCityAndCountry> addressByCityAndCountryList, Lock lock) {
        for (AddressByCityAndCountry addressByCityAndCountry : addressByCityAndCountryList) {
            GoogleCitySavingThread googleCitySavingThread = new GoogleCitySavingThread(addressByCityAndCountry, geocodingService, cityRepository, lock);
            googleCitySavingThread.start();
        }
    }

    public void saveCityByGivenAddress(String address)
            throws IOException, InterruptedException, ApiException, ParserConfigurationException, SAXException {
//        GeocodingResult geocodingResult = geocodingService.getGeocodeFromAddress(address);
//      for JSON
//        if (geocodingResult != null) {
//            SimplifiedGoogleCity simplifiedGoogleCity = new SimplifiedGoogleCity(
//                    geocodingResult.formattedAddress,
//                    geocodingResult.geometry,
//                    geocodingResult.placeId
//            );
//            saveCity(simplifiedGoogleCity);
//        }

        HttpResponse<String> httpResponse = geocodingService.getGeocodeHttpXmlResponseFromAddress(address);
        City city = geocodingService.parseHttpXmlResponseToObject(httpResponse);

            saveCity(city);

    }

    public void saveCity(City city) {
        if (city != null) {
            cityRepository.save(city);
        }
    }

    public City findCityByCoordinates(Double lat, Double lng) {
        List<City> cityList = cityRepository.findAll();
        return cityList.stream().filter(city ->
//                          for JSON
//                          city.getGeometry().viewport.northeast.lat >= lat
//                        && city.getGeometry().viewport.northeast.lng >= lng
//                        && city.getGeometry().viewport.southwest.lat <= lat
//                        && city.getGeometry().viewport.southwest.lng <= lng)
                                city.getGeometry().getViewport().getNortheast().getLat() >= lat
                                && city.getGeometry().getViewport().getNortheast().getLng() >= lng
                                && city.getGeometry().getViewport().getSouthwest().getLat() <= lat
                                && city.getGeometry().getViewport().getSouthwest().getLng() <= lng)
                .findFirst().orElse(null);
    }

    public List<City> findTenNearestCitiesByCoordinates(Double lat1, Double lng1) {
        List<City> cityList = cityRepository.findAll();
        Map<City, Double> distancesToCities = new HashMap<>();

        for (City city : cityList) {
            Double distance = calculateDistanceBetweenTwoPoints(
//                    lat1, lng1, simplifiedGoogleCity.getGeometry().location.lat, simplifiedGoogleCity.getGeometry().location.lng);
            lat1, lng1, city.getGeometry().getLocation().getLat(), city.getGeometry().getLocation().getLng());

            distancesToCities.put(city, distance);
        }
        Map<City, Double> sortedCitiesAccordingTODInstances = sortByValues(distancesToCities);
        return getFirstEntriesToLimit(10, sortedCitiesAccordingTODInstances);
    }

    public Double calculateDistanceBetweenTwoPoints(Double lat1, Double lng1, Double lat2, Double lng2) {
        return Math.sqrt(Math.pow((lat2 - lat1), 2) + Math.pow((lng2 -  lng1), 2));
    }

    public static List<City> getFirstEntriesToLimit(int limit, Map<City, Double> sortedMap) {
        return sortedMap.entrySet().stream()
                .limit(limit)
                .collect(ArrayList::new, (m, e) -> m.add(e.getKey()), List::addAll);
    }

    public boolean isCityDatabaseEmpty() {
        return cityRepository.count() != 0;
    }

    public void deleteCitiesInDatabase() {
        cityRepository.deleteAll();
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =
                Comparator.comparing(map::get);

        Map<K, V> sortedByValues =
                new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

}
