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
package com.onbelay.dagnabit.graph.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


/**
 * Defines Report direction for reports
 *
 */
public enum ReportDirectionType {
	TO_RELATIONSHIP ("TO", "To Relationships"),
	FROM_RELATIONSHIP ("FROM", "From Relationships");

	private final String code;
    private final String name;

    
    private static final Map<String,ReportDirectionType> lookup 
    = new HashMap<String,ReportDirectionType>();

    private static final Map<String,ReportDirectionType> lookupByName 
    = new HashMap<String,ReportDirectionType>();

static {
    for(ReportDirectionType c : EnumSet.allOf(ReportDirectionType.class))
     lookup.put(c.code, c);
    for(ReportDirectionType c : EnumSet.allOf(ReportDirectionType.class))
        lookupByName.put(c.name, c);
}


    
    
	private ReportDirectionType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
	    return name;
	}
	
    public static ReportDirectionType lookUp(String code) {
        return lookup.get(code);
    }

    public static ReportDirectionType lookUpByCodeOrName(String code) {
        ReportDirectionType fnd = lookUp(code);
        if (fnd != null)
            return fnd;
                    
        return lookupByName.get(code);
    }

    public static ReportDirectionType lookUpByName(String name) {
        return lookupByName.get(name);
    }

}
