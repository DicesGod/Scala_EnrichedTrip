import ca.mcit.model.Calendar
import java.io.{BufferedWriter, FileWriter}
import au.com.bytecode.opencsv.CSVWriter

object EnrichedTrip extends App{

  //Convert list of Calendar to map
  val lookupCalendarMap: Map[String, Calendar] = Calendar.createCalendarsMap

  val tripRoute = new TripRoute()

  val enrichedTripsMondays = tripRoute.lookupRouteTripTable.collect{ case (service_id,route_id,trip_id,tripRoute)
    if lookupCalendarMap.contains(service_id) => (route_id,service_id,trip_id,tripRoute,lookupCalendarMap(service_id))}
    .filter(x => x._4._2.route_type == 1 && x._5.monday == 1).toSeq.sortBy(_._1) //Route Type of Metro = 1

    enrichedTripsMondays.foreach(x => println(x._4._2.route_type,x._5.monday,x._4._1))


  //  val enrichedTrips = tripRoute.lookupRouteTripTable.collect{ case (service_id,route_id,trip_id,tripRoute)
//    if lookupCalendarMap.contains(service_id) => (trip_id,service_id,route_id,
//    //Trip info
//      tripRoute._1.trip_headsign+","
//      +tripRoute._1.direction_id+","
//      +tripRoute._1.shape_id+","
//      +tripRoute._1.wheelchair_accessible+","
//      +tripRoute._1.note_fr+","
//      +tripRoute._1.note_en+","
//      //Route info
//      +tripRoute._2.agency_id+","
//        +tripRoute._2.route_short_name+","
//        +tripRoute._2.route_long_name+","
//        +tripRoute._2.route_type+","
//        +tripRoute._2.route_url+","
//        +tripRoute._2.route_color+","
//        +tripRoute._2.route_text_color+","
//      //Calenda info
//      , lookupCalendarMap(service_id).monday+","
//      +lookupCalendarMap(service_id).tuesday+","
//      +lookupCalendarMap(service_id).wednesday+","
//      +lookupCalendarMap(service_id).thursday+","
//      +lookupCalendarMap(service_id).friday+","
//      +lookupCalendarMap(service_id).saturday+","
//      +lookupCalendarMap(service_id).sunday+","
//      +lookupCalendarMap(service_id).start_date+","
//      +lookupCalendarMap(service_id).end_date+","
//    )
//  }
//
//  val outputFile = new BufferedWriter(new FileWriter("/Users/minhle/Desktop/Projects/scala-project1/gtfs_stm/enrichedTrip.csv"))
//  val csvWriter = new CSVWriter(outputFile)
//  val csvFields = List("trip_id", "service_id", "route_id", "trip_headsign","direction_id","shape_id","wheelchair_accessible","note_fr","note_en"
//  ,"agency_id","route_short_name","route_long_name","route_type", "route_url","route_color","route_text_color"
//  ,"monday","tuesday","wednesday","thursday","friday","saturday","sunday","start_date","end_date")
//
//  val ListEnrichedTrips = csvFields.mkString(",") + enrichedTrips.mkString("\n").replace("(", "")
//    .replace(")","")
//
//  csvWriter.writeNext(ListEnrichedTrips)
//  outputFile.close()
}
