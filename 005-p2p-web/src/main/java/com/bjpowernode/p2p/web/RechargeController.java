package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.model.loan.RechargeRecord;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.RechargeRecordService;
import com.bjpowernode.p2p.service.loan.kuaiQianService;
import com.bjpowernode.p2p.service.user.RedisService;
import com.bjpowernode.p2p.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bjpowernode.p2p.cons.Constants;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * ClassName:RechargeController
 * Package:com.bjpowernode.p2p.web
 * Description: 描述信息
 *
 * @date:2021/9/3 9:21
 * @author:LTG
 * @Copyright:动力节点
 */
@Controller
public class RechargeController {

    @Reference(interfaceClass =kuaiQianService.class, version = "1.0.0", check = false, timeout = 15000)
    private kuaiQianService kuaiQianService;

    @Reference(interfaceClass = RechargeRecordService.class, version = "1.0.0", check = false, timeout = 15000)
    private RechargeRecordService rechargeRecordService;

    @Reference(interfaceClass = RedisService.class,version = "1.0.0",check = false,timeout = 15000)
    private RedisService redisService;

    @RequestMapping("loan/page/toRecharge")

    public String toRecharge(){
        return "toRecharge";
    }

    // 进行支付操作
    /*@RequestMapping("/loan/toRecharge")
    public String submitRecharge(HttpServletRequest request,
                                 Model model,
                                 @RequestParam("rechargeMoney") String yuan) throws Exception {

        // TODO 生成订单
        // 全局唯一订单号 = 时间戳 + redis唯一数组
       String rechargeNo = DateUtil.getTimestramp() + redisService.getOnlyNumber();

        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(user.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeStatus("0");//0表示充值中 ,1表示充值成功,2表示充值失败
        rechargeRecord.setRechargeMoney(Double.parseDouble(yuan));
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("快钱充值");
        int rows=rechargeRecordService. addRecharge(rechargeRecord);
        if (rows==0){
            throw new   Exception("生成订单失败");
        }

        //User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);


        Map<String, String> map = kuaiQianService.makeKuaiQianRequestParam(rechargeNo, yuan, user.getName(), user.getPhone(), request.getRemoteAddr());
        model.addAllAttributes(map);
        //TODO 创建充值记录
        return "kuaiQianForm";
    }*/
    @RequestMapping("loan/toRecharge")
    public String submitRecharge(HttpServletRequest request,
                                 Model model,
                                 @RequestParam("rechargeMoney") String yuan) throws Exception {
        // TODO 生成订单
        String rechargeNo = DateUtil.getTimestramp() + redisService.getOnlyNumber();
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(user.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeStatus("0");  // 0: 充值中 1：充值成功 2:充值失败
        rechargeRecord.setRechargeMoney(Double.parseDouble(yuan));
        rechargeRecord.setRechargeTime(new Date());
        rechargeRecord.setRechargeDesc("快钱充值");
        int rows = rechargeRecordService.addRecharge(rechargeRecord);
        if (rows == 0) {
            throw new Exception("充值，生成订单失败");
        }
        Map<String, String> map = kuaiQianService.makeKuaiQianRequestParam(rechargeNo,yuan, user.getName(), user.getPhone(), request.getRemoteAddr());
        model.addAllAttributes(map);

        return "kuaiQianForm";

    }



}
