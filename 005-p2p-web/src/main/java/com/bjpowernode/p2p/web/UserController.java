package com.bjpowernode.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.p2p.cons.Constants;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.service.user.RedisService;
import com.bjpowernode.p2p.service.user.UserService;
import com.bjpowernode.p2p.util.Result;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ClassName:UserController
 * Package:com.bjpowernode.p2p.web
 * Description: 用户相关的操作如 注册 登录
 *
 * @date:2021/8/28 16:48
 * @author:sjf
 * @Copyright:动力节点
 */
@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false, timeout = 15000)
    private UserService userService;

    @Reference(interfaceClass = RedisService.class, version = "1.0.0" , check = false, timeout = 15000)
    private RedisService redisService;

    @Reference(interfaceClass = FinanceAccountService.class, version = "1.0.0", check = false, timeout = 15000)
    private FinanceAccountService financeAccountService;

    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("/loan/page/register")
    public String toRegister() {
        return "register";
    }


    /**
     * 根据前端的手机号 查询该手机号是否存在(用于注册)
     * 可以使用：{"code":1,"message":"","success":true}
     * 不能使用：{"code":-1,"message":"137xxxxxx","success":false}
     * @param phone
     * @return
     */
    @RequestMapping("/loan/checkPhone")
    @ResponseBody
    public Object checkPhone(String phone) {
        // 用手机号作为参数 查询数据库
        User user = userService.checkPhone(phone);
        if (ObjectUtils.allNotNull(user)) {
            return  Result.error(phone);
        }
        return Result.success();

    }

    @RequestMapping("/loan/register")
    @ResponseBody
    public Result register(HttpServletRequest request,
                           String phone,
                           String loginPassword,
                           String messageCode) {

        try {
            // 1. 首先判断用户输入的验证码是否正确
            String redisCode = redisService.get(phone);
            if (!StringUtils.equals(messageCode,redisCode)) {
                return Result.error("验证码错误");
            }
            User user = userService.register(phone,loginPassword);
            request.getSession().setAttribute(Constants.SESSION_USER,user);
        }catch (Exception e) {
            return Result.error("注册失败");
        }

        return Result.success();
    }

    /**
     * 请求第三方接口，实现发送短信的效果
     * @return
     */
    @RequestMapping("/user/messageCode")
    @ResponseBody
    public Result sendMessageCode(String phone) throws Exception {
       // 请求第三方接口
        String messageCode = getRandNumber(6);
        Map<String, String> params = new HashMap<String, String>();
        params.put("appkey","0e97b3c5e497b7637867e71c44a42b1e");
        params.put("mobile",phone);
        params.put("content","【凯信通】您的验证码是：" + messageCode);
        //String resultJson = HttpClientUtils.doGet("https://way.jd.com/kaixintong/kaixintong", params);
        String resultJson = "{\n" +
                "\t\"code\": \"10000\",\n" +
                "\t\"charge\": false,\n" +
                "\t\"remain\": 0,\n" +
                "\t\"msg\": \"查询成功\",\n" +
                "\t\"result\": \"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-7046347</remainpoint>\\n <taskID>166052768</taskID>\\n <successCounts>1</successCounts></returnsms>\"\n" +
                "}\n";

        JSONObject jsonObject = JSONObject.parseObject(resultJson);
        String code = jsonObject.getString("code");
        if (!StringUtils.equals(code,"10000")) {
            return Result.error("发送短信失败");
        }
        String resultXml = jsonObject.getString("result");
        Document document = DocumentHelper.parseText(resultXml);
        Node node = document.selectSingleNode("/returnsms/returnstatus[1]");
        String returnstatus = node.getText();
        if (!StringUtils.equals(returnstatus,"Success")) {
            return Result.error("发送短信失败");
        }

        // 发送短信成功
        // 将随机生成的验证码保存到redis中
        redisService.put(phone,messageCode);

        return Result.success(messageCode);
    }

    @RequestMapping("/user/realname")
    public String toRealName() {
        return "realName";
    }

    /**
     * 实名认证
     * @return
     */
    @RequestMapping("loan/realName")
    @ResponseBody
    public Result realName(HttpServletRequest request,
                           @RequestParam(value = "realName", required = true) String realName,
                           @RequestParam(value = "idCard", required = true) String idCard,
                           @RequestParam(value = "messageCode", required = true) String messageCode,
                           @RequestParam(value = "phone",required = true) String phone) {


        Map<String, String> params = new HashMap<String, String>();
        params.put("appkey","0e97b3c5e497b7637867e71c44a42b1e");
        params.put("cardNo",idCard);
        params.put("realName",realName);
       try {
           // 验证验证码是否正确
           String redisCode = redisService.get(phone);
           if (!StringUtils.equals(redisCode,messageCode)) {
               return Result.error("验证码有误");
           }

           //String resultJson = HttpClientUtils.doGet("https://way.jd.com/youhuoBeijing/test", params);
           String resultJson = "{\n" +
                   "    \"code\": \"10000\",\n" +
                   "    \"charge\": false,\n" +
                   "    \"remain\": 1305,\n" +
                   "    \"msg\": \"查询成功\",\n" +
                   "    \"result\": {\n" +
                   "        \"error_code\": 0,\n" +
                   "        \"reason\": \"成功\",\n" +
                   "        \"result\": {\n" +
                   "            \"realname\": \"乐天磊\",\n" +
                   "            \"idcard\": \"350721197702134399\",\n" +
                   "            \"isok\": true\n" +
                   "        }\n" +
                   "    }\n" +
                   "}";
           JSONObject jsonObject = JSONObject.parseObject(resultJson);
           String code = jsonObject.getString("code");
           if (!StringUtils.equals(code,"10000")) {
               return Result.error("第三方接口实名认证失败");
           }
           String outResult = jsonObject.getString("result");
           JSONObject parseObject = JSONObject.parseObject(outResult);
           String innerResult = parseObject.getString("result");
           JSONObject object = JSONObject.parseObject(innerResult);
           Boolean isok = object.getBoolean("isok");

           if (!isok) {
               return Result.error("第三方接口实名认证失败");
           }

           // 认证成功,更新user表
           User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
           // 更新user表
           user.setName(realName);
           user.setIdCard(idCard);
           int rows = userService.modifyUser(user);
           if (rows == 0) {
               throw new Exception("实名认证后，更新用户失败");
           }

           // 完成数据库的User更新后，Session中的用户也需要更新
           request.getSession().setAttribute(Constants.SESSION_USER,user);

       }catch (Exception e) {
           return Result.error("第三方接口实名认证失败");
       }
        return Result.success();
    }

    @RequestMapping("/loan/myFinanceAccount")
    @ResponseBody
    public FinanceAccount myFinanceAccount(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(user.getId());

        return financeAccount;
    }

    @RequestMapping("/loan/logout")
    public String logOut(HttpServletRequest request) {
        // 删除Session中的user对象（退出用户）
        request.getSession().removeAttribute(Constants.SESSION_USER);
        // 重定向到首页
        return "redirect:/index";
    }

    /**
     * 跳转到登录页
     * @return
     */
    @RequestMapping("loan/page/login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/loan/login")
    @ResponseBody
    public Result login(HttpServletRequest request,
                        @RequestParam(value = "phone" ,required = true) String phone,
                        @RequestParam(value = "loginPassword", required = true) String loginPassword,
                        @RequestParam(value = "messageCode",required = true) String messageCode) {
        try {
            String redisCode = redisService.get(phone);
            if (!StringUtils.equals(redisCode,messageCode)) {
                return Result.error("验证码错误");
            }
            User user = userService.queryUserByPhoneAndPwd(phone,loginPassword);
            if (!ObjectUtils.allNotNull(user)) {
                return Result.error("用户名或者密码错误");
            }

            //修改最后登录时间
            user.setLastLoginTime(new Date());
            userService.modifyUser(user);

            request.getSession().setAttribute(Constants.SESSION_USER,user);

        }catch (Exception e) {
            return Result.error("登录异常");
        }
        return Result.success();

    }

    @RequestMapping("/loan/myCenter")
    public String myCenter(HttpServletRequest request, Model model){

        User user = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        model.addAttribute("user",user);
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(user.getId());
        model.addAttribute("availableMoney",financeAccount.getAvailableMoney());
        return "myCenter";
    }



    private String getRandNumber(int n) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            int number = random.nextInt(10);
            sb.append(number);
        }

        return sb.toString();
    }


}
