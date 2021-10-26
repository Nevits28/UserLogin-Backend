package com.nevits.controller.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Calendar;
import java.util.Date;
import com.nevits.exception.InvalidCredentialsException;
import com.nevits.repository.document.User;
import com.nevits.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nevits.utils.Constants.CLAIMS_ROLES_KEY;
import static com.nevits.utils.Constants.TOKEN_DURATION_MINUTES;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping( "/v1/auth" )
public class AuthController {
    @Value("${app.secret}")
    String secret;

    private final UserService userService;

    public AuthController(@Autowired UserService userService) {
        this.userService = userService;
    }
    
    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping
    public TokenDto login(@RequestBody LoginDto loginDto) {
        User user = userService.findByEmail(loginDto.email);
        if (BCrypt.checkpw(loginDto.password, user.getPasswordHash())) {
            return generateTokenDto(user);
        } else {
            throw new InvalidCredentialsException();
        }

    }

    private String generateToken(User user, Date expirationDate) {
        return Jwts.builder()
                .setSubject(user.getId())
                .claim(CLAIMS_ROLES_KEY, user.getRoles())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private TokenDto generateTokenDto(User user) {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.MINUTE, TOKEN_DURATION_MINUTES);
        String token = generateToken(user, expirationDate.getTime());
        return new TokenDto(token, expirationDate.getTime());
    }
}
