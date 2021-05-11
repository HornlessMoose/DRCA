package br.ufal.ic.db.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class Student extends Person {
    private Long credits = (long) 0 ;
    
    @OneToOne
    @NotNull(message = "Course can't be null")
    private Course course;
    
    @ElementCollection
    private List<Discipline> completedDisciplines;
    
    public Student(String name, Course course) {
    	super(name);
        this.course = course;
        this.completedDisciplines = new ArrayList<Discipline>();
    }
}