package book.management.controller;

import book.management.model.AuthenticationRequest;
import book.management.model.AuthenticationResponse;
import book.management.security.MyUserDetailsService;
import book.management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailService;

    @Autowired
    private JwtUtil jwtTokenUtil;
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthe(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails= userDetailService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt= jwtTokenUtil.generateToken(userDetails);

        System.out.println(jwt);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
