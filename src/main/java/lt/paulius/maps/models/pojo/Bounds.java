package lt.paulius.maps.models.pojo;

import java.io.Serializable;

public class Bounds implements Serializable {
    private static final long serialVersionUID = 1L;
    public LatLng southwest;
    public LatLng northeast;

    public Bounds() {
    }

    public LatLng getNortheast() {
        return northeast;
    }

    public Bounds(LatLng southwest, LatLng northeast) {
        this.northeast = northeast;
        this.southwest = southwest;
    }

    public void setNortheast(LatLng northeast) {
        this.northeast = northeast;
    }

    public LatLng getSouthwest() {
        return southwest;
    }

    public void setSouthwest(LatLng southwest) {
        this.southwest = southwest;
    }

    public String toString() {
        return String.format("[%s, %s]", this.northeast, this.southwest);
    }
}
