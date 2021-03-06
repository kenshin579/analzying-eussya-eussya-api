package com.sproutt.eussyaeussyaapi.api.oauth2;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.RequestUrlDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.OAuth2RequestService;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.SocialService;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/social")
@RestController
public class SocialController {

    private final JwtHelper jwtHelper;
    private final SocialService socialService;
    private final OAuth2RequestService oAuth2RequestService;

    @Value("${social.github.url}")
    private String githubRequestUrl;

    @Value("${social.google.url}")
    private String googleRequestUrl;

    @Value("${social.facebook.url}")
    private String facebookRequestUrl;

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostMapping("/login/{provider}")
    public ResponseEntity loginByProvider(@PathVariable String provider, @RequestParam String accessToken) {
        RequestUrlDto requestUrlDto = new RequestUrlDto(googleRequestUrl, githubRequestUrl, facebookRequestUrl);

        Member loginMember = oAuth2RequestService.getUserInfoByProvider(accessToken, provider, requestUrlDto);
        socialService.login(loginMember);
        String token = jwtHelper.createToken(secretKey, loginMember.toJwtInfo());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenKey, token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

}
