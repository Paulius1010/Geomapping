package lt.paulius.maps.models;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressType;
import com.google.maps.model.Geometry;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
public class CityDAO implements Serializable, Comparable {

    private String formattedAddress;

    private Geometry geometry;

    @Id
    private String placeId;

    public CityDAO(String formattedAddress,
                   Geometry geometry, String placeId) {
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.placeId = placeId;
    }

    public CityDAO() {
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

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}
