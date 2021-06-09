package uj.pwkp.gr1.vet.VetApp.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import uj.pwkp.gr1.vet.VetApp.controller.rest.request.UserRequest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

public class JwtGenerator {
    static String secret = "2qq9sVdJx1mSEPU6LhnXV242HdAd7STT";

    public static Optional<?> generateJWT(UserDetails userDetails) throws JOSEException {
        JWSSigner jwsSigner = new MACSigner(secret.getBytes(StandardCharsets.UTF_8));

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().
                claim("login", userDetails.getUsername())
                .claim("password", userDetails.getPassword())
                .claim("role", userDetails.getAuthorities().stream().findFirst().get().getAuthority())
                .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(), jwtClaimsSet);
        signedJWT.sign(jwsSigner);

        return Optional.of(signedJWT.serialize());
    }
}
