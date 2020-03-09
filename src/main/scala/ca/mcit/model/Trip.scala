package ca.mcit.model

import scala.io.{BufferedSource, Source}

case class Trip(route_id: Int, service_id: String, trip_id: String, trip_headsign: String, direction_id: Int, shape_id: Int,
                wheelchair_accessible: Int, note_fr: String, note_en: String)
object Trip {
  //where is the file on your local filesystem
  val filePath = "/Users/minhle/Desktop/Projects/scala-project1/gtfs_stm/trips.txt"
  val source: BufferedSource = Source.fromFile(filePath)

  def extractTrips: Iterator[Trip] = {
    source.getLines().drop(1)
      .map(line => line.split(",",-1))
      .map(a => Trip(a(0).toInt, a(1), a(2), a(3), a(4).toInt, a(5).toInt, a(6).toInt
        , a(7),a(8)))
  }
  //source.close()

  def createTripsMap:Map[(Int,String), Trip] = {
    extractTrips.map(trip => (trip.route_id,trip.trip_id) -> trip).toMap
  }
}

