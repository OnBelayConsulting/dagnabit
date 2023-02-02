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
package com.onbelay.dagnabit.dagmodel.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


/**
 * Defines whether the graph should be traversed depth first or breadth first.
 *
 */
public enum TraversalDirectionType {
	TRAVERSE_DEPTH_FIRST ("DEPTH_FIRST", "Depth-First"),
	TRAVERSE_BREADTH_FIRST ("BREADTH_FIRST", "Breadth-first");

	private final String code;
    private final String name;

    
    private static final Map<String,TraversalDirectionType> lookup 
    = new HashMap<String,TraversalDirectionType>();

    private static final Map<String,TraversalDirectionType> lookupByName 
    = new HashMap<String,TraversalDirectionType>();

static {
    for(TraversalDirectionType c : EnumSet.allOf(TraversalDirectionType.class))
     lookup.put(c.code, c);
    for(TraversalDirectionType c : EnumSet.allOf(TraversalDirectionType.class))
        lookupByName.put(c.name, c);
}


    
    
	private TraversalDirectionType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
	    return name;
	}
	
    public static TraversalDirectionType lookUp(String code) {
        return lookup.get(code);
    }

    public static TraversalDirectionType lookUpByCodeOrName(String code) {
        TraversalDirectionType fnd = lookUp(code);
        if (fnd != null)
            return fnd;
                    
        return lookupByName.get(code);
    }

    public static TraversalDirectionType lookUpByName(String name) {
        return lookupByName.get(name);
    }

}
