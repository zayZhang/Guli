package com.guli.ucenter.controller.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.ucenter.entity.UcenterMember;
import com.guli.ucenter.service.UcenterMemberService;
import com.guli.ucenter.util.ConstantPropertiesUtil;
import com.guli.ucenter.util.CookieUtils;
import com.guli.ucenter.util.HttpClientUtils;
import com.guli.ucenter.util.JwtUtils;
import com.guli.ucenter.vo.LoginInfoVo;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;


    @GetMapping("login")
    public String genOrConnect(HttpSession session){
        System.out.print("sessionId= "+session.getId());
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        // 回调地址
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL; //获取业务服务器重定向地址

        try{
            redirectUrl= URLEncoder.encode(redirectUrl,"utf-8");

        }catch(Exception e){
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }
//        String state= UUID.randomUUID().toString().replaceAll("-","");

        //这里为什么要配置state为我们的外网
        /**
         * 因为微信登录需要有对应的经营许可注册，这里是使用的老师的，老师给了个回调，里面的重定向地址为了给我们每个人配置不一样的
         * 地址，就使用了state作为重定向URL
         */
        String state ="zhangqingsong";

        System.out.println(state);
        session.setAttribute("wx-open-state", state);
        String qrcodeUrl=String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state
        );

        return "redirect:"+qrcodeUrl;
    }

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session,HttpServletRequest request,
                           HttpServletResponse response) {

        System.out.println("sessionId = " + session.getId());

        //得到授权临时票据code
        System.out.println("code = " + code);
        System.out.println("获取 state = " + state);

        // 判断state是否合法
        String stateStr = (String)session.getAttribute("wx-open-state");
        System.out.println("存储 state = " + stateStr);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(stateStr) || !state.equals(stateStr)) {
            throw new GuliException(ResultCodeEnum.error_get_state);
        }

        // 通过code获取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println(result);
        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        Map<String, Object> resultMap = gson.fromJson(result, HashMap.class);
        if (resultMap.get("errcode") != null) {
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        String accessToken = (String) resultMap.get("access_token");
        String openid = (String) resultMap.get("openid");

        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(openid)) {
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        // 根据Openid返回用户信息。
        UcenterMember member = memberService.getByOpenId(openid);
        if (member == null) { //新用户

            System.out.println("新用户注册");

            //从微信获取用户信息
            //获取用户基本信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);

            //获取用户信息
            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println(resultUserInfo);
            } catch (Exception e) {
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            Map<String, Object> resultUserInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            if (resultMap.get("errcode") != null) {
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            //保存用户信息
            String nickname = (String)resultUserInfoMap.get("nickname");
            String headimgurl = (String)resultUserInfoMap.get("headimgurl");

            member = new UcenterMember();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);
            memberService.save(member);
        }

        // TODO 登录
        System.out.println("登录");
        // 登录
        // 生成jwt，将member中的id，用户名和头像地址传进JWT中生成有效载荷
        String guliJwtToken = JwtUtils.generateJWT(member);
        // 存入cookie，超时时间30分钟,由于涉及到了cookie的跨域访问，所以这一步根本不好使
       // CookieUtils.setCookie(request, response, "guli_jwt_token", guliJwtToken, 60 * 30);
        System.out.println(guliJwtToken);
        return "redirect:http://localhost:3000?token=" + guliJwtToken;
    }

    /**
     * 将jwt令牌存cookie中获取出来
     * @param request
     * @return
     */
    @GetMapping("get-jwt")
    @ResponseBody
    public R getJwt(HttpServletRequest request){
        String guliJwtToken=CookieUtils.getCookieValue(request, "guli_jwt_token");
        return R.ok().data("guli_jwt_token", guliJwtToken);
    }

    /**
     * 解析jwt令牌，并将其封装金vo对象中，返回给前端用户
     * @param jwtToken
     * @return
     */
    @PostMapping("parse-jwt")
    @ResponseBody
    public R getLoginInfoByJwtToken(@RequestBody String jwtToken){
        Claims claims=JwtUtils.checkJWT(jwtToken);
        String id=(String)claims.get("id");
        String nickname=(String)claims.get("nickname");
        String avatar=(String)claims.get("avatar");
        LoginInfoVo loginInfoVo=new LoginInfoVo();
        loginInfoVo.setAvatar(avatar);
        loginInfoVo.setId(id);
        loginInfoVo.setNickname(nickname);

        return R.ok().data("loginInfo", loginInfoVo);
    }

}
