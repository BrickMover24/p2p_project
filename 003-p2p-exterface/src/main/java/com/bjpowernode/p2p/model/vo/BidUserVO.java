package com.bjpowernode.p2p.model.vo;

import java.io.Serializable;

/**
 * ClassName:BidUserVO
 * Package:com.bjpowernode.p2p.model.vo
 * Description: 描述信息
 *
 * @date:2021/9/3 9:10
 * @author:LTG
 * @Copyright:动力节点
 */


public class BidUserVO implements Serializable {

    private String  phone;
    private Double bidMoney;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }
}
