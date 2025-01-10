package org.example.gateway.controller;

import org.example.gateway.model.PrivilegeInfoResponseDTO;
import org.example.gateway.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 21.12.2024
 */
@RestController
@RequestMapping("/api/v1/")
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @GetMapping(value = "/privilege")
    public ResponseEntity<PrivilegeInfoResponseDTO> getLoyaltyInfo(@RequestHeader("X-User-Name") String username) throws URISyntaxException {
        PrivilegeInfoResponseDTO privilege = privilegeService.getUserPrivilege(username);
        return ResponseEntity.ok(privilege);
    }
}
