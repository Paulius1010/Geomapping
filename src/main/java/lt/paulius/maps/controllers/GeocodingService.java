package lt.paulius.maps.controllers;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import lt.paulius.maps.models.City;
import lt.paulius.maps.models.pojo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class GeocodingService {


    @Value("${google.maps.apiKey}")
    private String apiKey;

    public GeocodingService() throws ParserConfigurationException {
    }

    public GeocodingResult getGeocodeFromAddress(String address)
            throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
        GeocodingResult[]results = GeocodingApi.geocode(context, address).await();
        if (results.length !=0) {
            return results[0];
        } else {
            return null;
        }
    }

    public HttpResponse<String> getGeocodeHttpXmlResponseFromAddress(String address) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String requestUri = "https://maps.googleapis.com/maps/api/geocode/xml" + "?address=" + encodedAddress + "&key=" + apiKey;
        HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
                .timeout(Duration.ofMillis(2000)).build();
        HttpResponse<String> geocodingResponse = httpClient.send(geocodingRequest,
                HttpResponse.BodyHandlers.ofString());
        return geocodingResponse;
    }

    public City parseHttpXmlResponseToObject(HttpResponse<String> httpResponse) throws IOException, SAXException, ParserConfigurationException {

        City simplifiedCity = new City();

            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(httpResponse.body()));

            Document doc = db.parse(is);
            Element formattedAddressElement = (Element) doc.getElementsByTagName("formatted_address").item(0);
            Element placeIdElement = (Element) doc.getElementsByTagName("place_id").item(0);
            Element geometryElement = (Element) doc.getElementsByTagName("geometry").item(0);
            if (geometryElement == null) {
                return null;
            }
            Element locationElement = (Element) geometryElement.getElementsByTagName("location").item(0);
            Element locationLatElement = (Element) locationElement.getElementsByTagName("lat").item(0);
            Element locationLngElement = (Element) locationElement.getElementsByTagName("lng").item(0);
            Element locationTypeElement = (Element) geometryElement.getElementsByTagName("location_type").item(0);
            Element viewportElement = (Element) geometryElement.getElementsByTagName("viewport").item(0);
            Element southwestElement = (Element) viewportElement.getElementsByTagName("southwest").item(0);
            Element northeastElement = (Element) viewportElement.getElementsByTagName("northeast").item(0);
            Element southwestLatElement = (Element) southwestElement.getElementsByTagName("lat").item(0);
            Element southwestLngElement = (Element) southwestElement.getElementsByTagName("lng").item(0);
            Element northeastLatElement = (Element) northeastElement.getElementsByTagName("lat").item(0);
            Element northeastLngElement = (Element) northeastElement.getElementsByTagName("lng").item(0);

            Bounds viewport = new Bounds(new LatLng(
                    Double.parseDouble(getCharacterDataFromElement(southwestLatElement)),
                    Double.parseDouble(getCharacterDataFromElement(southwestLngElement))
            ),
                    new LatLng(
                            Double.parseDouble(getCharacterDataFromElement(northeastLatElement)),
                            Double.parseDouble(getCharacterDataFromElement(northeastLngElement))
                    ));
            LatLng location = new LatLng(
                    Double.parseDouble(getCharacterDataFromElement(locationLatElement)),
                    Double.parseDouble(getCharacterDataFromElement(locationLngElement))
            );
            LocationType locationType = new LocationType(getCharacterDataFromElement(locationTypeElement));

            Geometry geometry = new Geometry(viewport, location, locationType, viewport);

            simplifiedCity.setFormattedAddress(getCharacterDataFromElement(formattedAddressElement));
            simplifiedCity.setPlaceId(getCharacterDataFromElement(placeIdElement));
            simplifiedCity.setGeometry(geometry);

        return simplifiedCity;

    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

//            JAXBContext jaxbContext;
//            GeocodeResponse geocodeResponse = null;
//            try
//            {
//                jaxbContext = JAXBContext.newInstance(GeocodeResponse.class);
//                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//                geocodeResponse = (GeocodeResponse) jaxbUnmarshaller.unmarshal(new StringReader(httpResponse.body()));
//                System.out.println(geocodeResponse);
//            }
//            catch (JAXBException e)
//            {
//                e.printStackTrace();
//            }
//
//            if (geocodeResponse != null) {
//                return new SimplifiedCity(
//                        geocodeResponse.getResult().getFormattedAddress(),
//                        geocodeResponse.getResult().getGeometry(),
//                        geocodeResponse.getResult().getPlaceId()
//                );
//            }
//            return null;


        }

