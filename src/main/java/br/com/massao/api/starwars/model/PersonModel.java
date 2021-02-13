package br.com.massao.api.starwars.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor // used by JPA
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "person")
public class PersonModel {
    @Id
    @GeneratedValue
    private Long id;

    //@Column(name = "name", nullable = false)
    @Column(name = "name")
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "height")
    @Min(0)
    private int height;

    @Column(name = "mass")
    @Min(0)
    private int mass;

    @Column(name = "birth_year")
    @NotNull
    @NotEmpty
    private String birth_year;

    @Column(name = "gender")
    @NotNull
    @NotEmpty
    private String gender;

    @Column(name = "homeworld")
    @NotNull
    @NotEmpty
    private String homeworld;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonModel that = (PersonModel) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
