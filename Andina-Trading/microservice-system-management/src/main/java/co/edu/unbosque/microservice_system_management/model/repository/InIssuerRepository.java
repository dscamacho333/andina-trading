package co.edu.unbosque.microservice_system_management.model.repository;

import co.edu.unbosque.microservice_system_management.model.entity.Issuer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InIssuerRepository extends JpaRepository<Issuer, Integer> {

    @Modifying
    @Transactional
    @Query(
            value =
            """

            UPDATE
            	ISSUER i
            SET
            	i.is_active = False
            WHERE
            	i.id = :issuerId
            """,
            nativeQuery = true
    )
    void deleteIssuerById(
            @Param("issuerId") Integer issuerId
    );

    @Query(
            value =
                    """
                    SELECT
                        i.id,
                        i.name,
                        i.ticker,            
                        i.country_id,
                        i.industry_id,               
                        i.website,
                        i.notes,
                        i.is_active
                    FROM
                        ISSUER i
                    WHERE
                        i.is_active = True
                    """,
            nativeQuery = true
    )
    List<Issuer> findAllActiveIssuers();

    @Query(
            value =
                    """

                    SELECT
                    	i.id,
                    	i.name,
                    	i.ticker,               
                    	i.country_id,
                    	i.industry_id,               
                    	i.website,
                    	i.notes,
                    	i.is_active
                    FROM
                    	ISSUER i
                    WHERE
                    	i.is_active = True AND i.country_id = :countryId
                    """,
            nativeQuery = true
    )
    List<Issuer> findAllIssuersByCountry(
            @Param("countryId") Integer countryId
    );

    @Query(
            value =
                    """

                    SELECT
                    	i.id,
                    	i.name,
                    	i.ticker,               
                    	i.country_id,
                    	i.industry_id,               
                    	i.website,
                    	i.notes,
                    	i.is_active
                    FROM
                    	ISSUER i
                    WHERE
                    	i.is_active = True AND i.industry_id = :industryId
                    """,
            nativeQuery = true
    )
    List<Issuer> findAllIssuersByIndustry(
            @Param("industryId") Integer industryId
    );

    @Query(
            value =
                    """

                    SELECT
                    	i.id,
                    	i.name,
                    	i.ticker,              
                    	i.country_id,
                    	i.industry_id,               
                    	i.website,
                    	i.notes,
                    	i.is_active
                    FROM
                    	ISSUER i
                    INNER JOIN
                    	INDUSTRY ind ON i.industry_id = ind.id
                    WHERE
                    	i.is_active = True AND ind.sector_id = :sectorId
                    """,
            nativeQuery = true
    )
    List<Issuer> findAllIssuersBySector(
            @Param("sectorId") Integer sectorId
    );

}
