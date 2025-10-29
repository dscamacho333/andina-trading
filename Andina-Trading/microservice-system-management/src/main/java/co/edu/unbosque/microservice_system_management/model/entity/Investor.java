package co.edu.unbosque.microservice_system_management.model.entity;

import co.edu.unbosque.microservice_system_management.model.enums.AccountStatus;
import co.edu.unbosque.microservice_system_management.model.enums.OrderType;
import co.edu.unbosque.microservice_system_management.model.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class Investor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 20)
    private Role userRole;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 20)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_access")
    private LocalDateTime lastAccess;

    @Column(name = "has_subscription")
    private Boolean hasSubscription = false;

    @Column(name = "alpaca_status", length = 30)
    private String alpacaStatus;

    @Column(name = "alpaca_account_id", length = 100)
    private String alpacaAccountId;

    @Column(name = "daily_order_limit")
    private Integer dailyOrderLimit = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_order_type", length = 30)
    private OrderType defaultOrderType;

    @Column(name = "balance")
    private Float balance = 0f;

    @Column(name = "commission_rate")
    private Double commissionRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_user", nullable = false, length = 20)
    private Role roleUser;

    @Column(name = "bank_relationship_id")
    private String bankRelationshipId;
}
