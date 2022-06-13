package paw.project.backend_server.Filter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import paw.project.backend_server.Constant.SecurityConstant;
import paw.project.backend_server.Utility.JWTTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//authorize or not authorize any request that comes in
//it will happen once
//this filter it's gonna fire every time there is a new request and it's only gonna fire once
@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    private JWTTokenProvider jwtTokenProvider;

    public JWTAuthFilter(JWTTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //check to make sure that token is valid, the user is valid, and after that, set that user as an autentificated user
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //we have to take that request and make sure that method is not options--whenever a request goes to the server,??
        //if is options,which is a request that can be sent before every request collect informations about the server, we have to let it through
        if(request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)){
            response.setStatus(HttpStatus.OK.value());
        }else{
            String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
            if(authHeader==null || !authHeader.startsWith(SecurityConstant.TOKEN_PREFIX)){
                filterChain.doFilter(request,response);
                return;
            }
            String token=authHeader.substring(SecurityConstant.TOKEN_PREFIX.length());
            String username=jwtTokenProvider.getSubject(token);//the subject

            if(jwtTokenProvider.isTokenValid(username,token) && SecurityContextHolder.getContext().getAuthentication()==null){//the check in the security context holder is not needed if we not use session!!!
                List<GrantedAuthority> authorities=jwtTokenProvider.getAuthorities(token);

                Authentication authentication=jwtTokenProvider.getAuthentication(username,authorities,request);

                SecurityContextHolder.getContext().setAuthentication(authentication);//the user is now authenticated

            }else{
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request,response);//we let the request to continue it's course

    }
}
