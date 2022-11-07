public class Point{

    private double longitude;
    private double latitude;
    private long timestamp;

    public Point(double latitude, double longitude, long timestamp){

        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp =timestamp;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public long getTimestamp(){
        return this.timestamp;
    }
}





