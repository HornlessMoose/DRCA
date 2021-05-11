package br.ufal.ic.db.models;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class Professor extends Person {
	
	public Professor(String name) {
		super(name);
	}
	
}