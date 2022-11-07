import java.util.ArrayList;
import java.util.List;


public class Rides {


    private ArrayList<Double> id_ride_final;
    private ArrayList<Double> rideFare;

    private Segment segments;


    public Rides(Segment segments,List<Double> id_ride_final,List<Double> rideFare) {

        this.segments = segments;
        this.id_ride_final= (ArrayList<Double>) id_ride_final;
        this.rideFare= (ArrayList<Double>) rideFare;

    }
    public ArrayList<Double> getId_ride_final(){
        return id_ride_final;
    }

    public ArrayList<Double> getRideFare() {
        return rideFare;
    }

    public void calculateFare(int i, double id_ride) {

        id_ride_final.add(i, id_ride);
        rideFare.add(i, segments.getFare());


    }

}