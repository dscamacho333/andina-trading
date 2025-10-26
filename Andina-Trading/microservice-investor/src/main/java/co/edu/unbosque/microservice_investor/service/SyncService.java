package co.edu.unbosque.microservice_investor.service;


import co.edu.unbosque.microservice_investor.model.dto.AccountResponseDTO;
import co.edu.unbosque.microservice_investor.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SyncService {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public SyncService(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    public void syncUserAccountData(Integer userId, String alpacaAccountId) {
        // Obtener al usuario por ID
        UserDTO user = userService.getUserById(userId);

        // Obtener la información desde Alpaca
        AccountResponseDTO accountInfo = accountService.getAccountInfo(alpacaAccountId);

        // Actualizar los campos del usuario con los datos más recientes
        user.setAlpacaStatus(accountInfo.getStatus());
        user.setAlpacaAccountId(accountInfo.getId());
        user.setLastAccess(LocalDateTime.now());

        // Guardar los cambios en bd
        userService.updateUser(user);
    }
}