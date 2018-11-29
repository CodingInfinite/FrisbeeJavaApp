package spartons.com.frisbee.di.component;

import dagger.Component;
import spartons.com.frisbee.activities.main.ui.MainActivity;
import spartons.com.frisbee.di.scopes.ActivityScope;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {

    void inject(MainActivity mainActivity);
}
