package org.example.gateway.controller;

import org.example.gateway.model.PrivilegeDTO;
import org.example.gateway.model.PrivilegeInfoResponseDTO;
import org.example.gateway.model.TicketResponseDTO;
import org.example.gateway.model.UserInfoResponseDTO;
import org.example.gateway.service.PrivilegeService;
import org.example.gateway.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 10.01.2025
 */
@RestController
@RequestMapping("/api/v1/")
public class UserController {

    @Autowired
    TicketService ticketService;

    @Autowired
    PrivilegeService privilegeService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDTO> getUserInfo (
            @RequestHeader("X-User-Name") String username
    ) throws URISyntaxException {
        List<TicketResponseDTO> ticketsDTO = ticketService.getUserTicketDTOList(username);
        PrivilegeInfoResponseDTO privilegeInfo = privilegeService.getUserPrivilege(username);
        UserInfoResponseDTO userInfoDTO = new UserInfoResponseDTO();
        userInfoDTO.setTickets(ticketsDTO);
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        privilegeDTO.setBalance(Integer.parseInt(privilegeInfo.getBalance()));
        privilegeDTO.setStatus(privilegeInfo.getStatus());
        userInfoDTO.setPrivilege(privilegeDTO);
        return ResponseEntity.ok(userInfoDTO);
    }
}
