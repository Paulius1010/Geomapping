package lt.paulius.maps.models.pojo;

import java.io.Serializable;

public class GeocodeResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String status;
    private Result result;

    public GeocodeResponse() {
    }

    public GeocodeResponse(String status, Result result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String toString() {
        return String.format("[Geocode response: status=%s, result=%s]", this.status, this.result);
    }
}
