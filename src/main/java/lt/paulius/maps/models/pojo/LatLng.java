package lt.paulius.maps.models.pojo;

import com.google.maps.internal.StringJoin;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public class LatLng implements StringJoin.UrlValue, Serializable {
    private static final long serialVersionUID = 1L;
    public double lat;
    public double lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String toString() {
        return this.toUrlValue();
    }

    public String toUrlValue() {
        return String.format(Locale.ENGLISH, "%.8f,%.8f", this.lat, this.lng);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            com.google.maps.model.LatLng latLng = (com.google.maps.model.LatLng)o;
            return Double.compare(latLng.lat, this.lat) == 0 && Double.compare(latLng.lng, this.lng) == 0;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.lat, this.lng});
    }
}
