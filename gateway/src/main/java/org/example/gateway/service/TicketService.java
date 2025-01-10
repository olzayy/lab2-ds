package org.example.gateway.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.gateway.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Service
public class TicketService {

    @Value("${ticket.service.url}")
    private String basicUrl;

    @Autowired
    private FlightService flightService;

    @Autowired
    private PrivilegeService privilegeService;

    public List<TicketResponseDTO> getUserTicketDTOList(String username) throws URISyntaxException {
        List<TicketResponseDTO> tickets = getUserTickets(username);
        if (!tickets.isEmpty()) {
            tickets.forEach(ticket -> {
                try {
                    ticket.setDate(flightService.getFlight(ticket.getFlightNumber()).getDate());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        setAirportsToTickets(tickets);
        return tickets;
    }

    private List<TicketResponseDTO> getUserTickets(String username) throws URISyntaxException {
        URI uri = new URI(basicUrl + "/tickets");      // +username);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name", username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ArrayList<TicketResponseDTO>> result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {});
        return result.getBody();
    }

    public TicketResponseDTO getUserTicketDTO(
            String username, String ticketUid
    ) throws URISyntaxException, ErrorResponse {
        URI uri = new URI(basicUrl + "/tickets/" + ticketUid);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        headers.add("X-User-Name", username);
        try {
            ResponseEntity<TicketResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, TicketResponseDTO.class);
            TicketResponseDTO response = result.getBody();
            response.setToAirport(
                    flightService.getAirportsInfo(response.getFlightNumber()).getToAirportCity() +
                    " " +
                    flightService.getAirportsInfo(response.getFlightNumber()).getToAirportName()
            );
            response.setFromAirport(
                    flightService.getAirportsInfo(response.getFlightNumber()).getFromAirportCity() +
                    " " +
                    flightService.getAirportsInfo(response.getFlightNumber()).getFromAirportName()
            );
            response.setDate(flightService.getFlight(response.getFlightNumber()).getDate());
            return response;
        } catch (Exception e){
            throw new ErrorResponse("Билет не найден");
        }
    }

    public TicketResponseDTO deleteUserTicket(
            String username, String ticketUid
    ) throws URISyntaxException, ErrorResponse {
        URI uri = new URI(basicUrl + "/tickets/" + ticketUid);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        headers.add("X-User-Name", username);
        try {
            ResponseEntity<TicketResponseDTO> result = restTemplate.exchange(
                    uri,
                    HttpMethod.DELETE,
                    entity,
                    TicketResponseDTO.class
            );
            privilegeService.changeUserPrivilegeCauseCancel(username, ticketUid);
            return result.getBody();
        } catch (Exception e) {
            throw new ErrorResponse("Билет не найден");
        }
    }

    public TicketPurchaseResponseDTO buyTicket(
            String username,
            TicketPurchaseRequestDTO ticketPurchase
    ) throws URISyntaxException, ValidationErrorResponse {
        FlightResponseDTO flightResponseDTO = flightService.getFlight(ticketPurchase.getFlightNumber());
        if (flightResponseDTO == null) {
            throw new ValidationErrorResponse("Рейса не существует", new ArrayList<>());
        }
        TicketRequestDTO requestDTO = new TicketRequestDTO();
        requestDTO.setUserName(username);
        requestDTO.setFlightNumber(ticketPurchase.getFlightNumber());
        requestDTO.setPrice(ticketPurchase.getPrice());
        requestDTO.setStatus("PAID");

        URI uri = new URI(basicUrl + "/tickets");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name", username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(requestDTO, headers);
        ResponseEntity<TicketResponseDTO> ticketResp = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                TicketResponseDTO.class
        );
        BalanceHistoryRequestDTO balanceHistory = new BalanceHistoryRequestDTO();
        balanceHistory.setTicketUid(ticketResp.getBody().getTicketUid());
        balanceHistory.setDate(new Date());
        PrivilegeInfoResponseDTO privilegeInfo = privilegeService.getUserPrivilege(username);
        int userBalance = Integer.parseInt(privilegeInfo.getBalance());
        int amount;
        int paidByMoney;
        int paidByBonuses;
        PrivilegeInfoResponseDTO privilegeResponse;
        if (ticketPurchase.isPaidFromBalance() && userBalance != 0) {
            amount = Math.min(userBalance, ticketPurchase.getPrice());
            balanceHistory.setOperationType("DEBIT_THE_ACCOUNT");
            balanceHistory.setBalanceDiff(amount);
            privilegeResponse = privilegeService.changeUserPrivilegeBalance(username, balanceHistory);
            if (ticketPurchase.getPrice() > userBalance) {
                paidByMoney = ticketPurchase.getPrice() - userBalance;
                paidByBonuses = userBalance;
            }
            else {
                paidByMoney = 0;
                paidByBonuses = userBalance;
            }
        }
        else {
            amount = ticketPurchase.getPrice() / 10;
            balanceHistory.setOperationType("FILL_IN_BALANCE");
            balanceHistory.setBalanceDiff(amount);
            privilegeResponse = privilegeService.changeUserPrivilegeBalance(username, balanceHistory);
            paidByMoney = ticketPurchase.getPrice();
            paidByBonuses = 0;
        }

        TicketPurchaseResponseDTO result = new TicketPurchaseResponseDTO();
        result.setTicketUid(ticketResp.getBody().getTicketUid());
        result.setFlightNumber(ticketResp.getBody().getFlightNumber());
        setAirportToTicket(result);
        result.setDate(flightService.getFlight(ticketPurchase.getFlightNumber()).getDate());
        result.setPrice(ticketResp.getBody().getPrice());
        result.setPaidByMoney(paidByMoney);
        result.setPaidByBonuses(paidByBonuses);
        result.setStatus("PAID");
        result.setPrivilege(
                new PrivilegeDTO(Integer.parseInt(privilegeResponse.getBalance()), privilegeResponse.getStatus())
        );
        return result;
    }

    private void setAirportToTicket(TicketPurchaseResponseDTO ticket) throws URISyntaxException {
        ticket.setToAirport(
                flightService.getAirportsInfo(ticket.getFlightNumber()).getToAirportCity() +
                " " +
                flightService.getAirportsInfo(ticket.getFlightNumber()).getToAirportName()
        );
        ticket.setFromAirport(
                flightService.getAirportsInfo(ticket.getFlightNumber()).getFromAirportCity() +
                " " +
                flightService.getAirportsInfo(ticket.getFlightNumber()).getFromAirportName()
        );
    }

    private void setAirportsToTickets(List<TicketResponseDTO> tickets) throws URISyntaxException {
        for (TicketResponseDTO ticket : tickets) {
            ticket.setToAirport(
                    flightService.getAirportsInfo(ticket.getFlightNumber()).getToAirportCity() +
                    " " +
                    flightService.getAirportsInfo(ticket.getFlightNumber()).getToAirportName()
            );
            ticket.setFromAirport(
                    flightService.getAirportsInfo(ticket.getFlightNumber()).getFromAirportCity() +
                    " " +
                    flightService.getAirportsInfo(ticket.getFlightNumber()).getFromAirportName()
            );
        }
    }
}
