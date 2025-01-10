package org.example.gateway.service;

import org.example.gateway.model.BalanceHistoryDTO;
import org.example.gateway.model.BalanceHistoryRequestDTO;
import org.example.gateway.model.PrivilegeInfoResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 21.12.2024
 */
@Service
public class PrivilegeService {

    @Value("${privilege.service.url}")
    private String basicUrl;

    public PrivilegeInfoResponseDTO getUserPrivilege(String username) throws URISyntaxException {
        URI uri = new URI(basicUrl + "/privilege");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name",username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<PrivilegeInfoResponseDTO> result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                PrivilegeInfoResponseDTO.class
        );
        return result.getBody();
    }

    public PrivilegeInfoResponseDTO changeUserPrivilegeBalance(
            String username,
            BalanceHistoryRequestDTO balanceHistory
    ) throws URISyntaxException {
        URI uri = new URI(basicUrl + "/privilege");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name",username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(balanceHistory, headers);
        ResponseEntity<PrivilegeInfoResponseDTO> result = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                PrivilegeInfoResponseDTO.class
        );
        return result.getBody();
    }

    public PrivilegeInfoResponseDTO changeUserPrivilegeCauseCancel(
            String username,
            String ticketUid
    ) throws URISyntaxException {
        URI uri = new URI(basicUrl + "/privilege/" + ticketUid);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name",username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<PrivilegeInfoResponseDTO> result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                PrivilegeInfoResponseDTO.class
        );
        return result.getBody();
    }
}
