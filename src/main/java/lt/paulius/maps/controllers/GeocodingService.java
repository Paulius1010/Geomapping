package lt.paulius.maps.controllers;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeocodingService {

    public GeocodingResult getGeocodeFromAddress(String address, String apiKey)
            throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

        GeocodingResult[]results = GeocodingApi.geocode(context, address).await();
        if (results.length !=0) {
            return results[0];
        } else {
           return null;
        }
    }

}
