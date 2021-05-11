package br.ufal.ic.api;


import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Department;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretaryDTO {

    @ManyToOne
    private Department department;
    
    @Enumerated(EnumType.STRING)
    private SecretaryType secretaryType;

    @ElementCollection
    private List<Course> courses;
}
