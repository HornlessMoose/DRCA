package br.ufal.ic.api;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Discipline;
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
public class StudentDTO {
    private Long credits;

    @OneToOne
    private Course course;

    String name;

    @ElementCollection
    private List<Discipline> completedDisciplines;
}
