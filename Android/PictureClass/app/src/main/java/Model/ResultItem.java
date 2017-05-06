package Model;

/**
 * Created by Administrator on 2017/5/3.
 */

public class ResultItem {
    private int Type;
    private String Title;
    private int Img;
    public ResultItem(int type,String title,int img){
        this.Type = type;
        this.Title = title;
        this.Img = img;
    }

    public int getImg() {
        return Img;
    }

    public int getType() {
        return Type;
    }

    public String getTitle() {
        return Title;
    }

}
