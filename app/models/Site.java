package models;

import play.data.validation.Constraints;
import java.util.List;


@Base.CollectionName("sites")
public class Site extends Base {

    @Constraints.Required
    public String name;
    public String description;

    public static List<Site> find(String queryString, String sortString, boolean emptyDefault){
        return (List<Site>) Base.find(Site.class, queryString, sortString, emptyDefault);
    }

    public static List<Site> search(String searchTerm, String sortString, boolean emptyDefault){
        return (List<Site>) Base.search(Site.class, searchTerm, sortString, emptyDefault);
    }

    public static Site findById(String id){
        return (Site) Base.findById(Site.class, id);
    }

    public List<Client> clients(){
        return Client.find(null, null, true);
    }
}