package models;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

/**
 * Created by nwillia2 on 05/12/2016
 */
@SuppressWarnings("unchecked")
public class Base {
    @MongoId // auto
    @MongoObjectId
    public String id;
}
