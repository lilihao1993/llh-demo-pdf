/*
 *  Copyright© 2003-2016 浙江汇信科技有限公司, All Rights Reserved.
 */
package com.demo.system.controller;

import com.icinfo.framework.captcha.web.DefaultCaptchaServlet;
import com.icinfo.framework.common.exception.GenericException;
import com.icinfo.framework.common.ajax.AjaxResult;
import com.icinfo.framework.core.web.BaseController;
import com.icinfo.framework.security.shiro.LoginToken;
import com.demo.support.Constants;
import com.demo.system.dto.LoginDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 描述:  用户登录控制.<br>
 *
 * @author xiajunwei
 * @date 2016年05月18日
 */
@Controller
@RequestMapping("/admin")
public class LoginController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 进入登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() throws Exception {
        return "redirect:/";
    }

    /**
     * 用户登录验证
     *
     * @param session
     * @param loginDto
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult loginCheck(HttpSession session, HttpServletResponse response, @Valid LoginDto loginDto, BindingResult result) throws Exception {
        logger.debug("系统用户登录");
        if (result.hasErrors()) {
            //参数验证错误
            AjaxResult error = AjaxResult.error("参数验证错误");
            error.addErrorInfo(result.getAllErrors());
            return error;
        }
        //验证码校验
        String checkCode = session.getAttribute(DefaultCaptchaServlet.getSessionKey()) + "";
        if (!loginDto.getCheckCode().equalsIgnoreCase(checkCode)) {
            return AjaxResult.error("验证码输入错误");
        }
        session.removeAttribute(DefaultCaptchaServlet.getSessionKey());

        try {
            LoginToken loginToken = new LoginToken(loginDto.getUsername(), loginDto.getPassword());
            Subject subject = SecurityUtils.getSubject();
            subject.login(loginToken);
            session.setAttribute(Constants.SESSION_SYS_USER_KEY, subject.getPrincipal());
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            throw new GenericException("登录失败,用户名或密码错误!", e);
        }
        return AjaxResult.success("登录成功");
    }

    /**
     * 进入登录页面
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpSession session) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return "redirect:/";
    }
}
