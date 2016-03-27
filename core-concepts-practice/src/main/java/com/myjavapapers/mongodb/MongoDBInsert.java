package com.myjavapapers.mongodb;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;


public class MongoDBInsert {
	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost",27017);
		MongoDatabase database = mongoClient.getDatabase("test");
		 System.out.println("Connect to database successfully");
		 System.out.println(database.getName());
		 MongoCollection<Document> collection = database.getCollection("myCollection");
		 System.out.println(collection);
	}

}
