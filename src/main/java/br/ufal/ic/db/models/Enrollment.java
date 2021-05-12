package br.ufal.ic.db.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Enrollment {
    
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "Enrollment number can't be null")
	private Integer number;

	@OneToOne
	@NotNull(message = "Student can't be null")
	private Student student;

	@ElementCollection
	private List<Discipline> currentDisciplines;
	
	public Enrollment(Student student, Integer number) {
		this.student = student;
		this.number = number;
		this.currentDisciplines = new ArrayList<Discipline>();
	}

}
