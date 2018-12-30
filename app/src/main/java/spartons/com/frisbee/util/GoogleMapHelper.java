package spartons.com.frisbee.util;

import android.content.res.Resources;
import android.location.Location;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import spartons.com.frisbee.R;

import javax.inject.Inject;

public class GoogleMapHelper {

    private final Resources resources;

    @Inject
    public GoogleMapHelper(Resources resources) {
        this.resources = resources;
    }

    private static final int ZOOM_LEVEL = 18;
    private static final int TILT_LEVEL = 25;
    private static GeoApiContext.Builder geoApiContextBuilder = new GeoApiContext.Builder();

    /**
     * @param location in which position to Zoom the camera.
     * @return the [CameraUpdate] with Zoom and Tilt level added with the given position.
     */

    public CameraUpdate buildCameraUpdate(Location location) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .tilt(TILT_LEVEL)
                .zoom(ZOOM_LEVEL)
                .build();
        return CameraUpdateFactory.newCameraPosition(cameraPosition);
    }

    /**
     * This function sets the default google map settings.
     *
     * @param googleMap to set default settings.
     */

    public void defaultMapSettings(GoogleMap googleMap) {
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.setBuildingsEnabled(true);
    }

    private MarkerOptions getMarkerOptions(LatLng position, int resource) {
        return new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(resource))
                .position(position);
    }

    public MarkerOptions getUserMarker(Location location) {
        return getMarkerOptions(new LatLng(location.getLatitude(), location.getLongitude()), R.drawable.blue_dot);
    }

    /**
     * @param position where to draw the [com.google.android.gms.maps.model.Marker]
     * @return the [MarkerOptions] with given properties added to it.
     */

    public MarkerOptions getDriverMarkerOptions(LatLng position, float angle) {
        MarkerOptions options = getMarkerOptions(position, R.drawable.caronmap);
        options.flat(true);
        options.rotation(angle + 90);
        return options;
    }

    /**
     * @return the google distance api key.
     */

    private String distanceApi() {
        return resources.getString(R.string.google_distance_matrix_api_key);
    }

    /**
     * The function returns the ${[GeoApiContext]} with distance api key.
     *
     * @return the ${[GeoApiContext]} with distance api.
     */

    public GeoApiContext geoContextDistanceApi() {
        return geoApiContextBuilder
                .apiKey(distanceApi())
                .build();
    }

}
