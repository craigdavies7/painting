package models.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Base;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;
import play.inject.Injector;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by nwillia2 on 05/12/2016
 */
@SuppressWarnings("unchecked")
@Singleton
public class BaseDao {
    @Target({TYPE})
    @Retention(RUNTIME)
    public @interface CollectionName {
        String value() default "";
    }

    protected PlayJongo jongo;
    public MongoCollection mongoCollection;

    @Inject
    public BaseDao(PlayJongo jongo){
        this.jongo = jongo;
        mongoCollection = getCollection();
    }

    protected MongoCollection getCollection() {
      // get the collection name from the subclass annotation
      if (this.getClass().isAnnotationPresent(CollectionName.class)) {
          // get the collection from Mongo
          return jongo.getCollection(this.getClass().getAnnotation(CollectionName.class).value());
      }
      else {
          throw new IllegalArgumentException("No CollectionName annotation declared.");
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
    public Object findById(Class<?> baseClass, String id){
        if (id != null && !id.isEmpty()){
            return mongoCollection.findOne(new ObjectId(id)).as(baseClass);
        }
        return null;
    }

    // Saves the data in this instance to the mongoDb
    public void save(Base base){
        if (base.id == null){
            getCollection().save(base);
        } else {
            // We can't use 'save' for updates, or the entire document will be updated with the new params
            // this wouldn't work when we have forms which don't capture the entire record
            // using 'update', only updates the fields we send through - lovely :)
            mongoCollection.update(new ObjectId(base.id)).with(base);
        }
    }

    // Deletes the current object
    public void delete(Base base){mongoCollection.remove(new ObjectId(base.id));
    }
}
