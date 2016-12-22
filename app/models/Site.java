package models;

import play.data.validation.Constraints;
import java.util.List;

@Base.CollectionName("sites")
public class Site extends Base {

    @Constraints.Required
    public String name;
    public String description;
    public List<int> clientIds;

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
      // something like this?  But converting it to a string like this is rubbish
      // string clientIds = this.clientIds.toString();
      // return Client.find("{'id': {$in: clientIds}}", "{"name": 1}", true);

      // We need a way to pass through the dynamic query values through to the find method
      // so Base.find needs some work
    }
}
