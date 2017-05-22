package Model;

/**
 * Created by Administrator on 2017/5/10.
 */

public class HobbyLabel {
    private int id;
    private String name;

    public boolean isChoose() {
        return Choose;
    }

    public void setChoose(boolean choose) {
        Choose = choose;
    }

    private boolean Choose;
    public HobbyLabel(String name,int id) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
