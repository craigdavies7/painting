package models.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Client;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.ArrayList;
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

    public List<Client> searchOrAll(String searchTerm, String sortString){
        List<Client> results = (List<Client>) search(Client.class, searchTerm, sortString);
        if (results == null){
            // when no search is performed, we want to return all records
            return findAll(sortString);
        } else {
            return results;
        }
    }

    public List<Client> searchOrNone(String searchTerm, String sortString){
        List<Client> results = (List<Client>) search(Client.class, searchTerm, sortString);
        if (results == null){
            // when no search is performed, return an empty list
            return  new ArrayList<>();
        } else {
            return results;
        }
    }
}
