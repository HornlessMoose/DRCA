package br.ufal.ic.api;

import java.util.List;

import javax.persistence.ElementCollection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.ufal.ic.db.models.Discipline;
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
public  class CourseDTO {
    
    private String name;
    
    @ElementCollection
    private List<Discipline> disciplines;
    
    private SecretaryType secretaryType;
}