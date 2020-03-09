package ca.mcit.model

import scala.io.{BufferedSource, Source}

case class Route(route_id: Int, agency_id: String, route_short_name: Int, route_long_name: String, route_type: Int,
                 route_url: String,route_color: String,route_text_color: String)
object Route {
  //where is the file on your local filesystem
  val filePath = "/Users/minhle/Desktop/Projects/scala-project1/gtfs_stm/routes.txt"
  val source: BufferedSource = Source.fromFile(filePath)

  def extractRoutes: Iterator[Route] = {
    //reading routes from csv file to a list
    source.getLines().drop(1)
      .map(line => line.split(",", -1))
      .map(a => Route(a(0).toInt, a(1), a(2).toInt, a(3), a(4).toInt, a(5), a(6), a(7)))
    //.foreach(route => println(route))
  }
  //source.close()

  def createRoutesMap:Map[Int, Route] = {
    extractRoutes.map(route => route.route_id -> route).toMap
  }
}