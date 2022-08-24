package lt.paulius.maps.models.pojo;

import java.io.Serializable;

public class Geometry implements Serializable {
    private static final long serialVersionUID = 1L;
    public Bounds bounds;
    public LatLng location;
    public LocationType locationType;
    public Bounds viewport;

    public Geometry() {
    }

    public Geometry(Bounds bounds, LatLng location, LocationType locationType, Bounds viewport) {
        this.bounds = bounds;
        this.location = location;
        this.locationType = locationType;
        this.viewport = viewport;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    public Bounds getViewport() {
        return viewport;
    }

    public void setViewport(Bounds viewport) {
        this.viewport = viewport;
    }

    public String toString() {
        return String.format("[Geometry: %s (%s) bounds=%s, viewport=%s]", this.location, this.locationType, this.bounds, this.viewport);
    }
}