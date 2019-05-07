package com.guli.ucenter.service;

import com.guli.ucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author zay
 * @since 2019-04-22
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    Integer countRegisterByDay(String day);

    //判断用户是否通过微信登录过
    public UcenterMember getByOpenId(String openId);

}
