import ca.mcit.model.{Calendar, Connection}
import java.io.{BufferedWriter, OutputStreamWriter}
import au.com.bytecode.opencsv.CSVWriter
import org.apache.hadoop.fs.{FSDataOutputStream, Path}
import java.util.concurrent.TimeUnit

object EnrichedTrip extends App{
  try {
    val filePath = new Path("/user/fall2019/minhle/course3/enrichedTrip.csv")
    if (Connection.fs.exists(filePath)) {
      val deleteResult = Connection.fs.delete(new Path("/user/fall2019/minhle/course3"), true)
      println(deleteResult, "file exists, deleted the course3 directory and its content")
    }
    else {
      println("file does not exists,creating the course directory and its content")
    }

    //Wait 5 seconds to check deleting folder and its file,it is not necessary
    TimeUnit.SECONDS.sleep(5)

    //Convert list of Calendar to map
    val lookupCalendarMap: Map[String, Calendar] = Calendar.createCalendarsMap

    val tripRoute = new TripRoute()
    val enrichedTrips = tripRoute.lookupRouteTripTable.collect { case (service_id, route_id, trip_id, tripRoute)
      if lookupCalendarMap.contains(service_id) => (route_id, service_id, trip_id,
      //Trip info
      tripRoute._1.trip_headsign + ","
        + tripRoute._1.direction_id + ","
        + tripRoute._1.shape_id + ","
        + tripRoute._1.wheelchair_accessible + ","
        + tripRoute._1.note_fr + ","
        + tripRoute._1.note_en + ","
        //Route info
        + tripRoute._2.agency_id + ","
        + tripRoute._2.route_short_name + ","
        + tripRoute._2.route_long_name + ","
        + tripRoute._2.route_type + ","
        + tripRoute._2.route_url + ","
        + tripRoute._2.route_color + ","
        + tripRoute._2.route_text_color + ","
      //Calenda info
      , lookupCalendarMap(service_id).monday + ","
      + lookupCalendarMap(service_id).tuesday + ","
      + lookupCalendarMap(service_id).wednesday + ","
      + lookupCalendarMap(service_id).thursday + ","
      + lookupCalendarMap(service_id).friday + ","
      + lookupCalendarMap(service_id).saturday + ","
      + lookupCalendarMap(service_id).sunday + ","
      + lookupCalendarMap(service_id).start_date + ","
      + lookupCalendarMap(service_id).end_date + ","
    )
    }.toSeq.sortBy(_._1) //sort by route_id

    //Working on local machine
    //  val outputFile = new BufferedWriter(new FileWriter("/Users/minhle/Desktop/Projects/course3project/gtfs_stm/enrichedTrip.csv"))
    //  val csvWriter = new CSVWriter(outputFile)

    //Working on HDFS
    val stream: FSDataOutputStream = Connection.fs.create(filePath)
    val outputFile = new BufferedWriter(new OutputStreamWriter(stream))
    val csvWriter = new CSVWriter(outputFile,',',CSVWriter.NO_QUOTE_CHARACTER)
    val csvFields = List("route_id", "service_id", "trip_id", "trip_headsign", "direction_id", "shape_id", "wheelchair_accessible", "note_fr", "note_en"
      , "agency_id", "route_short_name", "route_long_name", "route_type", "route_url", "route_color", "route_text_color"
      , "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "start_date", "end_date")

    val ListEnrichedTrips: String = csvFields.mkString(",") + "\n" + enrichedTrips.mkString("\n")
      .replace("(", "")
      .replace(")", "")
    
    csvWriter.writeNext(ListEnrichedTrips)

    println("Created enrichedTrips.csv with: " + enrichedTrips.size + " records")

    outputFile.close()
    stream.close()
    csvWriter.close()
    Connection.fs.close()
    outputFile.close()
  }
  catch
    {
      case _: Exception => println("Connection error!")
    }
}
