package models;

import org.bson.types.ObjectId;
import org.jongo.Find;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import play.Play;
import play.data.validation.Constraints;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static models.Client.clients;

public class Site {

    public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);

    public static MongoCollection sites() {
        return jongo.getCollection("sites");
    }

    @MongoId // auto
    @MongoObjectId
    public String id;

    @Constraints.Required
    public String name;
    public String description;

    public static ArrayList<Site> search(String searchTerm){
        Find query;
        if (searchTerm == null || searchTerm.isEmpty()){
            query = sites().find();
        } else {
            query = sites().find("{$text: {$search: #}}", searchTerm);
        }
        query = query.sort("{name: 1}");

        MongoCursor<Site> cursor = query.as(Site.class);
        ArrayList<Site> sites = new ArrayList<>();
        for (Site site: cursor) {
            sites.add(site);
        }
        return sites;
    }

    // returns the record by the passed id
    public static Site findById(String id){
        if (id != null && !id.isEmpty()){
            return sites().findOne(new ObjectId(id)).as(Site.class);
        }
        return null;
    }

    // Saves the data in this instance to the mongoDb
    public void save(){
        if (this.id == null){
            sites().save(this);
        } else {
            // We can't use 'save' for updates, or the entire document will be updated with the new params
            // this wouldn't work when we have forms which don't capture the entire record
            // using 'update', only updates the fields we send through - lovely :)
            sites().update(new ObjectId(this.id)).with(this);
        }
    }

    public void delete(){
        sites().remove(new ObjectId(this.id));
    }

    public ArrayList<Client> clients(){
        return new ArrayList<>();
    }
}