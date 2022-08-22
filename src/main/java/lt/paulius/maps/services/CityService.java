package lt.paulius.maps.services;

import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import lt.paulius.maps.controllers.GeocodingService;
import lt.paulius.maps.models.AddressByCityAndCountry;
import lt.paulius.maps.models.CityDAO;
import lt.paulius.maps.repositories.CityRepository;
import lt.paulius.maps.threading.CitySavingThread;
import lt.paulius.maps.threading.Lock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
            List<AddressByCityAndCountry> addressByCityAndCountryList, String apiKey)
            throws IOException, InterruptedException, ApiException {
        for (AddressByCityAndCountry addressByCityAndCountry : addressByCityAndCountryList) {
            String address = addressByCityAndCountry.city() + ", " + addressByCityAndCountry.country();
            saveCityByGivenAddress(address, apiKey);
        }
    }

    public void executeCityDataImportToDatabaseFromCSVFileUsingMultipleThreads(
            List<AddressByCityAndCountry> addressByCityAndCountryList, Lock lock, String apiKey) {
        for (AddressByCityAndCountry addressByCityAndCountry : addressByCityAndCountryList) {
            CitySavingThread citySavingThread = new CitySavingThread(addressByCityAndCountry, geocodingService, cityRepository, lock, apiKey);
            citySavingThread.start();
        }
    }

    public void saveCityByGivenAddress(String address, String apiKey)
            throws IOException, InterruptedException, ApiException {
//        address = "Sarande, Albania";
        GeocodingResult geocodingResult = geocodingService.getGeocodeFromAddress(address, apiKey);
        CityDAO cityDAO = new CityDAO(
                geocodingResult.formattedAddress,
                geocodingResult.geometry,
                geocodingResult.placeId
        );
        saveCity(cityDAO);
    }

    public void saveCity(CityDAO cityDAO) {
        if (cityDAO != null) {
            cityRepository.save(cityDAO);
        }
    }

    public CityDAO findCityByCoordinates(Double lat, Double lng) {
        System.out.println("searching");
        List<CityDAO> cityDAOList = cityRepository.findAll();
        cityDAOList.forEach(System.out::println);
        return cityDAOList.stream().filter(cityDAO ->
                                cityDAO.getGeometry().viewport.northeast.lat >= lat
                                && cityDAO.getGeometry().viewport.northeast.lng >= lng
                                && cityDAO.getGeometry().viewport.southwest.lat <= lat
                                && cityDAO.getGeometry().viewport.southwest.lng <= lng)
                .findFirst().orElse(null);
    }

    public List<CityDAO> findTenNearestCitiesByCoordinates(Double lat1, Double lng1) {
        System.out.println("Searching the nearest ten");
        List<CityDAO> cityDAOList = cityRepository.findAll();
        Map<CityDAO, Double> distancesToCities = new HashMap<>();

        for (CityDAO cityDAO : cityDAOList) {
            Double distance = calculateDistanceBetweenTwoPoints(
                    lat1, lng1, cityDAO.getGeometry().viewport.northeast.lat, cityDAO.getGeometry().viewport.northeast.lng);
            distancesToCities.put(cityDAO, distance);
        }
        Map<CityDAO, Double> sortedCitiesAccordingTODInstances = sortByValues(distancesToCities);
        return getFirstEntriesToLimit(10, sortedCitiesAccordingTODInstances);
    }

    public Double calculateDistanceBetweenTwoPoints(Double lat1, Double lng1, Double lat2, Double lng2) {
        return Math.sqrt(Math.pow((lat2 - lat1), 2) + Math.pow((lng2 -  lng1), 2));
    }

//    public static Map<City> getFirstEntriesToLimit(int limit, SortedMap<Double,City> sortedMap) {
//       return sortedMap.entrySet().stream()
//                .limit(limit)
//                .collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
//    }

    public static List<CityDAO> getFirstEntriesToLimit(int limit, Map<CityDAO, Double> sortedMap) {
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

    public static <K, V extends Comparable<V>> Map<K, V>
    sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =
                Comparator.comparing(map::get);

        Map<K, V> sortedByValues =
                new TreeMap<>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

}
