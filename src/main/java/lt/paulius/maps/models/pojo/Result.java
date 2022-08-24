package lt.paulius.maps.models.pojo;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private Type type1;
    private Type type2;
    private String formattedAddress;
    private List<AddressComponent> addressComponents;
    private Geometry geometry;
    private String placeId;

    public Result() {
    }

    public Result(Type type1, Type type2, String formattedAddress, List<AddressComponent> addressComponents, Geometry geometry, String placeId) {
        this.type1 = type1;
        this.type2 = type2;
        this.formattedAddress = formattedAddress;
        this.addressComponents = addressComponents;
        this.geometry = geometry;
        this.placeId = placeId;
    }

    public Type getType1() {
        return type1;
    }

    public void setType1(Type type1) {
        this.type1 = type1;
    }

    public Type getType2() {
        return type2;
    }

    public void setType2(Type type2) {
        this.type2 = type2;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
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

    public String toString() {
        return String.format("[Result: %s, %s, formatted address=%s, address components=%s, geometry=%s, place id=%s]",
                this.type1, this.type2, this.formattedAddress, this.addressComponents, this.geometry, this.placeId);
    }

}
