package com.bjpowernode.p2p.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ClassName:PayKuaiQianController
 * Package:com.bjpowernode.p2p.web
 * Description: 描述信息
 *
 * @date:2021/9/4 11:41
 * @author:LTG
 * @Copyright:动力节点
 */
@Controller
public class PayKuaiQianController {

        // 接受快钱的异步通知
        @GetMapping("/kq/notify")
        public void receiveKQNotify(HttpServletResponse response) throws IOException {

            System.out.println("========receiveKQNotify=========");
            PrintWriter out = response.getWriter();
            out.println("<result>1</result><redirecturl>http://www.baidu.com</redirecturl>");
            out.flush();
            out.close();


    }
}
