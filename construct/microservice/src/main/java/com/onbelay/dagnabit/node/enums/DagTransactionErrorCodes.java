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

public enum DagTransactionErrorCodes {
	
	
	GRAPH_NODE_MISSING_TYPE ("dag.00100", "Missing graph node type"),
	GRAPH_NODE_MISSING_NAME ("dag.00102", "Missing graph node name"),
	GRAPH_NODE_UPDATE_MISSING_ID ("dag.00103", "Missing graph node id. Id is required for update."),
	GRAPH_NODE_SSHOT_MISSING_ENTITY_STATE ("dag.00104", "A graphNodeSnapshot is missing entity state."),
	GRAPH_NODE_SSHOT_INVALID_ENTITY_STATE_FOR_CREATE ("dag.00105", "A graphNodeSnapshot has an invalid entity state ( must be NEW) for requested create."),
	GRAPH_NODE_SSHOT_INVALID_ENTITY_STATE_FOR_UPDATE ("dag.00106", "A graphNodeSnapshot has an invalid entity state (must be MODIFIED OR UNMODIFIED)  for requested update."),
	GRAPH_LINK_MISSING_TYPE ("dag.00200", "Missing graph link type"),
	GRAPH_LINK_MISSING_NAME ("dag.00201", "Missing graph link name"),
	GRAPH_LINK_UPDATE_MISSING_ID ("dag.00203", "Missing graph link id. Id is required for update."),
	GRAPH_LINK_SSHOT_MISSING_ENTITY_STATE ("dag.00204", "A graphLinkSnapshot is missing entity state."),
	GRAPH_LINK_SSHOT_INVALID_ENTITY_STATE_FOR_CREATE ("dag.00205", "A graphLinkSnapshot has an invalid entity state ( must be NEW) for requested create."),
	GRAPH_LINK_SSHOT_INVALID_ENTITY_STATE_FOR_UPDATE ("dag.00206", "A graphLinkSnapshot has an invalid entity state (must be MODIFIED OR UNMODIFIED)  for requested update."),
	GRAPH_LINK_MISSING_FROM_NODE ("dag.00212", "Missing graph link from node"),
	GRAPH_LINK_MISSING_TO_NODE ("dag.00213", "Missing graph link to node")
	;
	private final String code;
	private final String message;

    private static final Map<String,DagTransactionErrorCodes> lookup 
    	= new HashMap<String,DagTransactionErrorCodes>();

    private static final Map<String,DagTransactionErrorCodes> lookupByName 
        = new HashMap<String,DagTransactionErrorCodes>();

    static {
    	for(DagTransactionErrorCodes c : EnumSet.allOf(DagTransactionErrorCodes.class))
         lookup.put(c.code, c);
        for(DagTransactionErrorCodes c : EnumSet.allOf(DagTransactionErrorCodes.class))
         lookupByName.put(c.message, c);
    }
    
	private DagTransactionErrorCodes(String code, String name) {
		this.code = code;
		this.message = name;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public static DagTransactionErrorCodes lookUp(String code) {
		return lookup.get(code);
	}



}
