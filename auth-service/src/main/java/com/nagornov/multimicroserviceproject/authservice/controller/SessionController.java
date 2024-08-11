package com.nagornov.multimicroserviceproject.authservice.controller;

import com.nagornov.multimicroserviceproject.authservice.config.security.jwt.JwtAuthentication;
import com.nagornov.multimicroserviceproject.authservice.dto.jwt.JwtResponse;
import com.nagornov.multimicroserviceproject.authservice.dto.session.DeleteSessionRequest;
import com.nagornov.multimicroserviceproject.authservice.dto.session.UpdateSessionRequest;
import com.nagornov.multimicroserviceproject.authservice.dto.session.CreateSessionRequest;
import com.nagornov.multimicroserviceproject.authservice.model.Session;
import com.nagornov.multimicroserviceproject.authservice.service.JwtService;
import com.nagornov.multimicroserviceproject.authservice.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final JwtService jwtService;

    @PostMapping("/api/session")
    public ResponseEntity<?> createSession(@RequestBody CreateSessionRequest request) {
        sessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Session has been created");
    }

    @GetMapping("/api/session")
    public ResponseEntity<?> getSessions(@RequestParam String service) {
        JwtAuthentication authInfo = jwtService.getAuthInfo();

        List<Session> data = sessionService.getSessions(service, authInfo.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping("/api/session/update")
    public ResponseEntity<?> updateSession(@RequestBody UpdateSessionRequest request) {
        Optional<JwtResponse> tokens = sessionService.updateSession(request);
        if (tokens.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Session update error");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tokens.get());
    }

    @PostMapping("/api/session/delete")
    public ResponseEntity<?> deleteSession(@RequestBody DeleteSessionRequest request) {
        JwtAuthentication authInfo = jwtService.getAuthInfo();

        sessionService.deleteSession(request, authInfo.getUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}