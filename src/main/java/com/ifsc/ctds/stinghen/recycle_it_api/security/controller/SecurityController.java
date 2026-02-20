package com.ifsc.ctds.stinghen.recycle_it_api.security.controller;

import com.ifsc.ctds.stinghen.recycle_it_api.security.dtos.LoginRequestDTO;
import com.ifsc.ctds.stinghen.recycle_it_api.security.utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class SecurityController {

    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;
    private final JWTUtils jwtUtils;

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDTO loginRequest, HttpServletRequest request, HttpServletResponse response) {

        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        auth = authenticationManager.authenticate( auth );

        if (auth.isAuthenticated()) {

            /*

            Mantem a sessão salva no servidor

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(auth);
            securityContextRepository.saveContext(context, request, response);*/

            // Uso do JWT
            String token = jwtUtils.generateToken((UserDetails) auth.getPrincipal());

            Cookie cookie = new Cookie("JWTSESSION", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 8); // 8 horas

            response.addCookie(cookie);

            response.setStatus(200);

        } else {
            response.setStatus(403);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        securityContextRepository.saveContext(SecurityContextHolder.createEmptyContext(), request, response);
    }
}
