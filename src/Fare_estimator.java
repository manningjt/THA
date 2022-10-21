import java.io.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.sql.Timestamp;


public class Fare_estimator {

    public static void main(String[] args) {

        String lat, lng, lat_line2, lng_line2, id_ride, timestamp, timestamp_line2,id_ride2;
        String result;
        double U,distance;
        int i = 0, SIZE_LIST = 2000, cont;
        double[] fare = new double[SIZE_LIST];
        double[] id_ride_final = new double[SIZE_LIST];
        Fare_estimator calculate = new Fare_estimator();
        BufferedReader buffer_read = null;

        try {
            // Open .csv in Buffered Reader
            buffer_read = new BufferedReader(new FileReader("/Users/a.manning/Downloads/paths 3.csv"));
            String line = buffer_read.readLine();

            while (line != null) {

                String[] fields_line1 = line.split(",");
                id_ride = fields_line1[0];
                lat = fields_line1[1];
                lng = fields_line1[2];
                timestamp = fields_line1[3];
                line = buffer_read.readLine();

                if (line == null) {
                    break;
                }

                String[] fields_line2 = line.split(",");
                id_ride2 = fields_line2[0];
                lat_line2 = fields_line2[1];
                lng_line2 = fields_line2[2];
                timestamp_line2 = fields_line2[3];
                float new_id_ride = Float.parseFloat(id_ride);
                float new_id_ride2 = Float.parseFloat(id_ride2);
                double new_lat = Double.parseDouble(lat);
                double new_lat_line2 = Double.parseDouble(lat_line2);
                double new_lng = Double.parseDouble(lng);
                double new_lng_line2 = Double.parseDouble(lng_line2);
                int new_timestamp = Integer.parseInt(timestamp);
                int new_timestamp_line2 = Integer.parseInt(timestamp_line2);
                double delta_time_seconds = (new_timestamp_line2 - new_timestamp);
                double delta_time_hours = delta_time_seconds / 3600;

                distance = calculate.calculate_speed(new_lat, new_lng, new_lat_line2, new_lng_line2);
                U = (distance / delta_time_hours);

            //if the speed is bigger than 100KM/h
            while (U > 100) {
                line = buffer_read.readLine();
                String[] fields_line4 = line.split(",");
                id_ride2 = fields_line4[0];
                lat_line2 = fields_line4[1];
                lng_line2 = fields_line4[2];
                timestamp_line2 = fields_line4[3];
                new_lat_line2 = Double.parseDouble(lat_line2);
                new_lng = Double.parseDouble(lng);
                new_lng_line2 = Double.parseDouble(lng_line2);
                new_timestamp_line2 = Integer.parseInt(timestamp_line2);
                delta_time_seconds = (new_timestamp_line2 - new_timestamp);
                delta_time_hours = delta_time_seconds / 3600;
                distance = calculate.calculate_speed(new_lat, new_lng, new_lat_line2, new_lng_line2);
                U = (distance / delta_time_hours);
            }

            calculate.fare_rules(distance, U, delta_time_hours, new_id_ride, new_timestamp, fare, id_ride_final, i);
            i++;

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            result =(calculate.prepare_result(fare, id_ride_final, i));
            if (buffer_read != null) {
                try {
                    buffer_read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                String route = "/Users/a.manning/Downloads/result.txt";
                String content = "id_ride, fare_estimate\n";
                File file = new File(route);
                // if the file doesn't exist is created
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                String[] fares = result.split(",");
                for(cont=0;cont<=8;cont++) {
                    bw.write(String.valueOf(cont+1)+ "  ,  ");
                    bw.write(String.valueOf(fares[cont] +"  \n"));
                }
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double calculate_speed(double new_lat, double new_lng, double new_lat_line2, double new_lng_line2) {
        double a, c, distance, delta_latitude, delta_longitude;
        int EARTH_RADIUS = 6371;


        delta_latitude = Math.toRadians(new_lat_line2 - new_lat);
        delta_longitude = Math.toRadians(new_lng_line2 - new_lng);
        new_lat = Math.toRadians(new_lat);
        new_lat_line2 = Math.toRadians(new_lat_line2);

        a = Math.sin(delta_latitude / 2) * Math.sin(delta_latitude / 2)+
                Math.cos(new_lat) * Math.cos(new_lat_line2) *
                        Math.sin(delta_longitude / 2) * Math.sin(delta_longitude / 2);
        c = 2 * Math.asin((Math.sqrt(a)));
        distance = (EARTH_RADIUS * c);

        return distance;
    }

    public void fare_rules(double distance, double U, double delta_time_hours, float new_id_ride, int new_timestamp, double fare1[], double id_ride_final1[], int i) {

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
            id_ride_final1[i] = new_id_ride;
            fare1[i] = fare_amount;
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
            id_ride_final1[i] = new_id_ride;
            fare1[i] = fare_amount;
        }
        // when the speed is bigger than 10KM and the movement of the vehicle is between 5AM  and 00 hour
        if (U > 10 && Hour.compareTo(hour2) > 0 && Hour.compareTo(hour3) < 0) {

            fare_amount = (0.74 * distance) ;
            id_ride_final1[i] = new_id_ride;
            fare1[i] = fare_amount;
        }
    }
    public String prepare_result (double[] fare2, double[] id_ride_final2, int i) {

        int  n = 0, m=0;
        double fare_estimate = 0, cont =1.0;
        String result = "0";
        double[] x = new double[9];
        double[] y = new double[9];
        System.out.println("id_ride | fare_estimate");
        while (cont <= id_ride_final2[i - 1]) {

            fare_estimate = 0;
            while (id_ride_final2[m] == cont) {
                    fare_estimate = fare_estimate + fare2[m];
                    m++;
            }

            fare_estimate = (fare_estimate + 1.30);

            if (fare_estimate < 3.47) {
                fare_estimate = 3.47;
            }
            DecimalFormat result_format = new DecimalFormat("#.##");
            System.out.print("    " + cont);
            System.out.println(" , " + result_format.format(fare_estimate));
            x[n] = cont;
            y[n] = fare_estimate;
            result = Arrays.toString(y);
            cont++;
            n++;
        }

        return result;

    }

}
