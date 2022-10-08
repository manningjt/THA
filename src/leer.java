import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
public class leer {
	private static double[] fare = new double[2000];
	private static  double [] id_ride_final = new double[2000];
	
	public static void main(String[] args) {
		
		String lat,lng, lat_line2,lng_line2, id_ride, timestamp, timestamp_line2;
		String result="0";
		double U;
		int i=0;
	    
		leer calculate = new leer();
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
		           lat_line2 = fields_line2[1];
		           lng_line2 = fields_line2[2];
		           timestamp_line2 = fields_line2[3];
		           U=calculate.calculate_speed(lat, lng, lat_line2, lng_line2, id_ride, timestamp, timestamp_line2,i);
				   filter(fields_line1,U);
				   i++;
	            }
	    }
	    catch (IOException e) {
	          e.printStackTrace();
	    }
	    finally {
	        result= calculate.prepare_result(i,id_ride_final,fare);
		    if (buffer_read != null) {
	           try {
	               buffer_read.close();
	           }
	           catch (IOException e) {
	                e.printStackTrace();
			   }
	        }
		    try {
                String route = "/Users/a.manning/Downloads/result.txt";
                String content = result;
			    File file = new File(route);
                // if the file doesn't exist is created
                if (!file.exists()) {
                   file.createNewFile();
                }
			    FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
		        bw.write(content);
                bw.close();
            }
		    catch (Exception e) {
                  e.printStackTrace();
			}
        }
	}
public  double calculate_speed (String lat,
						  String lng,
						  String lat_line2,
						  String lng_line2,
						  String id_ride,
						  String timestamp,
						  String timestamp_line2,
						  int i)
{
	double a,c,distance,U,delta_latitude,delta_longitude,delta_time;
	int R =6371;

	leer calculate = new leer();
	
    float new_id_ride = Float.parseFloat(id_ride);
    float new_lat = Float.parseFloat(lat);
    float new_lat_line2 = Float.parseFloat(lat_line2);
    float new_lng = Float.parseFloat(lng);
    float new_lng_line2 = Float.parseFloat(lng_line2);
    int new_timestamp = Integer.parseInt(timestamp);
    int new_timestamp_line2 = Integer.parseInt(timestamp_line2);

    delta_latitude = Math.toRadians(new_lat_line2 - new_lat) ;
    delta_longitude = Math.toRadians(new_lng_line2 - new_lng);
    delta_time = (new_timestamp_line2 - new_timestamp);
    a = Math.pow(Math.sin(delta_latitude/2),2) +
			Math.cos(new_lat) * Math.cos(new_lat_line2) *
					Math.pow(Math.sin(delta_longitude/2),2);
	c = 2 * Math.atan2(((Math.sqrt(a))),(Math.sqrt(1-a)));
    distance = (R*c);
	U = (distance/delta_time)*3600;
	calculate.fare_rules(distance, U, delta_time, new_id_ride, new_timestamp, i);
	
    return U;
}
public static String[] filter (String[] filtered_fields, double U) {

	if (U > 100) {
	      filtered_fields[0]= "0" ;
	      filtered_fields[1]= "0";
	      filtered_fields[2]= "0";
	      filtered_fields[3]= "0";
	}
	
	return filtered_fields;
}
public  void fare_rules( double distance,double U,double delta_time,float new_id_ride, int new_timestamp, int i) {

	double  fare_amount;
	// when the speed is less than 10KM
	if (U <= 10) {
           fare_amount = 11.90*((delta_time)/3600);
           if (fare_amount < 0.0) {
		         fare_amount = 0.0;
		   }
           if (fare_amount > 1000.0) {
		      fare_amount = 0.0;
		   }
		   id_ride_final [i]= new_id_ride;
		   fare [i]= fare_amount;
		   //test_rules (new_id_ride,fare_amount,delta_time,i, U);
	       Date timeD = new Date(new_timestamp * 1000);
           SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		   String Time = sdf.format(timeD);
           String time1 = new String("000000");
           String time2 = new String("050000");
           String time3 = new String("235959");

		   if ( U > 10 && Time.compareTo(time1) > 0 && Time.compareTo(time2)<0) {
 
                 fare_amount = (1.30*distance);
                 if (fare_amount > 1000.0) {
                 fare_amount = 0.0;
                 }
				 id_ride_final [i]= new_id_ride;
                 fare [i]= fare_amount;
                 // test_rules (new_id_ride,fare_amount,delta_time,i, U);
           }

           if ( U > 10 && Time.compareTo(time2) > 0 && Time.compareTo(time3)<0) {

                 fare_amount = (0.74*distance);

                 if (fare_amount > 1000.0) {
	                 fare_amount = 0.0;
                 }
                 id_ride_final [i]= new_id_ride;
                 fare [i]= fare_amount;
                 // test_rules(new_id_ride,fare_amount,delta_time,i, U);
	       }
	}
}
public  String prepare_result (int i, double id_ride_final [], double fare []) {
	
	int j,n=0;
	double fare_estimate=0, cont=1.0;
	String result="0";
	double[] x = new double [9];
	double[] y = new double[9];
	System.out.println("id_ride | fare_estimate");
	while (cont <= id_ride_final[i-1]) {
		
		for(j=0;j<i;j++) {
			
		   while(id_ride_final[j]==cont) {
		        fare_estimate= fare_estimate +fare[j];
		        j++;
		   }
		}
		fare_estimate=(fare_estimate + 1.30);
		if (fare_estimate < 3.47) {
			fare_estimate = 3.47;
		}
		DecimalFormat result_format = new DecimalFormat("#.##");
		System.out.print("    "+cont);
		System.out.println(" , "+result_format.format(fare_estimate));
		x[n]=cont;
		y[n]=fare_estimate;
		result = "id_ride, fare_estimate\n"
		         +Arrays.toString(x)+", " +Arrays.toString(y)+"\n";
		cont++;
		n++;
	}
	return result;
}

public void test_rules (double new_id_ride, double fare_amount, double delta_time, int i, double U) {
	System.out.println(new_id_ride);
    System.out.println(" , "+fare_amount); 
    System.out.println(delta_time);
    System.out.println(fare_amount);
    System.out.print(id_ride_final[i]);
    System.out.println(" , "+fare[i]);
    System.out.println(i);
    System.out.println(U);
}
}
