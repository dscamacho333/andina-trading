package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDocumentTypeRepository extends JpaRepository<DocumentType, Integer> {

    @Query(
            value =
            """
            SELECT
            	d.id,
                d.code,
                d.name,
                d.country_id,
                d.is_active
            FROM
            	DOCUMENT_TYPE d
            WHERE
            	d.is_active = True
            """,
            nativeQuery = true
    )
    List<DocumentType> findAllActiveDocumentTypes();


}
