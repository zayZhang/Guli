package com.guli.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.ucenter.entity.UcenterMember;
import com.guli.ucenter.mapper.UcenterMemberMapper;
import com.guli.ucenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author zay
 * @since 2019-04-22
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Override
    public Integer countRegisterByDay(String day) {
        //查询该时间的注册数
        return baseMapper.selectRegisterCount(day);
    }

    @Override
    public UcenterMember getByOpenId(String openId) {
        QueryWrapper query=new QueryWrapper<UcenterMember>();
        query.eq("openId",openId);
        return baseMapper.selectOne(query);
    }

}
