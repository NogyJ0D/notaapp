package com.valentingiarra.notaapp.model;

import com.valentingiarra.notaapp.persistence.entities.User;
import com.valentingiarra.notaapp.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

@Component
public class JwtUtil {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private UserService userService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateAuthToken(User user) {
        try {
            long now = System.currentTimeMillis();

            return Jwts.builder()
                    .claim("id", user.getId())
                    .claim("username", user.getUsername())
                    .claim("firstname", user.getFirstname())
                    .claim("lastname", user.getLastname())
                    .claim("email", user.getEmail())
                    .claim("birthdate", user.getBirthdate())
                    .claim("userRole", user.getUserRole())
                    .setIssuedAt(new Date(now))
                    .setExpiration(new Date(now + 3600000)) // 1 hour
                    .signWith(Keys.hmacShaKeyFor(Objects.requireNonNull(jwtSecret).getBytes()), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            return null;
            //return "Error while generating token.";
        }
    }

    public Object isLogged(HttpServletRequest req) {
        try {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer")) {
                String token = authHeader.substring(7);

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                return claims.get("id", Long.class);
            }
        } catch (ExpiredJwtException e) {
            return "Session expired. Log in again.";
        } catch (MalformedJwtException | IllegalArgumentException e) {
            return e.getMessage();
        }

        return null;
    }

    public String canAccess(HttpServletRequest req, int requiredRole) {
        // Returns the userRole of the logged user or an error message
        Object loggedUser = this.isLogged(req);

        // Not logged
        if (loggedUser.getClass() == String.class) {
            return loggedUser.toString();
        }

        // Logged but not enough permissions
        // If not admin and less than requiredRole
        int userRole = userService.getUserRoleById((Long) loggedUser);
        if (userRole < requiredRole) {
            return "You don't have permission on that resource.";
        }

        return "success";
    }

//    private Integer parseIntOrNull(String value) {
//        try {
//            return Integer.parseInt(value);
//        } catch (NumberFormatException e) {
//            return null;
//        }
//    }
}
