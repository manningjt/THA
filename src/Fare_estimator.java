import java.io.*;
import java.util.ArrayList;
public class Fare_estimator {

    public static void main(String[] args) {

        String lat, lng, lat_line2, lng_line2, id_ride, timestamp, timestamp_line2, id_ride2;
        String result;
        int i=0, cont;
        double U;

        ArrayList<Double> fare = new ArrayList<>();
        ArrayList<Double> id_ride_final = new ArrayList<>();
        BufferedReader buffer_read = null;
        Rides calculate = null;
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
                Point pointData = new Point(lat, lng, id_ride, timestamp, lat_line2, lng_line2, id_ride2, timestamp_line2);
                Segment destinationData = new Segment(lat, lng, id_ride, timestamp,lat_line2,lng_line2, id_ride2,timestamp_line2);
                U = destinationData.calculate_speed();
                //if the speed is bigger than 100KM/h
                while (U > 100) {
                    line = buffer_read.readLine();
                    String[] fields_line4 = line.split(",");
                    String id_ride3 = fields_line4[0];
                    String lat_line3 = fields_line4[1];
                    String lng_line3 = fields_line4[2];
                    String timestamp_line3 = fields_line4[3];
                    destinationData.validatePoint(id_ride3, lat_line3, lng_line3, timestamp_line3);
                    U = destinationData.calculate_speed();
                }
                destinationData.fare_rules(fare, id_ride_final,i);
                calculate = new Rides();
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
                    result = calculate.prepare_result(fare, id_ride_final,i);
                    if (buffer_read != null) {
                        try {
                            buffer_read.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                            bw.write("    " + String.valueOf(cont + 1) + "   ,  ");
                            bw.write(String.valueOf(fares[cont] + "  \n"));
                        }
                        bw.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        }
    }
}