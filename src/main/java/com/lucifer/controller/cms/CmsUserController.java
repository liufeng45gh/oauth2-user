package com.lucifer.controller.cms;

import com.lucifer.controller.ApiAccountController;
import com.lucifer.dao.UserDao;
import com.lucifer.model.User;
import com.lucifer.service.UserService;
import com.lucifer.utils.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/30.
 */


@Controller
public class CmsUserController {

    @Resource
    private UserService userService;

    @Resource
    private UserDao userDao;

    final Logger logger = LoggerFactory.getLogger(CmsUserController.class);

    @SuppressWarnings("unchecked")
    @RequestMapping(value="/cms/user/list",method = RequestMethod.GET)
    public String list(Integer page,HttpServletRequest request){
        logger.info("page :"+page);
        Integer perPageCount = 20;
        @SuppressWarnings("rawtypes")
        Map params=new HashMap();
        params.put(PageUtil.perPageCount, perPageCount);
        params.put(PageUtil.offset, PageUtil.getStartOffset(page, perPageCount));

        List<User> userList=userDao.getUserInfoList(params);
        Integer matchRecordCount=userDao.getUserInfoListCount(params);
        Integer totalPageCount=PageUtil.getTotalPageCount(matchRecordCount, perPageCount);
        //request.setAttribute("totalPageCount", totalPageCount);
        request.setAttribute("userList", userList);
        PageUtil pageUtil = new PageUtil(request);
        String pageDiv = pageUtil.willPaginate(totalPageCount,  "pages_bar",new String []{"page","msg"});
        request.setAttribute("pageDiv",pageDiv);
        return "/cms/user/list";
    }
}
