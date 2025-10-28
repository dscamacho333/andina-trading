package co.edu.unbosque.microservice_investor.service;

import co.edu.unbosque.microservice_investor.exception.InsufficientBalanceException;
import co.edu.unbosque.microservice_investor.model.dto.LoginResponse;
import co.edu.unbosque.microservice_investor.model.dto.UserDTO;
import co.edu.unbosque.microservice_investor.model.entity.User;
import co.edu.unbosque.microservice_investor.model.enums.AccountStatus;
import co.edu.unbosque.microservice_investor.model.enums.Role;
import co.edu.unbosque.microservice_investor.repository.UserRepository;
import co.edu.unbosque.microservice_investor.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private JwtUtil jwtUtil;


    @Autowired
    public UserService(UserRepository userRepository,  ModelMapper modelMapper, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
    }


    // Login user
    public LoginResponse loginUser(String email, String rawPassword) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Correo no registrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        UserDTO dto = modelMapper.map(user, UserDTO.class);
        dto.setPasswordHash(null);
        LoginResponse response = new LoginResponse(jwtUtil.generateToken(dto), false);
        response.setUser(dto);


        return response;
    }


    public UserDTO getUserById(Integer id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public float getBalance(Integer userId) {
        return userRepository.findById(userId)
                .map(User::getBalance)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
    }

    public void updateUser(UserDTO userDTO) {userRepository.save(modelMapper.map(userDTO, User.class));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }



    public boolean emailExists(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    public List<UserDTO> getUsersByRole(Role role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByStatus(AccountStatus status) {
        return userRepository.findAll().stream()
                .filter(user -> user.getAccountStatus() == status)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersWithSubscription() {
        return userRepository.findAll().stream()
                .filter(User::getHasSubscription)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public List<UserDTO> searchUsersByName(String name) {
        return userRepository.findAll().stream()
                .filter(user -> user.getName() != null && user.getName().toLowerCase().contains(name.toLowerCase()))
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public void subtractFromBalance(Integer userId, float amount) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        float currentBalance = user.getBalance();

        validateBalance(userId, amount);

        user.setBalance(currentBalance - amount);
        userRepository.save(user);
    }

    public void validateBalance(Integer userId, float amount) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        float currentBalance = user.getBalance();

        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException("No tienes saldo suficiente para completar esta orden, saldo requerido: "+amount+", saldo actual: "+currentBalance+".");
        }

    }
    public void addToBalance(Integer userId, float amount) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID " + userId));
        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    public void updateUserPreferences(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userDTO.getId()));

        if (userDTO.getDailyOrderLimit() != null) {
            user.setDailyOrderLimit(userDTO.getDailyOrderLimit());
        }

        if (userDTO.getDefaultOrderType() != null) {
            user.setDefaultOrderType(userDTO.getDefaultOrderType());
        }
        System.out.println(userDTO.getDefaultOrderType());
        userRepository.save(user);
    }
}