package lt.paulius.maps.models;

import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressType;
import com.google.maps.model.Geometry;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ElementCollection
    private List<AddressComponent> addressComponents;

    private String formattedAddress;

    private Geometry geometry;

    private String placeId;

    @ElementCollection
    private List<AddressType> types;

    public City(List<AddressComponent> addressComponents, String formattedAddress,
                Geometry geometry, String placeId, List<AddressType> types) {
        this.addressComponents = addressComponents;
        this.formattedAddress = formattedAddress;
        this.geometry = geometry;
        this.placeId = placeId;
        this.types = types;
    }

    public City() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
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

    public List<AddressType> getTypes() {
        return types;
    }

    public void setTypes(List<AddressType> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "GoogleCity{" +
                "id=" + id +
                ", addressComponents=" + addressComponents +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", geometry=" + geometry +
                ", placeId='" + placeId + '\'' +
                ", types=" + types +
                '}';
    }
}
