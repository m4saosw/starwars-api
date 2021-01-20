package br.com.massao.api.starwars.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor // used by JPA
@AllArgsConstructor
@Builder
@Entity(name = "person")
public class PersonModel {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    @NotEmpty(message = "Name is empty")
    private String name;

    @Column(name = "height")
    @Min(0)
    private int height;

    @Column(name = "mass")
    @Min(0)
    private int mass;

    @Column(name = "birth_year")
    @NotEmpty(message = "Birth_year is empty")
    private String birth_year;

    @Column(name = "gender")
    @NotEmpty(message = "Gender is empty")
    private String gender;

    @Column(name = "homeworld")
    @NotEmpty(message = "Homeworld is empty")
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
