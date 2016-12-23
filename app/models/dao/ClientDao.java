package models.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Base;
import models.Client;
import org.bson.types.ObjectId;
import play.data.validation.Constraints;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.List;

@SuppressWarnings("unchecked")
@BaseDao.CollectionName("clients")
@Singleton
public class ClientDao extends BaseDao {

    @Inject
    public ClientDao(PlayJongo jongo){
        super(jongo);
    }

    public List<Client> findAll(String sortString){
        return (List<Client>) BaseDao.toList(mongoCollection.find().sort(sortString).as(Client.class));
    }

    // returns the record by the passed id
    public Client findById(String id){
        return (Client) findById(Client.class, id);
    }

    public List<Client> search(String searchTerm, String sortString){
        // can't use full text search until the mongo index stuff is sorted
        // https://github.com/neilwilliams/painting/issues/24
        return (List<Client>) BaseDao.toList(mongoCollection.
            find("{name: {$regex: #, $options: 'i'}}", searchTerm).
            sort(sortString).
            as(Client.class)
        );
    }
}
