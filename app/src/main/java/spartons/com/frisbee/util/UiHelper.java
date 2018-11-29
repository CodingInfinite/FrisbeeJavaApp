package spartons.com.frisbee.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import io.reactivex.annotations.NonNull;
import spartons.com.frisbee.R;
import spartons.com.frisbee.lsitener.IPositiveNegativeListener;

import javax.inject.Inject;

public class UiHelper {

    private final Context context;

    @Inject
    public UiHelper(Context context) {
        this.context = context;
    }

    public void showFragment(@NonNull FragmentManager fragmentManager, int containerId, @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerId, fragment);
        fragmentTransaction.commit();
    }

    public boolean isPlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return ConnectionResult.SUCCESS == status;
    }

    public boolean isHaveLocationPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isLocationProviderEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void showPositiveDialogWithListener(@NonNull Activity activity, String title, String content, final IPositiveNegativeListener positiveNegativeDialogListener, String positiveText, boolean cancelable) {
        buildDialog(activity, title, content)
                .getBuilder()
                .positiveText(positiveText)
                .positiveColor(getColor(R.color.colorPrimary))
                .onPositive((dialog, which) -> positiveNegativeDialogListener.onPositive())
                .cancelable(cancelable)
                .show();
    }

    private MaterialDialog buildDialog(Activity activity, String title, String content) {
        return new MaterialDialog.Builder(activity)
                .title(title)
                .content(content)
                .build();
    }

    private int getColor(int resource) {
        return ContextCompat.getColor(context, resource);
    }

    public LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        return locationRequest;
    }

    public void toast(String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(@NonNull View view, String content) {
        Snackbar.make(view, content, Snackbar.LENGTH_SHORT).show();
    }

    public void changePinCircleColor(Drawable mDrawable, int circleColor) {
        int color = getColor(circleColor);
        if (mDrawable instanceof ShapeDrawable) {
            ShapeDrawable shapeDrawable = (ShapeDrawable) mDrawable;
            shapeDrawable.getPaint().setColor(color);
        } else if (mDrawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) mDrawable;
            gradientDrawable.setColor(color);
        } else if (mDrawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) mDrawable;
            colorDrawable.setColor(color);
        }
    }
}
