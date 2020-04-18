package ca.mcit.model

import org.apache.hadoop.fs.{FSDataInputStream, Path}
import scala.io.{BufferedSource, Source}

case class Calendar(service_id: String, monday: Int, tuesday: Int, wednesday: Int, thursday: Int,
                    friday: Int,saturday: Int,sunday: Int,start_date: Int,end_date: Int)

object Calendar {
  //The file on local system
//  val filePath = "/Users/minhle/Desktop/Projects/course3project/gtfs_stm/calendar.txt"
//  val source: BufferedSource = Source.fromFile(filePath)

  //The file on HDFS
  val filePath = new Path ("/user/fall2019/minhle/stm/calendar.txt")
  val stream: FSDataInputStream = Connection.fs.open(filePath)
  val source: BufferedSource = Source.fromInputStream(stream)

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

