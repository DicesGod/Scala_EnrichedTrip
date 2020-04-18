package ca.mcit.model

import org.apache.hadoop.fs.{FSDataInputStream, Path}
import scala.io.{BufferedSource, Source}

case class Route(route_id: Int, agency_id: String, route_short_name: Int, route_long_name: String, route_type: Int,
                 route_url: String,route_color: String,route_text_color: String)

object Route {
  //The file on your local filesystem
//  val filePath = "/Users/minhle/Desktop/Projects/course3project/gtfs_stm/routes.txt"
//  val source: BufferedSource = Source.fromFile(filePath)

  //The file on HDFS
  val filePath = new Path ("/user/fall2019/minhle/stm/routes.txt")
  val stream: FSDataInputStream = Connection.fs.open(filePath)
  val source: BufferedSource = Source.fromInputStream(stream)

  def extractRoutes: Iterator[Route] = {
    //reading routes from csv file to a list
    source.getLines().drop(1)
      .map(line => line.split(",", -1))
      .map(a => Route(a(0).toInt, a(1), a(2).toInt, a(3), a(4).toInt, a(5), a(6), a(7)))
    //.foreach(route => println(route))
  }

  def createRoutesMap:Map[Int, Route] = {
    extractRoutes.map(route => route.route_id -> route).toMap
  }
}