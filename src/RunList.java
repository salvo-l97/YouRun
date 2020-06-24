import java.util.ArrayList;

public class RunList {

    private ArrayList<Run> list;

    public synchronized ArrayList<Run> getList() {
        return list;
    }

    public RunList(){
        list = new ArrayList<Run>();
    }

    public synchronized void add(Run r){
        list.add(r);
    }

    public synchronized void remove(Run r){
        list.remove(r);
    }

    public synchronized void clear(){
        list.clear();
    }

    @Override
    public String toString() {
        String r;
        r = "START";
        for (Run run : list){
            r = "DATE: " + run.getDate() + " LENGTH: " + run.getLength() +" TIME: " + run.getTime();;
        }
        r = "FINISH";
        return r;
    }
}
