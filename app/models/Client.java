package models;

import play.modules.mongo.MongoEntity;
import play.modules.mongo.MongoModel;

@MongoEntity("clients")
public class Client extends MongoModel {

	public String first_name;
	public String surname;
	
}