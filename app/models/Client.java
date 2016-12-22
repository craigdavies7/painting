package models;

import play.data.validation.Constraints;
import java.util.List;

@Base.CollectionName("clients")
public class Client extends Base {

    @Constraints.Required
    public String name;

    public static List<Client> find(String queryString, String sortString, boolean emptyDefault){
        return (List<Client>) Base.find(Client.class, queryString, sortString, emptyDefault);
    }

    public static List<Client> search(String searchTerm, String sortString, boolean emptyDefault){
        return (List<Client>) Base.search(Client.class, searchTerm, sortString, emptyDefault);
    }

    public static Client findById(String id){
        return (Client) Base.findById(Client.class, id);
    }
}
