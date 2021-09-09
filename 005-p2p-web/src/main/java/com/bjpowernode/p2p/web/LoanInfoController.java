package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.BidUserVO;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.model.vo.PaginationVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:LoanInfoController
 * Package:com.bjpowernode.p2p.web
 * Description: 产品相关的操作
 *
 * @date:2021/8/28 9:56
 * @author:sjf
 * @Copyright:动力节点
 */

@Controller
public class LoanInfoController {

    @Reference(interfaceClass = LoanInfoService.class, version = "1.0.0", check = false, timeout = 15000)
    private LoanInfoService loanInfoService;

    @Reference(interfaceClass = BidInfoService.class, version = "1.0.0",check = false, timeout = 15000)
    private BidInfoService bidInfoService;

    @Reference(interfaceClass = FinanceAccountService.class, version = "1.0.0", check = false, timeout = 15000)
    private FinanceAccountService financeAccountService;


    /**
     * 产品列表
     * @param model
     * @param ptype
     * @param currentPage
     * @return
     */
    @RequestMapping("loan/loan")
    public String loan(Model model,
                       @RequestParam(value = "ptype",required = false) Integer ptype,
                       @RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage) {
        // 分页显示产品列表，参数（产品类型(可以为空),当前页currentPage(默认值是1)，每页显示的条数）
        // 返回的参数：List<LoanInfo>   分页信息
        Integer pageSize = 9;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currentPage",(currentPage - 1) * pageSize);
        paramMap.put("pageSize",pageSize);
        if (ObjectUtils.allNotNull(ptype)) {
            paramMap.put("productType",ptype);
        }
        PaginationVO<LoanInfo> paginationVO = loanInfoService.queryLoanInfoByPage(paramMap);
        // 总条数
        Integer totalSize = paginationVO.getTotalSize();
        // 计算出总页数
        int totalPage  = totalSize / pageSize;
        int mod = totalSize % pageSize;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }

        // 产品列表
        model.addAttribute("loanInfoList",paginationVO.getDatas());
        // 总条数
        model.addAttribute("totalSize",totalSize);
        // 当前页
        model.addAttribute("currentPage",currentPage);

        //总页数
        model.addAttribute("totalPage",totalPage);

        if (ObjectUtils.allNotNull(ptype)) {
            model.addAttribute("ptype",ptype);
        }


        // TODO 投资排行榜
       List<BidUserVO> bidUserVOList= loanInfoService.queryInvestTop();
        model.addAttribute("bidUserVOList",bidUserVOList);


        return "loan";
    }

    /**
     * 产品详情页数据查询
     * @param model
     * @param id 产品ID
     * @return
     */
    @RequestMapping("/loan/loanInfo")
    public String loanInfo(HttpServletRequest request,
                           Model model,
                           @RequestParam(value = "id",required = true) Integer id) {

        // 产品详情
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);
        model.addAttribute("loanInfo", loanInfo);

        // 该产品的投资记录，参数(产品ID，currentPage:0,pageSize:10)
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("loanId",id);
        paramMap.put("currentPage",0);
        paramMap.put("pageSize",10);
        List<BidInfo> bidInfoList = bidInfoService.queryRecentlyBidInfoByLoanId(paramMap);
        model.addAttribute("bidInfoList",bidInfoList);

        // 账户的余额
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        if (ObjectUtils.anyNotNull(user)) {
            // User不为空，是已登录状态
            FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(user.getId());
            model.addAttribute("availableMoney",financeAccount.getAvailableMoney());
        }

        /*// TODO 立即投资*/

        return "loanInfo";
    }

}
