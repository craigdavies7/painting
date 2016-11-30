package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import play.*;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.ArrayList;

public class Client {

    public static PlayJongo jongo = Play.application().injector().instanceOf(PlayJongo.class);

    public static MongoCollection clients() {
        return jongo.getCollection("clients");
    }

    @JsonProperty("_id")
    public ObjectId id;
    public String name;

    public static ArrayList<Client> findAll() {
        MongoCursor<Client> cursor = clients().find().as(Client.class);
        ArrayList<Client> clients = new ArrayList<Client>();
        for (Client client: cursor) {
            clients.add(client);
        }
        return clients;
    }
}