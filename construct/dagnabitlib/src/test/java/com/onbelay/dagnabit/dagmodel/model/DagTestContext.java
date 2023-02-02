package com.onbelay.dagnabit.dagmodel.model;

import java.util.Stack;

public class DagTestContext implements DagContext {

	private Stack<String> names = new Stack<String>();
	
	public void push(String s) {
		names.push(s);
	}
	
	public Stack<String> getNames() {
		return names;
	}
	
	public String toString() {
		return names.toString();
	}
	
}
