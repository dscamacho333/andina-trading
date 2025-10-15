package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.City;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICityRepository extends JpaRepository<City, Integer> {

    @Modifying
    @Transactional
    @Query(
            value =
            """

            UPDATE
            	CITY c
            SET
            	c.is_active = False
            WHERE
            	c.id = :cityId;
            """,
            nativeQuery = true
    )
    void deleteCityById(
            @Param("cityId") Integer cityId
    );

    @Query(
            value =
            """
            SELECT
            	c.id,
                c.name,
                c.is_active,
                c.country_id,
                c.economy_situation_id
            FROM
            	CITY c
            WHERE
            	c.is_active = True;
            """,
            nativeQuery = true
    )
    List<City> findAllActiveCities();

    @Query(
            value =
            """
            SELECT
            	c.id,
                c.name,
                c.is_active,
                c.country_id,
                c.economy_situation_id
            FROM
            	CITY c
            INNER JOIN
            	ECONOMY_SITUATION ec ON c.id = ec.id
            WHERE
            	c.is_active = True AND ec.name = :economySituation;
            """,
            nativeQuery = true
    )
    List<City> findAllCitiesByEconomySituation(
            @Param("economySituation") String economySituation
    );

}
