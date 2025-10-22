package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISectorRepository extends JpaRepository<Sector, Integer> {

    @Query(
            value =
            """
            SELECT
            	s.id,
                s.name,
                s.is_active
            FROM
            	SECTOR s
            WHERE
            	s.is_active = True
            """,
            nativeQuery = true
    )
    List<Sector> findAllActiveSectors();

}
