package com.bjpowernode.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName:PaginationVO
 * Package:com.bjpowernode.p2p.model.vo
 * Description: 描述信息
 *
 * @date:2021/8/28 11:09
 * @author:sjf
 * @Copyright:动力节点
 */
public class PaginationVO<T> implements Serializable {

    private List<T> datas;

    private Integer totalSize;

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }
}
