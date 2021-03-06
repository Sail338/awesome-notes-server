
import com.beust.klaxon.JsonObject
import com.beust.klaxon.*;
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI

import spark.Spark.*;
import com.mongodb.client.MongoDatabase
import org.bson.Document
import com.mongodb.DBObject
import com.mongodb.client.model.Filters.eq


/**
 * Created by hari on 8/8/17.
 */

fun main(args: Array<String>) {
    val  parser: Parser = Parser()
    val dbinfo = parser.parse("databaseinfo.json") as JsonObject
    val dbuser:String? = dbinfo.string("username");
    val dbpass:String? = dbinfo.string("password")
    //initialize connection to the database
   val uri:MongoClientURI = MongoClientURI("mongodb://" + dbuser +":" + dbpass + "@cluster0-shard-00-00-h3ga7.mongodb.net:27017,cluster0-shard-00-01-h3ga7.mongodb.net:27017,cluster0-shard-00-02-h3ga7.mongodb.net:27017/<DATABASE>?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin")
    val client:MongoClient = MongoClient(uri);
    val database = client.getDatabase("test")
    print(dbinfo.toString())
    get("/firstuse") { req, res ->

    }
    post("/data") { req, res ->
        print(req.body())
        //store the body in the database
        //connect to mongo atlas
        val stringBuilder: StringBuilder = StringBuilder(req.body())
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        val jsonAsMap = json.toMap();
        //have a users collection
        val userid : String? = json.string("id")
        ///query the database
        val x =database.getCollection("users").find(eq("userid",userid)).count()
        if(x >0){
            //if the user id exists insert into the collection for the data
            //first update the data
            val ob:BasicDBObject = BasicDBObject().append("userid",userid)
            database.getCollection("data").updateOne(ob,Document(jsonAsMap))
        }


                res.status(200)
        "done"
            //save the body of the request\

    }

    post("/getdata") { req, res ->
        print(req.body())
        //store the body in the database
        //connect to mongo atlas
        val stringBuilder: StringBuilder = StringBuilder(req.body())
        val json: JsonObject = parser.parse(stringBuilder) as JsonObject
        val jsonAsMap = json.toMap();
        //have a users collection
        val userid : String? = json.string("id")
        ///query the database
        val x  =database.getCollection("data").find(eq("userid",userid)).first()

        res.status(200)
        x.toJson().toString()
        //save the body of the request\

    }
}

