package models;

import org.jongo.MongoCollection;
import play.data.validation.Constraints;
import java.util.List;
@SuppressWarnings("unchecked")
@Base.CollectionName("clients")
public class Client extends Base {

    @Constraints.Required
    public String name;

    public static MongoCollection clientsCollection = Base.getCollection(Client.class);

    public static List<Client> findAll(String sortString){
        return (List<Client>) Base.toList(clientsCollection.find().sort(sortString).as(Client.class));
    }

    public static List<Client> search(String searchTerm, String sortString){
        // can't use full text search until the mongo index stuff is sorted
        // https://github.com/neilwilliams/painting/issues/24
        return (List<Client>) Base.toList(clientsCollection.
            find("{name: {$regex: #, $options: 'i'}}", searchTerm).
            sort(sortString).
            as(Client.class));
    }

    public static Client findById(String id){
        return (Client) Base.findById(Client.class, id);
    }
}
