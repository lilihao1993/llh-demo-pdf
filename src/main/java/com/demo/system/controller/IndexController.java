/*
 *  Copyright© 2003-2016 浙江汇信科技有限公司, All Rights Reserved.
 */
package com.demo.system.controller;

import com.demo.support.Constants;
import com.icinfo.framework.core.web.BaseController;
import com.icinfo.framework.security.shiro.UserProfile;
import com.icinfo.framework.tools.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * 描述: 系统管理首页控制器 .<br>
 *
 * @author xiajunwei
 * @date 2016年3月15日
 */
@Controller
@RequestMapping("/admin")
public class IndexController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 管理端首页
     *
     * @param session
     * @return
     */
    @RequestMapping
    public ModelAndView index(HttpSession session) {
        ModelAndView view = new ModelAndView("index");
        UserProfile userProfile = (UserProfile) session.getAttribute(Constants.SESSION_SYS_USER_KEY);
        if (userProfile != null) {
            //用户显示菜单
            view.addObject("menus", userProfile.getMenus());
            view.addObject("userProfile", userProfile);
        }
        return view;
    }

    /**
     * 管理中心首页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/dashboard")
    public String dashboard(HttpServletRequest request, HttpServletResponse response) {
        return "dashboard/dashboard";
    }


    /**
     * 企业信用报告下载
     *
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("downloadCreditReport")
    public ByteArrayOutputStream downloadCreditReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            ByteArrayOutputStream baos = pdfService.generatePDF(request.getParameter("pripid")).getBaos();
            byte[] bytes = baos.toByteArray();
            response.setContentType(response.getContentType());
            response.setHeader("Content-disposition",
                    /* "attachment; filename=" + URLEncoder.encode("企业信用报告-" + DateUtils.formatDate(new Date(), "yyyyMMddhhmmss") + ".pdf", "UTF-8"));*/

                    "attachment; filename=" + new String(("企业信用报告-" + DateUtils.formatDate(new Date(), "yyyyMMddhhmmss") + ".pdf").getBytes("gb2312"), "iso8859-1"));
            response.setHeader("Content-Length", String.valueOf(bytes.length));
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(bytes);
            bos.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("下载失败：" + e.getMessage());
        }
        return null;
    }

}
