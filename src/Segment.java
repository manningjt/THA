import java.sql.Date;
import java.text.SimpleDateFormat;

public class Segment {

    private double U, distance, delta_time_hours;
    private double VALID_SPEED_LIMIT_KMH = 100.0;
    private Point destination;
    private Point origin;

    private double fare_amount;
    private long timestamp;



    public Segment(Point destination, Point origin, long timestamp,double fare_amount) {

        this.destination = destination;
        this.timestamp = timestamp;
        this.origin=origin;
        this.fare_amount=fare_amount;

    }
    public double getFare(){
        return this.fare_amount;
    }

    public double calculate_distance() {

        double delta_time_seconds = (destination.getTimestamp() - origin.getTimestamp());
        delta_time_hours = delta_time_seconds / 3600;
        double delta_latitude = Math.toRadians(destination.getLatitude() - origin.getLatitude());
        double delta_longitude = Math.toRadians(destination.getLongitude() - origin.getLongitude());
        double a = Math.sin(delta_latitude / 2) * Math.sin(delta_latitude / 2) +
                Math.cos(Math.toRadians(origin.getLatitude())) * Math.cos(Math.toRadians(destination.getLatitude())) *
                        Math.sin(delta_longitude / 2) * Math.sin(delta_longitude / 2);
        double c = 2 * Math.asin((Math.sqrt(a)));
        int EARTH_RADIUS = 6371;
        distance = (EARTH_RADIUS * c);
        return distance;

    }

    public double calculate_speed() {
        U = (distance / delta_time_hours);
        return U;
    }
    public boolean isValid (){

        return calculate_speed() > VALID_SPEED_LIMIT_KMH;
    }
    public double fare_rules( double U, double distance) {

        // when the speed is less than 10KM
        if (U <= 10) {
            fare_amount = 11.90 * delta_time_hours;
            if (fare_amount < 0.0) {
                fare_amount = 0.0;
            }
            if (fare_amount > 1000.0) {
                fare_amount = 0.0;
            }
        }
        //convert time seconds to microseconds
        Date date_and_hour = new Date( timestamp * 1000 );
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String Hour = sdf.format(date_and_hour);
        String hour1 = "000000";
        String hour2 = "050000";
        String hour3 = "235959";

        // when the speed is bigger than 10KM and the movement of the vehicle is between 00 hour and 5 AM
        if (U > 10 && Hour.compareTo(hour1) > 0 && Hour.compareTo(hour2) < 0) {

            fare_amount = (1.30 * distance) ;
        }
        // when the speed is bigger than 10KM and the movement of the vehicle is between 5AM  and 00 hour
        if (U > 10 && Hour.compareTo(hour2) > 0 && Hour.compareTo(hour3) < 0) {

            fare_amount = (0.74 * distance) ;
        }
        return fare_amount;
    }

}


