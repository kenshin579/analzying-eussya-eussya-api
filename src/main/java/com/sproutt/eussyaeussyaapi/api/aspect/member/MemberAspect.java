package com.sproutt.eussyaeussyaapi.api.aspect.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@RequiredArgsConstructor
public class MemberAspect {

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    private final JwtHelper jwtHelper;
    private final MemberService memberService;

    @Around("execution(* *(.., @LoginMember (*), ..))")
    public Object convertMember(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(tokenKey);
        Member loginMember = getTokenOwner(token);

        return joinPoint.proceed(new Object[] {loginMember});
    }

    private Member getTokenOwner(String token) {
        JwtMemberDTO jwtMemberDTO = jwtHelper.decryptToken(secretKey, token);

        return memberService.findTokenOwner(jwtMemberDTO);
    }
}
