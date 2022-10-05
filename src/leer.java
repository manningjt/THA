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
	private static  double [] id_ridef = new double[2000];
	
	public static void main(String[] args) {
		
		String lat,lng, lat1,lng1, id_ride, timestamp, timestamp1;
		String result="0";
		double U;
		int i=0;
	    
		leer calculate = new leer();
          
	     BufferedReader bufferLectura = null;
	     try {
		        // Open .csv in Buffered Reader
		        bufferLectura = new BufferedReader(new FileReader("/Users/a.manning/Downloads/paths 3.csv"));  
		        // Read a line
		        String linea = bufferLectura.readLine();
		   
		        while (linea != null) {
			       
		           String[] campos = linea.split(",");    
		           //System.out.println(Arrays.toString(campos));
		           id_ride = campos[0];
		           lat = campos[1];
		           lng = campos[2];
		           timestamp = campos[3];
		          
		           // read next line 
		           linea = bufferLectura.readLine();
		   
		           if (linea == null) {
			           break;
		           }
		   
		           String[] campos2 = linea.split(",");
		               
		           lat1 = campos2[1];
		           lng1 = campos2[2];
		           timestamp1 = campos2[3];
		           //calling calculating distance
		           U=calculate.haversine(lat, lng, lat1, lng1, id_ride, timestamp, timestamp1,i);
				   //calling filter
		           campos=filter(campos,U);	   
				   //System.out.println(Arrays.toString(campos));
				   //System.out.println(U);
				   
				   i++;
	   }   
	 }
	 catch (IOException e) {
	  e.printStackTrace();
	 }
	 finally {
		 //calling valor to print answer
	  result= calculate.valor(i,id_ridef,fare);    
		 
	  if (bufferLectura != null) {
	   try {
	    bufferLectura.close();
	   } 
	   
	   catch (IOException e) {
	    e.printStackTrace();
	   }
	  }
	 
	  try {
          String ruta = "/Users/a.manning/Downloads/result.txt";
          String contenido = result;
            
          File file = new File(ruta);
          // if the file doesn't exists is created
          if (!file.exists()) {
              file.createNewFile();
          }
          FileWriter fw = new FileWriter(file);
          BufferedWriter bw = new BufferedWriter(fw);
          
		bw.write(contenido);
          bw.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
	 } 
     	 	




public  double haversine (String lat,String lng, String lat1,String lng1, String id_ride, String timestamp, String timestamp1,int i){
 
	double a,c,distance,U,deltlat,deltlng,delttime;
	int R =6371;
	leer calculate = new leer();
	
    float id_ridea = Float.parseFloat(id_ride);
    float lata = Float.parseFloat(lat);
    float latb = Float.parseFloat(lat1);
    float lnga = Float.parseFloat(lng);
    float lngb = Float.parseFloat(lng1);
    Integer timestampa = Integer.parseInt(timestamp);
    Integer timestampb = Integer.parseInt(timestamp1);
	
//calculating distance
    deltlat=Math.toRadians(latb - lata) ;
    deltlng= Math.toRadians(lngb - lnga);
    delttime= (timestampb - timestampa);
    a = Math.pow(Math.sin(deltlat/2),2)+ Math.cos(lata)*Math.cos(latb)* Math.pow(Math.sin(deltlng/2),2);
    c = 2* Math.atan2(((Math.sqrt(a))),(Math.sqrt(1-a)));
    distance = (R*c);
    U = (distance/delttime)*3600;
    
	calculate.fare_rules(distance, U, deltlat, deltlng, delttime, id_ridea, timestampa, i);
	
    return U;
}

public static String[] filter (String[] camposa, double U) {
	
	//filter
	
	if (U > 100) {
		  
	      camposa[0]= "0" ;
	      camposa[1]= "0";
	      camposa[2]= "0";
	      camposa[3]= "0";
       //System.out.println("AAAAA");      
     }
	
	return camposa; 
	
}

public  void fare_rules( double distance,double U,double deltlat,double deltlng,double delttime,float id_ridea, int timestampa, int i) {
	// Fare rules
	double  fare_amount;
    
	if (U <= 10) {
           fare_amount = 11.90*((delttime)/3600);
           //System.out.println(fare_amount);
           if (fare_amount < 0.0) {
		         fare_amount = 0.0;
	         }
           if (fare_amount > 1000.0) {
		      fare_amount = 0.0;
	        }
            id_ridef [i]= id_ridea;
            fare [i]= fare_amount;
            //test_rules (id_ridea,fare_amount,delttime,i, U); 
            // System.out.println("XXXXXXXXX");             
	        
	 Date timeD = new Date(timestampa * 1000);
     SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");

     String Time = sdf.format(timeD);
     String time1 = new String("000000");
     String time2 = new String("050000");
     String time3 = new String("235959");

      //System.out.println(Time);

      if ( U > 10 && Time.compareTo(time1) > 0 && Time.compareTo(time2)<0) {
 
          fare_amount = (1.30*distance); 
          if (fare_amount > 1000.0) {
                 fare_amount = 0.0;
           }
          
          id_ridef [i]= id_ridea;
          fare [i]= fare_amount;
         // test_rules (id_ridea,fare_amount,delttime,i, U);
          //System.out.println("WWWWWWWWWWW");
      }   

       if ( U > 10 && Time.compareTo(time2) > 0 && Time.compareTo(time3)<0) {

             fare_amount = (0.74*distance);

             if (fare_amount > 1000.0) {
	               fare_amount = 0.0;
             }
            id_ridef [i]= id_ridea;
            fare [i]= fare_amount;
           // test_rules(id_ridea,fare_amount,delttime,i, U);           
	       //System.out.println("ZZZZZZZZZZZZZZZZZZZZ");   	
                             
         }                  
      }          
}     
	

public  String valor (int i, double id_ridef [], double fare []) {
	
	int j,n=0;
	double fare_estimate=0, cont=1.0;
	String result="0";
	double[] x = new double [9];
	double[] y = new double[9];
	
	
	
	while (cont <= id_ridef[i-1]) {
		
		for(j=0;j<i;j++) {
			
		   while(id_ridef[j]==cont) {
		        fare_estimate= fare_estimate +fare[j];
		        j++;
		        //System.out.print(id_ridef[j]);
		        //System.out.println(" , "+fare[j]);
		       //System.out.println(fare_estimate);
		        //System.out.println(id_ridef[j]);
		   }
	     }
		 fare_estimate=(fare_estimate + 1.30);
		 if (fare_estimate < 3.47) {
			 
			 fare_estimate = 3.47;
		 }
		
		 DecimalFormat formato = new DecimalFormat("#.##");
		 
		 System.out.print(cont);
		 System.out.println(", "+formato.format(fare_estimate));
		
		  
		 x[n]=cont;
		 y[n]=fare_estimate; 
		
		 result = "id_ride, fare_estimate\n"
		         +Arrays.toString(x)+", " +Arrays.toString(y)+"\n";
		 cont++;
		 n++;
		 
		 
	}	
						
	return result;
}

public void test_rules (double id_ridea, double fare_amount, double delttime, int i, double U) {
	System.out.println(id_ridea);
    System.out.println(" , "+fare_amount); 
    System.out.println(delttime);
    System.out.println(fare_amount);
    System.out.print(id_ridef[i]);
    System.out.println(" , "+fare[i]);
    System.out.println(i);
    System.out.println(U);	      
    
  
}
}
