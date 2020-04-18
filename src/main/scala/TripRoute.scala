import ca.mcit.model.{Route, Trip}

class TripRoute{
  //Convert list of Trips and Routes to maps
  val lookupTripMap: Map[(Int,String), Trip] = Trip.createTripsMap
  val lookupRouteMap: Map[Int, Route] = Route.createRoutesMap

  //Join the Trips and Routes by route_id
  val lookupRouteTripTable:Iterable[(String,Int,String,(Trip,Route))] = lookupTripMap.collect{ case ((route_id,trip_id),trip)
    if lookupRouteMap.contains(route_id) => (trip.service_id,route_id,trip_id,(trip, lookupRouteMap(route_id))) }
}
