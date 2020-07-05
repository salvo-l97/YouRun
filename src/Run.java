import java.io.Serializable;

public class Run implements Serializable, Comparable<Run> {
    private String length;
    private String time;
    private String date;

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "date = "+date+ ", length = "+length+", time = " + time;
    }

    @Override
    public int compareTo(Run r) {
        int x1 = date.compareTo(r.getDate());
        int x3 = length.compareTo(r.getLength());
        int x2 = time.compareTo(r.getTime());
       if(x1 == 0 && x2 == 0 && x3 == 0){
           return 0;
       } else {
           return -1;
       }
    }
}
