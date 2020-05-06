package com.onbelay.dagnabit.graph.examples.genealogy;

import java.time.LocalDate;

import com.onbelay.dagnabit.graph.model.DagData;

public class PersonData implements DagData {

	private LocalDate birthDate;

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	
	
}
