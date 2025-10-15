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
    private String name;
    private boolean isActive;

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
