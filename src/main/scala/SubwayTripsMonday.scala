import java.io.{BufferedWriter, OutputStreamWriter}

import au.com.bytecode.opencsv.CSVWriter
import ca.mcit.model.{Calendar, Connection, Route, Trip}
import org.apache.hadoop.fs.{FSDataOutputStream, Path}

object SubwayTripsMonday extends App{
  //Convert list of Calendar to map
  val lookupCalendarMap: Map[String, Calendar] = Calendar.createCalendarsMap

  val tripRoute = new TripRoute()
  //println(tripRoute.lookupRouteMap)

  val enrichedSubwayTripsMondays: Seq[(Int, String, String, (Trip, Route), Calendar)] = tripRoute.lookupRouteTripTable.collect{ case (service_id,route_id,trip_id,tripRoute)
    if lookupCalendarMap.contains(service_id) => (route_id,service_id,trip_id,tripRoute,lookupCalendarMap(service_id))}
    .filter(x => x._4._2.route_type == 1 && x._5.monday == 1).toSeq.sortBy(_._1) //Route Type of Metro = 1

  //enrichedSubwayTripsMondays.foreach(x => println(x._4._2.route_type,x._5.monday,x._4._1))

  val subwayTripsMonday: Seq[(Int, Int, String)] = enrichedSubwayTripsMondays.map(x => (x._4._2.route_type,x._5.monday,
    x._4._1.route_id+","
      +x._4._1.service_id+","
      +x._4._1.trip_id+","
      +x._4._1.trip_headsign+","
      +x._4._1.direction_id+","
      +x._4._1.shape_id+","
      +x._4._1.wheelchair_accessible+","
      +x._4._1.wheelchair_accessible+","
      +x._4._1.note_fr+","
      +x._4._1.note_en+","
  ))

  //Working on LocalMachine
//  val outputFile = new BufferedWriter(new FileWriter("/Users/minhle/Desktop/Projects/course3project/gtfs_stm/subwayTripsMonday.csv"))
//  val csvWriter = new CSVWriter(outputFile)

  //Working on HDFS
  val filePath = new Path ("/user/fall2019/minhle/course3/subwayTripsMonday.csv")
  val stream: FSDataOutputStream = Connection.fs.create(filePath)
  val outputFile = new BufferedWriter(new OutputStreamWriter(stream))
  val csvWriter = new CSVWriter(outputFile)

  ////Route Type of Metro = 1 and Monday = 1
  val csvFields = List("route_type","monday","route_id", "service_id", "trip_id", "trip_headsign","direction_id","shape_id","wheelchair_accessible","note_fr","note_en")
  val ListSubwayTripsMondays: String = csvFields.mkString(",") + "\n" + subwayTripsMonday.mkString("\n")
    .replace("(", "")
    .replace(")","")

  //enrichedTripsMondays.foreach(println)

  csvWriter.writeNext(ListSubwayTripsMondays)
  println("Created subwayTripsMonday.csv with: " +subwayTripsMonday.size+" records")

  outputFile.close()
  stream.close()
  csvWriter.close()
  Connection.fs.close()
}
