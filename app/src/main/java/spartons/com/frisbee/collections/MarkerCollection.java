package spartons.com.frisbee.collections;

import android.util.Log;
import com.google.android.gms.maps.model.Marker;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class MarkerCollection {

    private Map<String, Marker> markerMap = new HashMap<>();

    @Inject
    public MarkerCollection() {

    }

    public void insertMarker(String key, Marker marker) {
        if (!markerMap.containsKey(key))
            markerMap.put(key, marker);
        else
            Log.e("MarkerCollection", "false");
    }

    public void removeMarker(String driverId) {
        Marker marker = getMarker(driverId);
        if (marker != null) {
            marker.remove();
            markerMap.remove(driverId);
        }
    }

    public Marker getMarker(String driverId) {
        return markerMap.get(driverId);
    }

    public Map<String, Marker> allMarkers() {
        return markerMap;
    }

}
