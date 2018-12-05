package spartons.com.frisbee.repo;

import com.google.android.gms.maps.model.Marker;
import spartons.com.frisbee.collections.MarkerCollection;

import javax.inject.Inject;
import java.util.Map;

public class MarkerRepo {

    private final MarkerCollection markerCollection;

    @Inject
    public MarkerRepo(MarkerCollection markerCollection) {
        this.markerCollection = markerCollection;
    }

    public void insert(String key, Marker marker) {
        markerCollection.insertMarker(key, marker);
    }

    public void remove(String s) {
        markerCollection.removeMarker(s);
    }

    public Marker get(String s) {
        return markerCollection.getMarker(s);
    }

    public Map<String, Marker> allItems() {
        return markerCollection.allMarkers();
    }
}
