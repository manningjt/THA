import java.text.DecimalFormat;
import java.util.ArrayList;

public class FileData {
    private Rides ride;
    ArrayList<Double> id_ride_final1 = new ArrayList<>();
    ArrayList<Double> fare_total = new ArrayList<>();

    public FileData(Rides ride) {
        this.ride=ride;
    }
    public String showResult(ArrayList<Double> id_ride_final, ArrayList<Double> fare, int i){

        int n = 0, m = 0;
        double cont = 1.0;
        String result = "0";

        System.out.println("id_ride | fare_estimate");

        while (cont <=  id_ride_final.get(i - 1)) {
            double fare_estimate = 1.30;

            while (id_ride_final.get(m) == cont) {
                fare_estimate = fare_estimate + fare.get(m);
                m++;
                if (m == id_ride_final.size()) {
                    break;
                }
            }

            if (fare_estimate < 3.47) {
                fare_estimate = 3.47;
            }
            DecimalFormat result_format = new DecimalFormat("#.##");
            System.out.print("    " + cont);
            System.out.println(" , " + result_format.format(fare_estimate));
            id_ride_final1.add(n, cont);
            fare_total.add(n, fare_estimate);
            result = String.valueOf(fare_total);
            cont++;
            n++;
        }

        return result;

    }
}
