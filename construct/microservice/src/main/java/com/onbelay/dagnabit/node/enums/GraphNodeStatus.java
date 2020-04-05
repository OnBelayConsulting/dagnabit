/*
 Copyright 2019, OnBelay Consulting Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  
 */
package com.onbelay.dagnabit.node.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines current status of node.
 * 
 * @author canmxf
 *
 */
public enum GraphNodeStatus {
	ENABLED ("E", "ENABLED"),
	SUSPENDED ("S", "SUSPENDED");

	private final String code;
	private final String name;

    private static final Map<String,GraphNodeStatus> lookup 
    	= new HashMap<String,GraphNodeStatus>();

    private static final Map<String,GraphNodeStatus> lookupByName 
        = new HashMap<String,GraphNodeStatus>();

    static {
    	for(GraphNodeStatus c : EnumSet.allOf(GraphNodeStatus.class))
         lookup.put(c.code, c);
        for(GraphNodeStatus c : EnumSet.allOf(GraphNodeStatus.class))
         lookupByName.put(c.name, c);
    }
    
	private GraphNodeStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
    public String getName() {
        return name;
    }
    
	public static GraphNodeStatus lookUp(String code) {
		return lookup.get(code);
	}

    public static GraphNodeStatus lookUpByName(String name) {
        return lookupByName.get(name);
    }
    
    public static GraphNodeStatus lookUpByNameOrCode(String name) {
		GraphNodeStatus rule = GraphNodeStatus.lookUp(name);
		if (rule != null) {
			return rule; 
		} else {
			return lookupByName.get(name);
		}
    }

}
