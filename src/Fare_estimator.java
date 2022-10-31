import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Fare_estimator {

    public static void main(String[] args) {

        String lat, lng, lat_line2, lng_line2, id_ride, timestamp, timestamp_line2, id_ride2;
        String result;
        int i=0, cont;
        double U;
        ArrayList<Double> fare = new ArrayList<>();
        ArrayList<Double> id_ride_final = new ArrayList<>();

        BufferedReader buffer_read = null;
        Rides ride =null;
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
                Point pointData = new Point(lat,lng,lat_line2, lng_line2);
                List<Point> pointOrigin = new ArrayList<>();
                pointOrigin.add(pointData);
                List<Point> pointDestination = new ArrayList<>();
                pointDestination.add(pointData);
                Segment pointsData = new Segment(pointOrigin,id_ride,timestamp,timestamp_line2,fare);
                List<Segment> segments = new ArrayList<>();
                segments.add(pointsData);
                Segment calculateData = new Segment(pointOrigin,id_ride,timestamp,timestamp_line2,fare);
                calculateData.calculate_distance();
                U = calculateData.calculate_speed();
                //if the speed is bigger than 100KM/h
                while (U > 100) {
                    line = buffer_read.readLine();
                    String[] fields_line4 = line.split(",");
                    String id_ride3 = fields_line4[0];
                    String lat_line3 = fields_line4[1];
                    String lng_line3 = fields_line4[2];
                    String timestamp_line3 = fields_line4[3];
                    calculateData.validatePoint( lat_line3,lng_line3,timestamp_line3);
                    calculateData.calculate_distance();
                    U = calculateData.calculate_speed();
                }
                calculateData.fare_rules(id_ride_final,i);
                ride = new Rides(segments);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            assert ride != null;
            result = ride.prepare_result(id_ride_final,i);
            try {
                buffer_read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String route = "/Users/a.manning/Downloads/result.txt";
                String content = "id_ride | fare_estimate\n";
                File file = new File(route);
                // if the file doesn't exist is created
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                String[] fares = result.split(",");

                for (cont = 0; cont <= 8; cont++) {
                    bw.write("    " + (cont + 1) + "   ,  ");
                    bw.write((fares[cont] + "  \n"));
                }
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}