package spartons.com.frisbee.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import spartons.com.frisbee.application.MyCustomApplication;
import spartons.com.frisbee.di.component.ActivityComponent;
import spartons.com.frisbee.di.component.DaggerActivityComponent;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    protected ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = DaggerActivityComponent
                .builder()
                .appComponent(getApp().appComponent())
                .build();
    }

    private MyCustomApplication getApp() {
        return (MyCustomApplication) getApplicationContext();
    }
}
