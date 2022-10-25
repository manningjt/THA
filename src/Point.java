public class Point {

    public String latOrigin,
            lngOrigin,
            id_rideOrigin,
            timestampOrigin,
            latDestination,
            lngDestination,
            id_rideDestination,
            timestampDestination;

    public Point (String lat,String lng,String id_ride,String timestamp,String lat_line2,String lng_line2,String id_ride2,String timestamp_line2){

        this.latOrigin = lat;
        this.lngOrigin = lng;
        this.id_rideOrigin = id_ride;
        this.timestampOrigin = timestamp;
        this.latDestination = lat_line2;
        this.lngDestination = lng_line2;
        this.id_rideDestination = id_ride2;
        this.timestampDestination = timestamp_line2;


    }



}
