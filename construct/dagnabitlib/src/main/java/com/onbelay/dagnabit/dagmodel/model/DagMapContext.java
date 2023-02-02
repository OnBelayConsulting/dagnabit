package com.onbelay.dagnabit.dagmodel.model;

import java.util.HashMap;
import java.util.Map;

public class DagMapContext implements DagContext {

	private Map<String, Object> contextMap = new HashMap<>();

	public Map<String, Object> getContextMap() {
		return contextMap;
	}

	public void setContextMap(Map<String, Object> contextMap) {
		this.contextMap = contextMap;
	}
	
	
	
}
