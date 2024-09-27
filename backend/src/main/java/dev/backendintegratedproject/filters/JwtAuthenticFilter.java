package dev.backendintegratedproject.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.exceptions.ExceptionForm;
import dev.backendintegratedproject.services.UserService;
import dev.backendintegratedproject.util.JwtUtils;

import java.io.IOException;

@Component
public class JwtAuthenticFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;


    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/v3/token")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = getJWTfromRequest(request);
        String username = null;
        String oid = null;

        if (token != null) {
            try {
                username = jwtUtils.getUsernameFromToken(token);
                oid = jwtUtils.getAllClaimsFromToken(token).get("oid", String.class);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }catch (SignatureException e) {
                setErrorResponse(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwt Token is invalid"), request, response);
                return;
            }catch (MalformedJwtException e) {
                setErrorResponse(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwt Token is invalid"), request, response);
                return;
            } catch (ExpiredJwtException e) {
                setErrorResponse(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwt Token is Expired"), request, response);
                return;
            }

        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try{
                UserDetailsDTO userDetails = userService.loadUserByUsername(username);
                if (jwtUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (UsernameNotFoundException e){
                setErrorResponse(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwt Token is Expired"), request, response);
                return;
            }



        }
        filterChain.doFilter(request, response);
    }

    private String getJWTfromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    private void setErrorResponse(ResponseStatusException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(ex.getStatusCode().value());
        response.setContentType("application/json");
        ExceptionForm exceptionForm = new ExceptionForm(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), request.getContextPath());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(exceptionForm));
    }
}
