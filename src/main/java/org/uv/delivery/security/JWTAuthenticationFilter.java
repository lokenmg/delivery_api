/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.delivery.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.uv.delivery.models.usuario.UsuarioBase;
import org.uv.delivery.models.usuario.Usuario1;
import org.uv.delivery.repository.UsuarioRepository;

/**
 *
 * @author juan
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    private final JWTUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    
    public JWTAuthenticationFilter(JWTUtils jwtUtils, UsuarioRepository usuarioRepository){
        this.jwtUtils=jwtUtils;
        this.usuarioRepository = usuarioRepository;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        Usuario1 u=null;
        String user;
        String password;
        try{
            u = new ObjectMapper().readValue(request.getInputStream(), Usuario1.class);
            user=u.getEmail();
            password=u.getPassword();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user, password);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response, 
                                            FilterChain chain, 
                                            Authentication authResult) 
                                            throws IOException, ServletException {
        
        User u=(User)authResult.getPrincipal();
        UsuarioBase usuario = usuarioRepository.findByEmail(u.getUsername());
        String [] parts = usuario.getClass().getName().split("[.]");
        String token=jwtUtils.generateAccesToken(u.getUsername(), usuario.getId(), parts[parts.length-1]);
        response.addHeader("Authorization", token);
        Map<String, String> httpResponse = new HashMap<>();
        httpResponse.put("id", String.valueOf(usuario.getId()));
        httpResponse.put("type", parts[parts.length-1]);
        httpResponse.put("token", token);
        httpResponse.put("message", "Correct Authentication");
        httpResponse.put("Username", u.getUsername());
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        response.getWriter().flush();
        
        super.successfulAuthentication(request, response, chain, authResult); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    
    
}
