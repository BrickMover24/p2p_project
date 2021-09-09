package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class IndexController {

    @Reference(interfaceClass = LoanInfoService.class, version = "1.0.0", check = false, timeout = 15000)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false, timeout = 15000)
    private UserService userService;

    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0", check = false, timeout = 15000)
    private BidInfoService bidInfoService;


    @RequestMapping("/index")
    public String toIndex(Model model) {
        // 查询历史平均年化收益率
        // 模拟多线程高并发访问Redis
       /* ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 1000; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Double histryAvgRate = loanInfoService.queryHistryAvgRate();
                    model.addAttribute(Constants.HISTRY_AVG_RATE,histryAvgRate);
                }
            });
        }


        // 关闭线程池
        executorService.shutdown();*/


        //查询历史年化收益率
        Double histryAvgRate = loanInfoService.queryHistryAvgRate();
        model.addAttribute(Constants.HISTRY_AVG_RATE,histryAvgRate);

        // 查询平台用户数量
        Integer allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT,allUserCount);

        // 查询平台累计成交金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY, allBidMoney);


        // 分析：尽管在页面上展示的效果不同，但是实际上都是查询产品列表
        // 返回值：List<LoanInfo>   请求的参数 productType  limit 0,1
        Map<String, Object> paramMap = new HashMap<String, Object>();
        // 查询新手宝
        paramMap.put("productType",Constants.PRODUCT_TYPE_X);
        paramMap.put("currentPage",0);
        paramMap.put("pageSize",1);
        List<LoanInfo> loanInfoX = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("loanInfoX",loanInfoX);

        paramMap.put("productType",Constants.PRODUCT_TYPE_Y);
        paramMap.put("pageSize",4);
        // 查询优选类产品
        List<LoanInfo> loanInfoY = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("loanInfoY", loanInfoY);

        // 查询散标类产品
        paramMap.put("productType",Constants.PRODUCT_TYPE_S);
        paramMap.put("pageSize",8);
        List<LoanInfo> loanInfoS = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("loanInfoS",loanInfoS);

        return "index";
    }
}
