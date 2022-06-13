package paw.project.backend_server.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import paw.project.backend_server.Constant.SecurityConstant;
import paw.project.backend_server.Filter.JWTAccessDeniedHandler;
import paw.project.backend_server.Filter.JWTAuthEntryPoint;
import paw.project.backend_server.Filter.JWTAuthFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private JWTAuthEntryPoint jwtAuthEntryPoint;
    private JWTAuthFilter jwtAuthFilter;
    private JWTAccessDeniedHandler jwtAccessDeniedHandler;
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfiguration(JWTAuthEntryPoint jwtAuthEntryPoint, JWTAuthFilter jwtAuthFilter,
                                 JWTAccessDeniedHandler jwtAccessDeniedHandler, @Qualifier("userDetailsService")UserDetailsService userDetailsService,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);//tell Spring using our userDetailsService

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //we have to pass in everything that we want spring security to manage in terms of endpoint URL
        //we will tell which URls to secure, which can be left open to the world,session management etc

        http.csrf().disable().cors()//not use Cross Site Request Forgery link:https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/csrf.html
                //we don't want anyone from any URL to be able to connect to our API,we want a specific list,or a specific URl,a specific domain to access our API(-cors-)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//tell spring security about our session
                    //we specify that our session management is gonna be stateless(we won't tracking anyone who logins , we don't need because JWT is doing this)
                .and().authorizeRequests().antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()//for this URLs we dont need to authenticate
                .anyRequest().authenticated()//otherwise we want to autenticate
                .and()
                .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
