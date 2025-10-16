package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.EconomySituation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEconomySituationRepository extends JpaRepository<EconomySituation, Integer> {

    @Query(
            value =
            """
            SELECT
            	es.id,
                es.name,
                es.description,
                es.is_active
            FROM
            	ECONOMY_SITUATION es
            WHERE
            	es.is_active = True
            """,
            nativeQuery = true
    )
    List<EconomySituation> findAllActiveEconomySituations();


}
