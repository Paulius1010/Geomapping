package lt.paulius.maps.models;

//import com.google.maps.model.Geometry;

import lt.paulius.maps.models.pojo.Geometry;

import javax.persistence.*;

@Entity
@Table(name = "cities")
public class City {

    private String formattedAddress;
    private Geometry geometry;
    @Id
    private String placeId;

    public City(String formattedAddress, Geometry geometry, String placeId) {
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.placeId = placeId;
    }

    public City() {
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "City{" +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", geometry=" + geometry +
                ", placeId='" + placeId + '\'' +
                '}';
    }

}
