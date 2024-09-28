package com.koiteampro.koipondcons.config;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.exception.AuthenException;
import com.koiteampro.koipondcons.services.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    //ai cũng có thể truy cập được
    private final List<String> AUTH_PERMISSION = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/login",
            "/api/register",
            "/submitOrder",
            "/vnpay-payment-return",
            "/"
    );

    public boolean isPublic(String uri) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return AUTH_PERMISSION.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return  null;
        return authHeader.substring(7);
        //Bearer dfdfsf21323
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isPublicApi = isPublic(request.getRequestURI());
        if (isPublicApi)
            filterChain.doFilter(request, response);
        else {
            String token = getToken(request);
            if (token == null) {
                resolver.resolveException(request, response, null, new AuthenException("Empty token!"));
                return;
            }
            Account account;
            try {
                account = tokenService.getAccountByToken(token);
            } catch (ExpiredJwtException e) {
                //lỗi token hết hạn
                resolver.resolveException(request, response, null, new AuthenException("Expired token!"));
                return;
            } catch (MalformedJwtException e) {
                //lỗi response token sai, ko đúng định dạng
                resolver.resolveException(request, response, null, new AuthenException("Invalid token!"));
                return;
            }
            //lưu lại thông tin account
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    account,
                    token,
                    account.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        }
    }
}