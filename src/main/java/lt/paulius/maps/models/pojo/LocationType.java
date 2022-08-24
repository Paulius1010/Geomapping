package lt.paulius.maps.models.pojo;

import java.io.Serializable;

public class LocationType implements Serializable {
    private static final long serialVersionUID = 1L;
    private String locationType;

    public LocationType() {
    }

    public LocationType(String locationType) {
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    @Override
    public String toString() {
        return locationType;
    }
}
