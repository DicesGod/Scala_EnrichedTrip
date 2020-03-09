package ca.mcit.model

import scala.io.{BufferedSource, Source}

case class Calendar(service_id: String, monday: Int, tuesday: Int, wednesday: Int, thursday: Int,
                    friday: Int,saturday: Int,sunday: Int,start_date: Int,end_date: Int)
object Calendar {
  //where is the file on your local filesystem
  val filePath = "/Users/minhle/Desktop/Projects/scala-project1/gtfs_stm/calendar.txt"
  val source: BufferedSource = Source.fromFile(filePath)

  def extractCalendars: Iterator[Calendar] = {
    source.getLines().drop(1)
      .map(line => line.split(",",-1))
      .map(a => Calendar(a(0), a(1).toInt, a(2).toInt, a(3).toInt, a(4).toInt, a(5).toInt, a(6).toInt
        , a(7).toInt, a(8).toInt, a(9).toInt))
  }

  def createCalendarsMap:Map[String, Calendar] = {
    extractCalendars.map(calendar => calendar.service_id -> calendar).toMap
  }
}

