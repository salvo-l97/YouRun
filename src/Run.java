import java.io.Serializable;

public class Run implements Serializable, Comparable<Run> {
    private int length;
    private int time;
    private String date;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
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
        if (date == r.getDate() && length == r.getLength() && time == r.getTime()) return 0;
        else return -1;
    }
}
