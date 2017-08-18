
import com.beust.klaxon.JsonObject
import com.beust.klaxon.*;
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI

import spark.Spark.*;
import com.mongodb.client.MongoDatabase
import org.bson.Document
import com.mongodb.DBObject




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
        database.getCollection("test").insertOne(Document(jsonAsMap));

                res.status(200)
        "done"
            //save the body of the request\

    }
}

