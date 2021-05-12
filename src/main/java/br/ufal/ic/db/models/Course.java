package br.ufal.ic.db.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import br.ufal.ic.db.models.Secretary.SecretaryType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@RequiredArgsConstructor
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "Course name can't be blank")
	@Setter
	private String name;
	
	@Setter
	@ElementCollection
    private List<Discipline> disciplines;

	@Setter
	@NotNull(message = "Secretary type can't be null")
	SecretaryType secretaryType;
	
	public Course(String name, SecretaryType secretaryType) {
		this.name = name;
		this.secretaryType = secretaryType;
		this.disciplines = new ArrayList<Discipline>();
	}
}