package co.edu.unbosque.microservice_system_management.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "CITY"
)
public class City {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_active", insertable = false)
    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(
            name = "country_id"
    )
    private Country country;

    @ManyToOne
    @JoinColumn(
            name = "economy_situation_id"
    )
    private EconomySituation economySituation;

}
