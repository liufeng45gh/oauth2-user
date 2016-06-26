package com.lucifer.controller;


import com.lucifer.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/26.
 */
@Controller
@RequestMapping(value = "/oauth2.0")
public class Oauth2Controller {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/authorize",method= RequestMethod.GET)
    public String authorize(Model model, @RequestParam(value = "response_type") String responseType,@RequestParam(value = "client_id") String clientId, @RequestParam(value = "redirect_uri")String redirectUri) throws IOException {
        //response.sendRedirect("/auth2.0/login");
        model.addAttribute("response_type",responseType);
        model.addAttribute("client_id",clientId);
        model.addAttribute("redirect_uri",redirectUri);
        logger.info("authorize has been called");
        return "login";
    }

    @RequestMapping(value = "/authorize",method= RequestMethod.POST)
    public Result login(Model model, @RequestParam(value = "response_type") String responseType,@RequestParam(value = "client_id") String clientId, @RequestParam(value = "redirect_uri")String redirectUri,String userName,String password){
        return Result.ok();
    }

    @RequestMapping(value = "token",method= RequestMethod.GET)
    public Map token(){
        Map map = new HashMap<>();
        return map;
    }
}
