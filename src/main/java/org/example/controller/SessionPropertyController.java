package org.example.controller;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.example.model.session.SessionProperty;
import org.example.repository.session.SessionPropertyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/external")
public class SessionPropertyController
{
    private final SessionPropertyRepository sessionPropertyRepository;

    public SessionPropertyController(SessionPropertyRepository sessionPropertyRepository)
    {
        this.sessionPropertyRepository = sessionPropertyRepository;
    }

    @GetMapping("/session/property")
    public List<SessionProperty> getAllSessionProperties() {
        return ImmutableList.copyOf(sessionPropertyRepository.findAll());
    }

    @PostMapping("/session/property")
    public ResponseEntity<String> addSessionProperties(@RequestBody SessionProperty sessionProperty)
    {
        try {
            log.info("Add session property : {}", sessionProperty.toString());
            log.info("Session property class : {}", sessionProperty.getClass().getName());
            sessionPropertyRepository.save(sessionProperty);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Session property added: " + sessionProperty);
    }

    @PutMapping("/session/property")
    public ResponseEntity<String> updateSessionProperties(@RequestBody SessionProperty sessionProperty)
    {
        try {
            SessionProperty original = sessionPropertyRepository.findByUniqueKey(sessionProperty.getUniqueKey());
            log.info("find session property : {}", sessionProperty);
            if (original == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session property not found: " + sessionProperty.getUniqueKey());
            }
            log.info("Update session property : {}", sessionProperty);
            log.info("Session property class : {}", sessionProperty.getClass().getName());
            sessionPropertyRepository.updateSessionProperty(
                    sessionProperty.getUniqueKey(),
                    sessionProperty.getDefaultValue(),
                    sessionProperty.getMinValue(),
                    sessionProperty.getMaxValue(),
                    sessionProperty.getDescription());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok("Updated: " + sessionProperty);
    }

    @DeleteMapping("/session/property")
    public ResponseEntity<String> deleteSessionProperties(@RequestParam(name = "property") String property)
    {
        try {
            sessionPropertyRepository.deleteByUniqueKey(property);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok(property + " deleted!");
    }
}
