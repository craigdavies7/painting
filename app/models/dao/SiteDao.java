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

    // returns the record by the passed id
    public Site findById(String id){
        return (Site) findById(Site.class, id);
    }

    public List<Site> search(String searchTerm, String sortString){
        // can't use full text search until the mongo index stuff is sorted
        // https://github.com/neilwilliams/painting/issues/24
        if (searchTerm == null) searchTerm = "";
        return (List<Site>) BaseDao.toList(mongoCollection.
                find("{name: {$regex: #, $options: 'i'}}", searchTerm).
                sort(sortString).
                as(Site.class));
    }

    public List<Client> clients(String sortString){
        // Craig, I've started this for you, this returns all clients.
        // You need to change this to bring back only the associated clients
        // to this site, making using of the clientIds property of this class.
      return (List<Client>) BaseDao.toList(clientDao.mongoCollection.find().sort(sortString).as(Client.class));
    }
}
