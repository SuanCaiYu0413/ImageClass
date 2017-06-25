package Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/24.
 */

public class LabelModel implements Serializable {
    private String LabelId;
    private String name;
    private String userId;
    private String picId;

    public String getLabelId() {
        return LabelId;
    }

    public void setLabelId(String labelId) {
        LabelId = labelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPicId() {
        return picId;
    }

    public void setPicId(String picId) {
        this.picId = picId;
    }
}
