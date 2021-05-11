package br.ufal.ic.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.ufal.ic.db.models.University;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public  class DepartmentDTO {
    
    private String name;
    private University university;

}