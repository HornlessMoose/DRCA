package br.ufal.ic.db.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.ufal.ic.db.models.Secretary.SecretaryType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Discipline {
	
	public enum DisciplineType {
        MANDATORY, ELECTIVE
    }
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "The course name can't be blank")
	private String name;
	private Long credits = (long) 0, min_credits = (long) 0;

	@NotNull(message = "Discipline code can't be null")
    private String code;
	
	@Enumerated(EnumType.STRING)
    private DisciplineType disciplineType;
	
	@Enumerated(EnumType.STRING)
    private SecretaryType secretaryType;
	
	@ElementCollection
    private List<Discipline> pre_discipline;
	
	@OneToOne
	private Professor professor;
	
	@ElementCollection
	private List<Student> students;
	
	public Discipline(String name, String code, DisciplineType disciplineType, SecretaryType secretaryType, Professor professor) {
		this.name = name;
		this.code = code;
		this.disciplineType = disciplineType;
		this.secretaryType = secretaryType;
		this.pre_discipline = new ArrayList<Discipline>();
		this.students = new ArrayList<Student>();
		this.professor = professor;
	}
	
	

}