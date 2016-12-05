package models;

import org.bson.types.ObjectId;
import org.jongo.Find;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import play.Play;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by nwillia2 on 05/12/2016.
 */
public class Base {
    @Target({TYPE})
    @Retention(RUNTIME)
    public @interface CollectionName {
        String value() default "";
    }

    @MongoId // auto
    @MongoObjectId
    public String id;

    private static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);

    protected static MongoCollection getCollection(String collectionName) {
        return jongo.getCollection(collectionName);
    }

    protected static List<?> find(Class<?> subClass, String queryString, String sortString, boolean emptyDefault){
        ArrayList objects = new ArrayList();
        Find query;
        if (queryString != null && !queryString.isEmpty()) {
            query = getCollection(getCollectionName(subClass)).find(queryString);
        } else {
            if (emptyDefault){
                return objects;
            } else {
                query = getCollection(getCollectionName(subClass)).find();
            }
        }
        if (sortString != null && !sortString.isEmpty()) query = query.sort(sortString);

        MongoCursor<?> cursor = query.as(subClass);
        for (Object obj : cursor) {
            objects.add(obj);
        }
        return objects;
    }

    protected static List<?> search(Class<?> subClass, String searchTerm, String sortString, boolean emptyDefault){
        ArrayList objects = new ArrayList<>();
        Find query;
        if (searchTerm != null && !searchTerm.isEmpty()){
            query = getCollection(getCollectionName(subClass)).find("{$text: {$search: #}}", searchTerm);
        } else {
            if (emptyDefault){
                return objects;
            } else {
                query = getCollection(getCollectionName(subClass)).find();
            }
        }
        if (sortString != null && !sortString.isEmpty()) query = query.sort(sortString);

        MongoCursor<?> cursor = query.as(subClass);
        for (Object obj: cursor) {
            objects.add(obj);
        }
        return objects;
    }

    // returns the record by the passed id
    protected static Object findById(Class<?> subClass, String id){
        if (id != null && !id.isEmpty()){
            return getCollection(getCollectionName(subClass)).findOne(new ObjectId(id)).as(subClass);
        }
        return null;
    }

    // Saves the data in this instance to the mongoDb
    public void save(){
        if (this.id == null){
            getCollection(getCollectionName(this.getClass())).save(this);
        } else {
            // We can't use 'save' for updates, or the entire document will be updated with the new params
            // this wouldn't work when we have forms which don't capture the entire record
            // using 'update', only updates the fields we send through - lovely :)
            getCollection(getCollectionName(this.getClass())).update(new ObjectId(this.id)).with(this);
        }
    }

    // Deletes the current object
    public void delete(){
        getCollection(getCollectionName(this.getClass())).remove(new ObjectId(this.id));
    }

    private static String getCollectionName(Class<?> subClass){
        // get the collectionname from the subclass anotation
        if (subClass.isAnnotationPresent(CollectionName.class)) {
            return subClass.getAnnotation(CollectionName.class).value();
        }
        else {
            throw new IllegalArgumentException("No CollectionName anotation declared.");
        }
    }
}
