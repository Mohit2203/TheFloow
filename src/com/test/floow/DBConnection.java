package com.test.floow;
import com.mongodb.DB;
import com.mongodb.MongoClient;
public class DBConnection {
	
	/**
	 * @author Mohit
	 * @param hostname
	 * @param port
	 * @return
	 */
	public static DB connectDB(String hostname,int port) {
		
		// Creating a Mongo client 
	      MongoClient mongo = new MongoClient(hostname,port); 
	   
	      // Creating Credentials 
		/*
		  MongoCredential credential; credential =
		  MongoCredential.createCredential("sampleUser", "myDb",
		  "password".toCharArray());
		 */
	      DB db = mongo.getDB("theFloowDb");

		 return db;
	}

}
