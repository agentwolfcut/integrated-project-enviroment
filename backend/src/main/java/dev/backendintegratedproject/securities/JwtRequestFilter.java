package dev.backendintegratedproject.securities;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (uri.equals("/v2/login") || uri.equals("/v2/api-docs")) {
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired", e);
                request.setAttribute("jwt_error", "JWT Token has expired");
            } catch (SignatureException e) {
                logger.error("JWT Token has been tampered with", e);
                request.setAttribute("jwt_error", "JWT Token has been tampered with");
            } catch (Exception e){
                logger.error("JWT Token is invalid" , e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token Invalid");
                return;
            }

            // this mtf catch BUG for 403
//            } catch (IllegalArgumentException e) {
//                logger.error("JWT Token is invalid", e);
//                request.setAttribute("jwt_error", "JWT Token is invalid");
//
//            // dont need to catch this
//            } catch (UnsupportedJwtException e) {
//                logger.error("JWT Token is not supported", e);
//                request.setAttribute("jwt_error", "JWT Token is not supported");
//            } catch (MalformedJwtException e) {
//                logger.error("JWT Token is not well-formed", e);
//                request.setAttribute("jwt_error", "JWT Token is not well-formed");
//            }

        } else {
            logger.warn("JWT Token is missing or invalid");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is missing or invalid");
//            request.setAttribute("jwt_error", "JWT Token is missing or invalid");
            return;
        }

        // Validate and set security context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>());
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token is invalid or expired");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}