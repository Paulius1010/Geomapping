package lt.paulius.maps.services;

import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import lt.paulius.maps.controllers.GeocodingService;
import lt.paulius.maps.models.AddressByCityAndCountry;
import lt.paulius.maps.models.City;
import lt.paulius.maps.repositories.CityRepository;
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

    public void executeCityDataImportToDatabaseFromCSVFile(
            List<AddressByCityAndCountry> addressByCityAndCountryList, String apiKey)
            throws IOException, InterruptedException, ApiException {
        for (AddressByCityAndCountry addressByCityAndCountry : addressByCityAndCountryList) {
            String address = addressByCityAndCountry.city() + ", " + addressByCityAndCountry.country();
            saveCityByGivenAddress(address, apiKey);
        }
    }

    private void saveCityByGivenAddress(String address, String apiKey)
            throws IOException, InterruptedException, ApiException {
        address = "Sarande, Albania";
        GeocodingResult geocodingResult = geocodingService.getGeocodeFromAddress(address, );
        City city = new City(
                Arrays.stream(geocodingResult.addressComponents).toList(),
                geocodingResult.formattedAddress,
                geocodingResult.geometry,
                geocodingResult.placeId,
                Arrays.stream(geocodingResult.types).toList()
        );
        saveCity(city);
    }

    public void saveCity(City city) {
        if (city == null) {
        } else {
            cityRepository.save(city);
        }
    }

    public City findCityByCoordinates(Double lat, Double lng) {
        return cityRepository.findAll().stream().filter(city ->
                        city.getGeometry().bounds.northeast.lat >= lat
                                && city.getGeometry().bounds.northeast.lng <= lng)
                .findFirst().orElse(null);
    }

    public List<City> findTenNearestCitiesByCoordinates(Double lat1, Double lng1) {
        List<City> cityList = cityRepository.findAll();
        Map<City, Double> distancesToCities = new HashMap<>();

        for (City city : cityList) {
            Double distance = calculateDistanceBetweenTwoPoints(
                    lat1, lng1, city.getGeometry().bounds.northeast.lat, city.getGeometry().bounds.northeast.lng);
            distancesToCities.put(city, distance);
        }
        SortedMap<City, Double> sortedDistancesToCities = new TreeMap<>(distancesToCities);
        return getFirstEntriesToLimit(10, sortedDistancesToCities);
    }

    public Double calculateDistanceBetweenTwoPoints(Double lat1, Double lng1, Double lat2, Double lng2) {
        return Math.sqrt(Math.pow((lat2 - lat1), 2) + Math.pow((lng2 -  lng1), 2));
    }

//    public static Map<City> getFirstEntriesToLimit(int limit, SortedMap<Double,City> sortedMap) {
//       return sortedMap.entrySet().stream()
//                .limit(limit)
//                .collect(TreeMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
//    }

    public static List<City> getFirstEntriesToLimit(int limit, SortedMap<City, Double> sortedMap) {
        return sortedMap.entrySet().stream()
                .limit(limit)
                .collect(ArrayList::new, (m, e) -> m.add(e.getKey()), List::addAll);
    }

}
