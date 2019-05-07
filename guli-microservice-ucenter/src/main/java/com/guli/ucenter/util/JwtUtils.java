package com.guli.ucenter.util;

import com.guli.ucenter.entity.UcenterMember;
import com.guli.ucenter.entity.UcenterMember;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;
import java.util.Date;

public class JwtUtils {

    public static final String SUBJECT = "guli-user";

    //秘钥
    public static final String APPSECRET = "guli666";

    public static final long EXPIRE = 1000 * 60 * 30;  //过期时间，毫秒，30分钟


    /**
     * 生成jwt token
     *
     * @param member
     * @return
     */
    public static String generateJWT(UcenterMember member) {

        if (member == null || StringUtils.isEmpty(member.getId())
                || StringUtils.isEmpty(member.getNickname())
                || StringUtils.isEmpty(member.getAvatar())) {
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)//设置载荷
                .claim("id", member.getId())//claim可以自定义设置一些载荷字段，预定义的使用set方法来设置
                .claim("nickname", member.getNickname())
                .claim("avatar", member.getAvatar())
                .setIssuedAt(new Date())//这个就是预定义的字段
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))//这个设置的时间，就是当前时间戳+上我们设置的存活时间，就是三十分钟后失效
                .signWith(SignatureAlgorithm.HS256, APPSECRET)//设置签名哈希
                .compact();//设置jwt头，由于一般都是默认值，所以我们不做其他处理

        return token;
    }

    /**
     * 解析令牌里面的信息
     * @param token
     * @return
     */
    public static Claims checkJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
        return claims;
    }


}
