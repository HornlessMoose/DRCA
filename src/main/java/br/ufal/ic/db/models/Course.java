package br.ufal.ic.db.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import br.ufal.ic.db.models.Secretary.SecretaryType;

import javax.validation.constraints.NotBlank;

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

	@NotBlank(message = "The course name can't be blank")
	@Setter
	private String name;
	
	@Setter
	@ElementCollection
    private List<Discipline> disciplines;

	@Setter
	SecretaryType secretaryType;
	
	public Course(String name, SecretaryType secretaryType) {
		this.name = name;
		this.secretaryType = secretaryType;
		this.disciplines = new ArrayList<Discipline>();
	}
}