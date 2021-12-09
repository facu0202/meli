package com.facuferro.meetup.controller;

import com.facuferro.meetup.api.UserOAuthResponse;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.security.SecurityConstant;
import com.facuferro.meetup.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@Slf4j
@Api(description = "Endpoints para autenticar usuarios.",tags = {"login"})
public class UserOAuthController {

    private UserService userService;

    @ApiOperation(value = "Autentica el usuario", notes = "Autentica el usuario", tags = { "login" })
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful operation", response= UserOAuthResponse.class )  })
    @PostMapping("/login")
    public ResponseEntity<UserOAuthResponse> login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
        log.info("Login user {}",username);
        User user = userService.getAndVerify(username, pwd);
        user.setToken(getJWTToken(user));
        return ResponseEntity.ok(UserOAuthResponse.createFrom(user));
    }

    private String getJWTToken(User user) {
        String secretKey = SecurityConstant.SECRET;
        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(user.getEmail())
                .claim("authorities",
                        getGrand(user).stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(new Date().getTime() + 1000 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }

    private List<GrantedAuthority> getGrand(User user) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole().toString());
    }

}
