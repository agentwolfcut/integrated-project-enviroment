package dev.backendintegratedproject.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.backendintegratedproject.dtos.users.UserDetailsDTO;
import dev.backendintegratedproject.exceptions.ExceptionForm;
import dev.backendintegratedproject.primarydatasource.repositories.BoardPermissionRepository;
import dev.backendintegratedproject.primarydatasource.repositories.BoardRepository;
import dev.backendintegratedproject.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;

public class BoardPermissionFilter extends OncePerRequestFilter {
    @Autowired
    private BoardPermissionRepository boardPermissionRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String[] pathParts = request.getRequestURI().split("/");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (boardRepository.findById(pathParts[3]).isEmpty()) {
            if (authentication == null) {
                if (request.getMethod().equals("GET")) {
                    setErrorResponse(new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"), request, response);
                    return;
                } else {
                    setErrorResponse(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Board not found"), request, response);
                    return;
                }
            } else { //auth board not exist
                setErrorResponse(new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"), request, response);
                return;
            }
        }
        if (authentication == null) { //unauth case
            if (!boardRepository.getIsPublicByBoardID(pathParts[3])) { //not public
                if (request.getMethod().equals("GET")) {
                    setErrorResponse(new ResponseStatusException(HttpStatus.FORBIDDEN, "Board is not accessible"), request, response);
                    return;
                } else {
                    setErrorResponse(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Board not found"), request, response);
                    return;
                }
            } else {
                if (!request.getMethod().equals("GET")) {
                    setErrorResponse(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Board not found"), request, response);
                    return;
                }
            }
        }
        if (authentication != null) { //authn case
            UserDetailsDTO userDetailsDTO = (UserDetailsDTO) authentication.getPrincipal();
            if (pathParts[2].equals("boards") && pathParts.length > 3) {
                if (!boardPermissionRepository.checkBoardAccess(userDetailsDTO.getOid(), pathParts[3])) {
                    if (!boardRepository.getIsPublicByBoardID(pathParts[3])) {
                        setErrorResponse(new ResponseStatusException(HttpStatus.FORBIDDEN, "Board not found"), request, response);
                        return;
                    } else {
                        if (!request.getMethod().equals("GET")) {
                            setErrorResponse(new ResponseStatusException(HttpStatus.FORBIDDEN, "Board not found"), request, response);
                            return;
                        }
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        if ((!request.getMethod().equals("GET") && request.getRequestURI().startsWith("/v3/boards")) || (!request.getRequestURI().startsWith("/v3/boards"))) {
        if (!request.getRequestURI().startsWith("/v3/boards")) {
            return true;
        }
        String[] pathParts = request.getRequestURI().split("/");
        if (pathParts.length == 3) {
            return true;
        }
        return false;
    }

    private void setErrorResponse(ResponseStatusException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(ex.getStatusCode().value());
        response.setContentType("application/json");
        ExceptionForm exceptionForm = new ExceptionForm(ex.getStatusCode().value(), ex.getMessage(), request.getContextPath());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(exceptionForm));
    }
}
