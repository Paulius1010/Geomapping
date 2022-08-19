package lt.paulius.maps.controllers;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeocodingService {

    public GeocodingResult getGeocodeFromAddress(String address, String apiKey)
            throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

//        GeocodingResult[]results = GeocodingApi.newRequest(context).components(ComponentFilter.postalCode("75002")).await();
        GeocodingResult[]results = GeocodingApi.geocode(context, address).await();
        System.out.println(results[0]);
        return results[0];


////this will get geolocation details via address
//        GeocodingResult[] results2 = GeocodingApi.geocode(context, "One Apple Park Way Cupertino, CA 95014").await();
//        System.out.println(results2[0]);
//
////another way to get geolocation details via address
//        GeocodingResult[] results3 = GeocodingApi.newRequest(context).address("One Apple Park Way Cupertino, CA 95014").await();
//        System.out.println(results3[0]);

    }

}
