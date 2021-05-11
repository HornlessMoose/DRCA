package br.ufal.ic.api;
import java.util.Arrays;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.ufal.ic.db.models.Course;
import br.ufal.ic.db.models.Discipline;
import br.ufal.ic.db.models.Professor;
import br.ufal.ic.db.models.Student;
import br.ufal.ic.db.models.Discipline.DisciplineType;
import br.ufal.ic.db.models.Secretary.SecretaryType;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public  class DisciplineDTO {
    
    private String name;

	private String code;
    
	private Long credits, min_credits;
	
	@Enumerated(EnumType.STRING)
    private DisciplineType disciplineType;
	
	@Enumerated(EnumType.STRING)
    private SecretaryType secretaryType;
	
	@ElementCollection
	private List<Discipline> pre_disciplines = Arrays.asList();

	@ElementCollection
	private List<Student> students = Arrays.asList();;
	
	@OneToOne
	private Professor professor;
	
	@ManyToOne
	private Course course;
}