package com.rookiefly.session.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rookiefly on 2015/8/6.
 */
@Controller
public class HomeController {

    @RequestMapping("index.htm")
    public String index(HttpServletRequest request) {
        request.getSession();
        return "index";
    }
}
