package uj.pwkp.gr1.vet.VetApp.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class JwtFilter extends BasicAuthenticationFilter {
    static String secret = "2qq9sVdJx1mSEPU6LhnXV242HdAd7STT";

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        UsernamePasswordAuthenticationToken token = null;
        String jwt;
        if(header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);
            SignedJWT signedJWT = null;
            try {
                signedJWT = SignedJWT.parse(jwt);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(isSignatureValid(jwt) && signedJWT != null) {
                String username = (String) signedJWT.getPayload().toJSONObject().get("login");
                String password = (String) signedJWT.getPayload().toJSONObject().get("password");
                String role = (String) signedJWT.getPayload().toJSONObject().get("role");
                Set<SimpleGrantedAuthority> authoritySet = Collections.singleton(new SimpleGrantedAuthority(role));
                token = new UsernamePasswordAuthenticationToken(username, password, authoritySet);
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            }
        }
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }

    public boolean isSignatureValid(String token) {
        SignedJWT signedJWT;
        try {
            signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());
            if(signedJWT.verify(verifier)) {
                Date referenceTime = new Date();
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                Date expirationDate = claims.getExpirationTime();
                boolean expired = expirationDate == null || expirationDate.before(referenceTime);
                return !expired;
            }
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
        }
        return false;
    }
}
