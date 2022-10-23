import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Segment extends Point {

    private  String lat_destination , lng_destination , id_ride_destination, timestamp_destination;
    private double U, distance, delta_time_hours;
    private  int new_timestamp = Integer.parseInt(timestamp);
    private  float new_id_ride = Float.parseFloat(id_ride);
    private  double new_lng = Double.parseDouble(lng);
    private double new_lat = Double.parseDouble(lat);

    public Segment(String lat,
                   String lng,
                   String id_ride,
                   String timestamp,
                   String lat_destination,
                   String lng_destination,
                   String id_ride_destination,
                   String timestamp_destination) {
        super(lat, lng, id_ride, timestamp);

        this.lat_destination = lat_destination;
        this.lng_destination = lng_destination;
        this.id_ride_destination = id_ride_destination;
        this.timestamp_destination = timestamp_destination;

    }

    public double calculate_speed() {

        double new_lng_line2 = Double.parseDouble(lng_destination);
        double new_lat_destination = Double.parseDouble(lat_destination);
        int    new_timestamp_line2 = Integer.parseInt((timestamp_destination));

        double delta_time_seconds = (new_timestamp_line2 - new_timestamp);
        delta_time_hours = delta_time_seconds / 3600;
        double delta_latitude = Math.toRadians(new_lat_destination-new_lat );
        double delta_longitude = Math.toRadians(new_lng_line2 - new_lng);
        double a = Math.sin(delta_latitude / 2) * Math.sin(delta_latitude / 2) +
                Math.cos(Math.toRadians(new_lat)) * Math.cos(Math.toRadians(new_lat_destination)) *
                        Math.sin(delta_longitude / 2) * Math.sin(delta_longitude / 2);
        double c = 2 * Math.asin((Math.sqrt(a)));
        int EARTH_RADIUS = 6371;
        distance = (EARTH_RADIUS * c);
        U = (distance / delta_time_hours);
        return U;
    }

    public void validatePoint(String id_ride3, String lat_line3, String lng_line3, String timestamp_line3) {

            lat_destination = lat_line3;
            lng_destination = lng_line3;
            timestamp_destination = timestamp_line3;

    }

    public void fare_rules(ArrayList<Double> fare, ArrayList<Double> id_ride_final, int i) {

        double fare_amount = 0.0;
        // when the speed is less than 10KM
        if (U <= 10) {
            fare_amount = 11.90 * delta_time_hours;
            if (fare_amount < 0.0) {
                fare_amount = 0.0;
            }
            if (fare_amount > 1000.0) {
                fare_amount = 0.0;
            }
            id_ride_final.add(i, (double) new_id_ride);
            fare.add(i, fare_amount);
        }

        long time = Long.parseLong(String.valueOf(new_timestamp));
        //convert time seconds to microseconds
        Date date_and_hour = new Date( time * 1000 );
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String Hour = sdf.format(date_and_hour);
        String hour1 = new String("000000");
        String hour2 = new String("050000");
        String hour3 = new String("235959");

        // when the speed is bigger than 10KM and the movement of the vehicle is between 00 hour and 5 AM
        if (U > 10 && Hour.compareTo(hour1) > 0 && Hour.compareTo(hour2) < 0) {

            fare_amount = (1.30 * distance) ;
            id_ride_final.add(i, (double) new_id_ride);
            fare.add(i, fare_amount);
        }
        // when the speed is bigger than 10KM and the movement of the vehicle is between 5AM  and 00 hour
        if (U > 10 && Hour.compareTo(hour2) > 0 && Hour.compareTo(hour3) < 0) {

            fare_amount = (0.74 * distance) ;
            id_ride_final.add(i, (double) new_id_ride);
            fare.add(i, fare_amount);
        }
    }
}