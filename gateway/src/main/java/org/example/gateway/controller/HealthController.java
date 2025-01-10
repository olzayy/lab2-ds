package org.example.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер проверки жизнеспособности gateway
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@RestController
@RequestMapping("/manage")
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<?> getServiceStatus() {
        return ResponseEntity.ok().build();
    }
}
