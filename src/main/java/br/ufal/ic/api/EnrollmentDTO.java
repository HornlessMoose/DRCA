package br.ufal.ic.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Student;

@Setter
@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnrollmentDTO {
    private Integer number;

    @OneToOne
	private Student student;

    @ElementCollection
    private List<Discipline> currentDisciplines;

}
