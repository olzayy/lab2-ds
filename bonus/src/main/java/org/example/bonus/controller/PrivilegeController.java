package org.example.bonus.controller;

import org.example.bonus.entity.Privilege;
import org.example.bonus.entity.PrivilegeHistory;
import org.example.bonus.model.BalanceHistoryDTO;
import org.example.bonus.model.BalanceHistoryRequestDTO;
import org.example.bonus.model.PrivilegeInfoResponseDTO;
import org.example.bonus.repository.PrivilegeHistoryRepository;
import org.example.bonus.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 21.12.2024
 */
@RestController
@RequestMapping
public class PrivilegeController {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PrivilegeHistoryRepository privilegeHistoryRepository;

    @GetMapping("/privilege")
    public ResponseEntity<PrivilegeInfoResponseDTO> getUserPrivilege(
            @RequestHeader("X-User-Name")  String username
    ){
        Privilege privilege = privilegeRepository.findPrivilegeByUserName(username);
        if (privilege == null) {
            Privilege newPrivilege = new Privilege();
            newPrivilege.setUserName(username);
            newPrivilege.setStatus("BRONZE");
            newPrivilege.setBalance(0);
            privilege = privilegeRepository.save(newPrivilege);
        }
        return ResponseEntity.ok(preparePrivilegeInfoRespDTO(privilege));
    }

    @GetMapping("/privilege/{ticketUid}")
    public ResponseEntity<PrivilegeInfoResponseDTO> deleteTicketUserPrivilege(
            @RequestHeader("X-User-Name")  String username,
            @PathVariable("ticketUid") String ticketUid
    ){
        Privilege privilege = privilegeRepository.findPrivilegeByUserName(username);
        if (privilege == null) {
            Privilege newPrivilege = new Privilege();
            newPrivilege.setUserName(username);
            newPrivilege.setStatus("BRONZE");
            newPrivilege.setBalance(0);
            privilege = privilegeRepository.save(newPrivilege);
        }
        List<PrivilegeHistory> privilegeHistoryList = privilege.getPrivilegeHistoryList();
        for (PrivilegeHistory privilegeHistory : privilegeHistoryList) {
            if (privilegeHistory.getTicketUid().equals(UUID.fromString(ticketUid))) {
                if (privilegeHistory.getOperationType().equals("FILL_IN_BALANCE")) {
                    privilege.setBalance(privilege.getBalance() - privilegeHistory.getBalanceDiff());
                }
                else if (privilegeHistory.getOperationType().equals("DEBIT_THE_ACCOUNT")) {
                    privilege.setBalance(privilege.getBalance() + privilegeHistory.getBalanceDiff());
                }
                privilegeRepository.save(privilege);
            }
        }
        return ResponseEntity.ok(preparePrivilegeInfoRespDTO(privilege));
    }

    @PostMapping("/privilege")
    public ResponseEntity<PrivilegeInfoResponseDTO> changeUserPrivilegeBalance(
            @RequestHeader("X-User-Name")  String username,
            @RequestBody BalanceHistoryRequestDTO balanceHistory
    ) {
        PrivilegeHistory privilegeHistory = new PrivilegeHistory();
        Privilege privilege = privilegeRepository.findPrivilegeByUserName(username);
        privilegeHistory.setPrivilege(privilege);
        privilegeHistory.setTicketUid(UUID.fromString(balanceHistory.getTicketUid()));
        privilegeHistory.setDateTime(balanceHistory.getDate().toInstant().atOffset(ZoneOffset.UTC));
        privilegeHistory.setOperationType(balanceHistory.getOperationType());
        privilegeHistory.setBalanceDiff(balanceHistory.getBalanceDiff());
        privilegeHistoryRepository.save(privilegeHistory);
        if (balanceHistory.getOperationType().equals("DEBIT_THE_ACCOUNT")) {
            privilege.setBalance(privilege.getBalance() - balanceHistory.getBalanceDiff());
            privilegeRepository.save(privilege);
        }
        else if (balanceHistory.getOperationType().equals("FILL_IN_BALANCE")) {
            privilege.setBalance(privilege.getBalance() + balanceHistory.getBalanceDiff());
            privilegeRepository.save(privilege);
        }
        Privilege result = privilegeRepository.findPrivilegeByUserName(username);
        return ResponseEntity.ok(preparePrivilegeInfoRespDTO(result));
    }

    private PrivilegeInfoResponseDTO preparePrivilegeInfoRespDTO(Privilege privilege) {
        PrivilegeInfoResponseDTO response = new PrivilegeInfoResponseDTO();
        response.setBalance(privilege.getBalance().toString());
        response.setStatus(privilege.getStatus());
        List<BalanceHistoryDTO> balanceHistoryList = new ArrayList<>();
        List<PrivilegeHistory> historyList = privilege.getPrivilegeHistoryList();
        if (historyList != null) {
            for (PrivilegeHistory history : historyList) {
                BalanceHistoryDTO balanceHistory = new BalanceHistoryDTO();
                balanceHistory.setDate(history.getDateTime().toString());
                balanceHistory.setTicketUid(history.getTicketUid().toString());
                balanceHistory.setBalanceDiff(history.getBalanceDiff().toString());
                balanceHistory.setOperationType(history.getOperationType());
                balanceHistoryList.add(balanceHistory);
            }
        }
        response.setHistory(balanceHistoryList);
        return response;
    }
}
