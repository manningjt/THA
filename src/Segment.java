import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Segment {

    private double U, distance, delta_time_hours;

    private List<Point> points;
    private String id_ride;
    private String timestamp;
    private String timestamp_line2;

    ArrayList<Double> fare;
    public Segment(List<Point> points, String id_ride, String timestamp, String timestamp_line2,ArrayList<Double> fare) {
        this.points = points;
        this.id_ride = id_ride;
        this.timestamp = timestamp;
        this.timestamp_line2 = timestamp_line2;
        this.fare=fare;

    }

    public List<Point> getPoints() {
        return points;
    }

    public void calculate_distance() {
        int new_timestamp = Integer.parseInt(timestamp);


        for (Point pnt : points) {
            String latOrigin = pnt.lat;
            String lngOrigin = pnt.lng;
            String latDestination = pnt.lat_line2;
            String lngDestination = pnt.lng_line2;
            double new_lat = Double.parseDouble(latOrigin);
            double new_lng = Double.parseDouble(lngOrigin);
            double new_lat_destination = Double.parseDouble(latDestination);
            double new_lng_line2 = Double.parseDouble(lngDestination);
            int new_timestamp_line2 = Integer.parseInt((timestamp_line2));
            double delta_time_seconds = (new_timestamp_line2 - new_timestamp);
            delta_time_hours = delta_time_seconds / 3600;
            double delta_latitude = Math.toRadians(new_lat_destination - new_lat);
            double delta_longitude = Math.toRadians(new_lng_line2 - new_lng);
            double a = Math.sin(delta_latitude / 2) * Math.sin(delta_latitude / 2) +
                    Math.cos(Math.toRadians(new_lat)) * Math.cos(Math.toRadians(new_lat_destination)) *
                            Math.sin(delta_longitude / 2) * Math.sin(delta_longitude / 2);
            double c = 2 * Math.asin((Math.sqrt(a)));
            int EARTH_RADIUS = 6371;
            distance = (EARTH_RADIUS * c);
        }
    }

    public double calculate_speed() {
        U = (distance / delta_time_hours);
        return U;
    }
    public void validatePoint(String lat_line3, String lng_line3, String timestamp_line3){
        for (Point pnt : points) {
            pnt.lat_line2 =lat_line3;
            pnt.lng_line2 = lng_line3;
        }
        timestamp_line2 = timestamp_line3;

    }
    public void fare_rules(ArrayList<Double> id_ride_final, int i) {

        float new_id_ride = Float.parseFloat(id_ride);
        int new_timestamp = Integer.parseInt(timestamp);
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
        String hour1 = "000000";
        String hour2 = "050000";
        String hour3 = "235959";

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


