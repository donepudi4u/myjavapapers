package com.myjavapapers.utils;

import java.util.HashMap;
import java.util.Map;

public class CollectionUtils {
	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < 10; i++) {
			if(map.get("one")!= null){
				Integer integer = map.get("one");
				//integer = integer+1;
				map.put("one", ++integer);
			}else {
				map.put("one", 1);
			}
			if(map.get("two")!= null){
				Integer integer = map.get("two");
				integer = integer+5;
				map.put("two", integer);
			}else {
				map.put("two", 5);
			}
		}
		/*System.out.println(map.put("One", 1));
		System.out.println(map.put("One", 2));
		if (map.put("One", 2) != null){
			Integer integer = map.get("One");
			System.out.println(integer);
			integer++;
			map.put("One", integer);
		}*/
		System.out.println(map);
	}
	
	/*private static void saveEmbeddedObject(DB cimAppsMongoDB,String fileName ) {
        try {
            //   Mongo mongo = new Mongo(MongoConstants.MONGO_SERVER, MongoConstants.MONGO_PORT);
            // Logger.getLogger(STRsave.class.getName()).info(  "mongo instance done");
            //   DB db1 = mongo.getDB("cimapps");

           // StringBuilder ObjName = new StringBuilder();
            //ObjName.append(str_number).append("-").append(item.getName()).append("-").append(embObj.getName());
         //   ObjName.append(str_number).append("-").append(item.getName()).append("-").append(embeddedObjectSequence);
            File docFile = new File(path);
            // create a "cimappsSTR" namespace
            GridFS gfscimapps = new GridFS(cimAppsMongoDB, "cimappsSTR1");
            // remove the image file from mongoDB
            // try query not on file name but on the id or use find for the _id
            //gfscimapps.remove(gfscimapps.findOne(ObjName.toString())); //this removes all previous GridFS entries even though it shouldnt
            // get doc file from local drive
            GridFSInputFile gfsFile = gfscimapps.createFile(docFile);
            // set a new filename for identify purpose
            gfsFile.setFilename(fileName);
            // save the image file into mongoDB
            gfsFile.save();
            // get image file by it's filename
            GridFSDBFile imageForOutput = gfscimapps.findOne(fileName);
            //imageForOutput.
        } catch (UnknownHostException e) {
            System.out.println("exception = " + e);
            Logger.getLogger(LotusNotes2Mongo.class.getName()).log(Level.SEVERE, null, e);
        } catch (MongoException | IOException | NotesException e) {
            System.out.println("exception = " + e);
            Logger.getLogger(LotusNotes2Mongo.class.getName()).log(Level.SEVERE, null, e);
        }
    }*/

}
