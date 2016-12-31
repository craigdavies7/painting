package models.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Evolution;
import uk.co.panaxiom.playjongo.PlayJongo;

@BaseDao.CollectionName("evolutions")
@Singleton
public class EvolutionDao extends BaseDao {

    @Inject
    public EvolutionDao(PlayJongo jongo){
        super(jongo);
    }

    public Evolution findByVersion(String version){
        return mongoCollection.findOne("{version: #}", version).as(Evolution.class);
    }
}
