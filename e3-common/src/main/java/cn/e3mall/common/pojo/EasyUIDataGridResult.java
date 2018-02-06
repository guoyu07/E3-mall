package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cxq on 2017/10/8.
 */
public class EasyUIDataGridResult implements Serializable{

    private long total;
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
