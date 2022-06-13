package paw.project.backend_server.Utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import paw.project.backend_server.Constant.SecurityConstant;
import paw.project.backend_server.Domain.UserPrincipal;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class JWTTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    //at this point,the user passed the authetification
    public String generateJwtToken(UserPrincipal userPrincipal){
        String[] permissions=getClaimsFromUser(userPrincipal);

        return JWT.create().withIssuer(SecurityConstant.GET_ARRAYS_LLC).withAudience(SecurityConstant.GET_ARRAYS_ADMINISTRATION)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withArrayClaim(SecurityConstant.AUTHORITIES,permissions).withExpiresAt(new Date(System.currentTimeMillis()+SecurityConstant.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }
    
    public List<GrantedAuthority> getAuthorities(String token){
        String[] permissions=getClaimsFromToken(token);

        //go through the collection(stream==loop)
        return stream(permissions).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    //method that get the authentification -without passing the user info once we verified the token
    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest request){
        UsernamePasswordAuthenticationToken userPassAuthToken=new UsernamePasswordAuthenticationToken(username,null,authorities);

        userPassAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return userPassAuthToken;
    }
    //tells to Spring that this user is autenticated and passes their requests


    //check if token is valid
    public boolean isTokenValid(String username,String token){
        JWTVerifier verifier=getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpired(verifier,token);
    }

    //check if token is not expired
    private boolean isTokenExpired(JWTVerifier verifier, String token) {
        Date expirationDate=verifier.verify(token).getExpiresAt();
        return expirationDate.before(new Date());
    }

    public String getSubject(String token){
        JWTVerifier verifier=getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier=getJWTVerifier();
        return verifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try{
            Algorithm algorithm=Algorithm.HMAC512(secret);
            verifier=JWT.require(algorithm).withIssuer(SecurityConstant.GET_ARRAYS_LLC).build();

        }catch(JWTVerificationException exception){
            //we wll not throw the critical exception, we will give our custom message
            throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);

        }
        return verifier;

    }

    private String[] getClaimsFromUser(UserPrincipal user) {
        List<String> authorities=new ArrayList<>();

        for(GrantedAuthority grantedAuthority:user.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }

        return authorities.toArray(new String[0]);

    }


}
