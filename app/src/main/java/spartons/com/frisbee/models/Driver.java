package spartons.com.frisbee.models;

import android.support.annotation.NonNull;

public class Driver {

    private String id;
    public double lat;
    public double lng;
    public float angle;

    public Driver(String id, double lat, double lng, float angle) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.angle = angle;
    }

    public Driver() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void update(double lat, double lng, float angle) {
        this.lat = lat;
        this.lng = lng;
        this.angle = angle;
    }

    @NonNull
    @Override
    public String toString() {
        return "Driver{" +
                "id='" + id + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", angle=" + angle +
                '}';
    }
}
