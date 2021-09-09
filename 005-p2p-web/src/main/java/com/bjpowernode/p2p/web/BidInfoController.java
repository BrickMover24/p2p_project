package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.util.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:BidInfoController
 * Package:com.bjpowernode.p2p.web
 * Description: 投资相关操作
 *
 * @date:2021/9/2 20:10
 * @author:LTG
 * @Copyright:动力节点
 */
/*
@Controller
public class BidInfoController {

    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",timeout = 15000)
    private  BidInfoService bidInfoService;


    @RequestMapping("/loan/invest")
    @ResponseBody
    public Result invest(HttpServletRequest request,
                         @RequestParam(value = "bidMoney",required = true) Double bidMoney,
                         @RequestParam(value = "loanId",required = true) Integer loanId){

        System.out.println(bidMoney);
        System.out.println(loanId);

        try {

            User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
            //投资的业务逻辑,新加一条投资记录(b_bid_info),修改账户可要资金,修产品信息表中的剩余可投金额
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("loanId",loanId);
            params.put("uid",user.getId());
            params.put("bidMoney",bidMoney);
            bidInfoService.invest(params);

        }catch (Exception e){
            return  Result.error("投资错误!!!");
        }

        return  Result.success();
    }


}*/

@Controller
public class BidInfoController {

    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", check = false, timeout = 15000)
    private BidInfoService bidInfoService;

    @RequestMapping("/loan/invest")
    @ResponseBody
    public Result invest(HttpServletRequest request,
                         @RequestParam(value = "bidMoney", required = true) Double bidMoney,
                         @RequestParam(value = "loanId", required = true) Integer loanId) {

        try {
            User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
            // 投资的业务逻辑 新加一条投资记录(b_bid_info) 修改账户的可用资金 修改产品信息表中的剩余可投金额（可能页需要修改产品的状态）
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("loanId",loanId);
            paramMap.put("uid",user.getId());
            paramMap.put("bidMoney",bidMoney);
            paramMap.put("phone",user.getPhone());
            bidInfoService.invest(paramMap);
        }catch (Exception e) {
            return Result.error("投资错误");
        }


        return Result.success();
    }
}

