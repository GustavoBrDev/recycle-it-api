package com.ifsc.ctds.stinghen.recycle_it_api.security.utils;

import com.ifsc.ctds.stinghen.recycle_it_api.security.exceptions.CookieNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtils {

    private final String COOKIE_NAME = "JWTSESSION";

    public Cookie getJWTCookie(HttpServletRequest request) {

        Cookie [] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(COOKIE_NAME)) {
                    return cookie;
                }
            }
        }

        throw new CookieNotFoundException(COOKIE_NAME);
    }
}
