package com.onbelay.dagnabit.graph.examples.genealogy;

import com.onbelay.dagnabit.graph.model.DagData;

import java.time.LocalDate;

public class PersonData implements DagData {

	private LocalDate birthDate;

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	
	
}
