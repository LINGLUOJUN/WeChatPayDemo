package com.ys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ShuangYang on 2016/8/11.
 */
@Controller
public class UserController {

    /**
     * 打开首页
     * @return
     */
    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }
}
