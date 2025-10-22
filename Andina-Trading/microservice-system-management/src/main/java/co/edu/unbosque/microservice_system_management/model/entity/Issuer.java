package co.edu.unbosque.microservice_system_management.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Table(
        name = "ISSUER"
)
@Entity
public class Issuer {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "ticker")
    private String ticker;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;

    @Column(name = "website")
    private String website;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_active", insertable = false, updatable = false)
    private boolean isActive ;


}
