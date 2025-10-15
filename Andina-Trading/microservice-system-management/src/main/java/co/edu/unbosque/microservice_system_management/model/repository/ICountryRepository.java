package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.Country;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICountryRepository extends JpaRepository<Country, Integer> {

    @Modifying
    @Transactional
    @Query(
            value =
            """
            UPDATE
            	COUNTRY c
            SET
            	c.is_active = False
            WHERE
            	c.id = :countryId;
            """,
            nativeQuery = true
    )
    void deleteCountryByid(
            @Param("countryId") Integer countryId
    );


    @Query(
            value =
            """
            SELECT
            	c.id,
                c.code,
                c.name,
                c.is_active
            FROM
            	COUNTRY c
            WHERE
            	c.is_active = True;
            """,
            nativeQuery = true
    )
    List<Country> findAllActiveCountries();


}
