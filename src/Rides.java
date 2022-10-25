import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Rides {


    ArrayList<Double> fare_total = new ArrayList<>();
    ArrayList<Double> id_ride_final1 = new ArrayList<>();
    private double fare_estimate;
    public Rides (){

        this.fare_estimate = fare_estimate;

    }

    public String prepare_result (ArrayList<Double> fare, ArrayList<Double> id_ride_final, int i) {

         int  n = 0, m=0;
         double  cont =1.0;
        String result = "0";

         System.out.println("id_ride | fare_estimate");

        while (cont <= id_ride_final.get(i-1)) {

          fare_estimate = 0;
          while (id_ride_final.get(m) == cont && m<(i-1)) {
                  fare_estimate = fare_estimate + fare.get(m);
                  m++;
          }

          fare_estimate = (fare_estimate + 1.30);

          if (fare_estimate < 3.47) {
             fare_estimate = 3.47;
          }
          DecimalFormat result_format = new DecimalFormat("#.##");
          System.out.print("    " + cont);
          System.out.println(" , " + result_format.format(fare_estimate));
          id_ride_final1.add(n,cont);
          fare_total.add(n,fare_estimate);
          result = String.valueOf(fare_total);
           cont++;
           n++;
        }

         return result;

    }

}
