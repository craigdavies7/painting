package models;

import org.jongo.MongoCollection;
import play.data.validation.Constraints;
import java.util.List;
@SuppressWarnings("unchecked")
@Base.CollectionName("sites")
public class Site extends Base {

    @Constraints.Required
    public String name;
    public String description;
    public List<Integer> clientIds;

    public static MongoCollection sitesCollection = Base.getCollection(Site.class);

    public static List<Site> search(String searchTerm, String sortString){
        // can't use full text search until the mongo index stuff is sorted
        // https://github.com/neilwilliams/painting/issues/24
        if (searchTerm == null) searchTerm = "";
        return (List<Site>) Base.toList(sitesCollection.
                find("{name: {$regex: #, $options: 'i'}}", searchTerm).
                sort(sortString).
                as(Site.class));
    }

    public static Site findById(String id){
        return (Site) Base.findById(Site.class, id);
    }

    public List<Client> clients(String sortString){
        // Craig, I've started this for you, this returns all clients.
        // You need to change this to bring back only the associated clients
        // to this site, making using of the clientIds property of this class.
      return (List<Client>) Base.toList(Client.clientsCollection.find("{site:#}", this.id).sort(sortString).as(Client.class));
    }
}
