package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InIndustryRepository extends JpaRepository<Industry, Integer> {

    @Query(
            value =
                    """
                    SELECT
                        i.id,
                        i.sector_id,
                        i.name,
                        i.is_active
                    FROM
                        INDUSTRY i
                    WHERE
                        i.is_active = True
                    """,
            nativeQuery = true
    )
    List<Industry> findAllActiveIndustries();


}
