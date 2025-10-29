package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.Investor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InInvestorRepository extends JpaRepository<Investor, Integer> {

    @Modifying
    @Transactional
    @Query(
            value =
                    """
                    UPDATE
                    	USERS u
                    SET
                    	u.account_status = 'INACTIVE'
                    WHERE
                    	u.user_id = :investorId
                    """,
            nativeQuery = true
    )
    void deleteInvestorById(
            @Param("investorId") Integer investorId
    );

    @Query(
            value =
                    """
                    SELECT
                      u.user_id,
                      u.name,
                      u.email,
                      u.password_hash,
                      u.user_role,
                      u.phone,
                      u.account_status,
                      u.created_at,
                      u.last_access,
                      u.has_subscription,
                      u.alpaca_status,
                      u.alpaca_account_id,
                      u.daily_order_limit,
                      u.default_order_type,
                      u.balance,
                      u.commission_rate,
                      u.role_user,
                      u.bank_relationship_id
                    FROM
                    	USERS u
                    WHERE
                    	u.account_status = :accountStatus
                    """,
            nativeQuery = true
    )
    List<Investor> findAllInvestorsByAccountStatus(
            @Param("accountStatus") String accountStatus
    );


}
