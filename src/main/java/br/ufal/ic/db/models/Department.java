package br.ufal.ic.db.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@RequiredArgsConstructor
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Department name can't be blank")
    private String name;
    
    @ManyToOne
    @NotNull(message = "University can't be null")
    private University university;

    public Department(String name, University university) {
        this.name = name;
        this.university = university;
    }
}