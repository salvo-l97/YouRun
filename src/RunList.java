import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class RunList implements Serializable {

    private ArrayList<Run> list;

    public synchronized ArrayList<Run> getcopy() {
        ArrayList<Run> r_list = new ArrayList<>();
        r_list.addAll(list);
        return r_list;
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

    public synchronized ArrayList<Run> show(){
        ArrayList<Run> rl = new ArrayList<>();
        for (Run run : list) {
            Run r = new Run();
            r.setDate(run.getDate());
            r.setLength(run.getLength());
            r.setTime(run.getTime());
            rl.add(r);
        }
        return rl;
    }

    @Override
    public String toString() {
        return "RunList{" +
                "list=" + list +
                '}';
    }
}
