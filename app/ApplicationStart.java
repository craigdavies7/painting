import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Evolution;
import models.dao.EvolutionDao;
import play.Application;
import play.Logger;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.io.File;

@Singleton
public class ApplicationStart {

    @Inject
    public ApplicationStart(Application app, EvolutionDao evolutionDao) {

        Logger.info("Looking for new evolution files...");

        File evolutionsDir = app.getFile("conf/evolutions");

        String version;
        Evolution evolution;
        if (evolutionsDir.exists()) {
            File[] evolutionFiles = evolutionsDir.listFiles();
            for (File f : evolutionFiles) {
                version = f.getName().replace(".js", "");
                evolution = evolutionDao.findByVersion(version);
                if (evolution == null){
                    Logger.info("Applying " + f.getName() + "...");
                    evolution = new Evolution();
                    evolution.version = version;
                    evolutionDao.save(evolution);
                }
            }
        }
    }
}