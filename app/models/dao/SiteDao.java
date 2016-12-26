package models.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Client;
import models.Site;
import play.data.validation.Constraints;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.List;

@SuppressWarnings("unchecked")
@BaseDao.CollectionName("sites")
@Singleton
public class SiteDao extends BaseDao {

    private ClientDao clientDao;

    @Inject
    public SiteDao(PlayJongo jongo, ClientDao clientDao){
        super(jongo);
        this.clientDao = clientDao;
    }

    public List<Site> findAll(String sortString){
        return (List<Site>) BaseDao.toList(mongoCollection.find().sort(sortString).as(Site.class));
    }

    // returns the record by the passed id
    public Site findById(String id){
        return (Site) findById(Site.class, id);
    }

    public List<Site> search(String searchTerm, String sortString){
        List<Site> results = (List<Site>) search(Site.class, searchTerm, sortString);
        if (results == null){
            // when no search is performed, we want to return all records
            return findAll(sortString);
        } else {
            return results;
        }
    }

    public List<Client> clients(String sortString){
        // Craig, I've started this for you, this returns all clients.
        // You need to change this to bring back only the associated clients
        // to this site, making using of the clientIds property of this class.
      return (List<Client>) BaseDao.toList(clientDao.mongoCollection.find().sort(sortString).as(Client.class));
    }
}
