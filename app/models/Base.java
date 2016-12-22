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
@SuppressWarnings("unchecked")
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

    protected static MongoCollection getCollection(Class<?> subClass) {
      // get the collection name from the subclass annotation
      if (subClass.isAnnotationPresent(CollectionName.class)) {
          // get the collection from Mongo
          return jongo.getCollection(subClass.getAnnotation(CollectionName.class).value());
      }
      else {
          throw new IllegalArgumentException("No CollectionName anotation declared.");
      }
    }

    protected static List<?> toList(MongoCursor cursor){
        List objects = new ArrayList();
        for (Object obj : cursor) {
            objects.add(obj);
        }
        return objects;
    }

    // returns the record by the passed id
    protected static Object findById(Class<?> subClass, String id){
        if (id != null && !id.isEmpty()){
            return getCollection(subClass).findOne(new ObjectId(id)).as(subClass);
        }
        return null;
    }

    // Saves the data in this instance to the mongoDb
    public void save(){
        if (this.id == null){
            getCollection(this.getClass()).save(this);
        } else {
            // We can't use 'save' for updates, or the entire document will be updated with the new params
            // this wouldn't work when we have forms which don't capture the entire record
            // using 'update', only updates the fields we send through - lovely :)
            getCollection(this.getClass()).update(new ObjectId(this.id)).with(this);
        }
    }

    // Deletes the current object
    public void delete(){
        getCollection(this.getClass()).remove(new ObjectId(this.id));
    }
}
