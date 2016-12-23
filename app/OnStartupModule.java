import com.google.inject.AbstractModule;
import play.Logger;

public class OnStartupModule extends AbstractModule {

    @Override
    protected void configure() {

        Logger.info("Binding application start");
        bind(ApplicationStart.class).asEagerSingleton();

    }
}