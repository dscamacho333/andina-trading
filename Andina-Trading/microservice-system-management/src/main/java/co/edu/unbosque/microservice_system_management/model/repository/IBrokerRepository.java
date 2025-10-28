package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.Broker;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBrokerRepository extends JpaRepository<Broker, Integer> {

    @Modifying
    @Transactional
    @Query(
            value =
                    """

                    UPDATE
                    	BROKER b
                    SET
                    	b.is_active = FALSE
                    WHERE
                    	b.id = :brokerId
                    """,
            nativeQuery = true
    )
    void deleteBrokerById(
            @Param("brokerId") Integer brokerId
    );

    @Query(
            value =
                    """
                    SELECT
                    	b.id,
                        b.first_name,
                        b.middle_name,
                        b.last_name,
                        b.document_type_id,
                        b.document_number,
                        b.email,
                        b.phone,
                        b.is_active,
                        b.created_at,
                        b.updated_at
                    FROM
                    	BROKER b
                    WHERE
                    	b.is_active = True
                    """,
            nativeQuery = true
    )
    List<Broker> findAllActiveBrokers();


    @Query(
            value =
                    """
                    SELECT
                    	b.id,
                        b.first_name,
                        b.middle_name,
                        b.last_name,
                        b.document_type_id,
                        b.document_number,
                        b.email,
                        b.phone,
                        b.is_active,
                        b.created_at,
                        b.updated_at
                    FROM
                    	BROKER b
                    INNER JOIN
                    	DOCUMENT_TYPE dt ON b.document_type_id = dt.id
                    INNER JOIN
                    	COUNTRY c ON dt.country_id = c.id
                    WHERE
                    	(b.is_active = True) AND (c.id = :countryId);
                    """,
            nativeQuery = true
    )
    List<Broker> findAllActiveBrokersByCountryId(
            @Param("countryId") Integer countryId
    );



}
