import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.CommandResult;
import models.Evolution;
import models.dao.EvolutionDao;
import play.Application;
import play.Logger;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

@Singleton
public class ApplicationStart {
    private String getEvolutionVersion(String filename){
        if (filename != null){
            return filename.replace(".js", "");
        }
        return null;
    }

    private class FileComparator implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            int v1, v2;
            try {
                v1 = Integer.parseInt(getEvolutionVersion(f1.getPath()));
                v2 = Integer.parseInt(getEvolutionVersion(f2.getPath()));
                // sort ascending
                return v1 - v2;
            } catch (NumberFormatException nfe){
                return 0;
            }
        }
    }

    @Inject
    public ApplicationStart(Application app, PlayJongo jongo, EvolutionDao evolutionDao) {

        Logger.info("Looking for new evolution files...");

        File evolutionsDir = app.getFile("conf/evolutions");

        String version;
        Evolution evolution;
        if (evolutionsDir.exists()) {
            File[] evolutionFiles = evolutionsDir.listFiles();
            Arrays.sort(evolutionFiles, new FileComparator());
            for (File f : evolutionFiles) {
                version = getEvolutionVersion(f.getName());
                evolution = evolutionDao.findByVersion(version);
                if (evolution == null){
                    Logger.info("Applying " + f.getName() + "...");
                    try {
                        String content = new String(Files.readAllBytes(f.toPath()));
                        Logger.info("Executing " + content  + "...");
                        CommandResult result = jongo.getDatabase().doEval(content);
                        if (result.ok()) {
                            Logger.info("Successfully applied " + f.getName() + "!");
                        } else {
                            Logger.error(result.getErrorMessage());
                        }
                    } catch (IOException e) {
                        Logger.error("Error while reading " + f.getName() + ". Details: " + e.getMessage());
                    }
                    evolution = new Evolution();
                    evolution.version = version;
                    evolutionDao.save(evolution);
                }
            }
        }
    }
}