package br.ufal.ic.db.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Secretary {

    public enum SecretaryType {
        GRADUATE, POSTGRADUATE
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @NotNull(message = "Department can't be null")
    private Department department;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Secretary Type can't be null")
    private SecretaryType secretaryType;
    
    @ElementCollection
    private List<Course> courses;

    public Secretary(Department department, SecretaryType secretaryType) {
        this.department = department;
        this.secretaryType = secretaryType;
        this.courses = new ArrayList<Course>();
    }    
}
