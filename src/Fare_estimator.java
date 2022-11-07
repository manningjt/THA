import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Fare_estimator {

    public static void main(String[] args) {


        String result;
        int i=0, cont;
        double U,distance,fare_amount =0;
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
                double id_ride = Double.parseDouble(fields_line1[0]);
                double latitude = Double.parseDouble(fields_line1[1]);
                double longitude = Double.parseDouble(fields_line1[2]);
                long   timestamp = Long.parseLong(fields_line1[3]);
                Point origin = new Point(latitude,longitude,timestamp);
                origin.getLatitude();
                origin.getLongitude();
                origin.getTimestamp();
                line = buffer_read.readLine();

                if (line == null) {
                    break;
                }
                String[] fields_line2 = line.split(",");
                id_ride = Double.parseDouble(fields_line2[0]);
                latitude = Double.parseDouble(fields_line2[1]);
                longitude = Double.parseDouble(fields_line2[2]);
                timestamp = Long.parseLong(fields_line2[3]);
                Point destination = new Point(latitude,longitude,timestamp);
                destination.getLatitude();
                destination.getLongitude();
                destination.getTimestamp();
                Segment calculateData = new Segment(destination,origin,timestamp,fare_amount);
                distance = calculateData.calculate_distance();
                U=calculateData.calculate_speed();
                //if the speed is bigger than 100KM/h
                while (calculateData.isValid() == true) {
                    line = buffer_read.readLine();
                    String[] fields_line4 = line.split(",");
                    latitude = Double.parseDouble(fields_line4[1]);
                    longitude = Double.parseDouble(fields_line4[2]);
                    timestamp = Long.parseLong(fields_line4[3]);
                    destination = new Point(latitude,longitude,timestamp);
                    destination.getLatitude();
                    destination.getLongitude();
                    destination.getTimestamp();
                    calculateData = new Segment(destination,origin,timestamp,fare_amount);
                    distance=calculateData.calculate_distance();
                    U=calculateData.calculate_speed();
                    calculateData.isValid();
                }
                fare_amount=calculateData.fare_rules(U,distance);
                Segment segments = new Segment (destination,origin,timestamp,fare_amount);
                ride = new Rides(segments,id_ride_final,fare);
                ride.calculateFare(i,id_ride);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            FileData total = new FileData(ride);
            result = total.showResult(id_ride_final,fare,i);
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