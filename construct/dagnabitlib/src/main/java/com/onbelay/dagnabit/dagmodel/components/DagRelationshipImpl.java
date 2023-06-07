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
package com.onbelay.dagnabit.dagmodel.components;

import com.onbelay.dagnabit.dagmodel.model.DagNode;
import com.onbelay.dagnabit.dagmodel.model.DagRelationship;
import com.onbelay.dagnabit.dagmodel.model.DagRelationshipType;

public class DagRelationshipImpl extends DagItemImpl implements DagRelationship {
	public static final String DEFAULT_TYPE = "link";

	private DagRelationshipType type;
	private DagNodeImpl fromNode;
	private DagNodeImpl toNode;

	public DagRelationshipImpl(
			DagNodeImpl fromNode, 
			DagRelationshipType type,
			DagNodeImpl toNode) {
		
		super(
				fromNode.getName() + ":" + type.getName() + ":>" + toNode.getName()
				);
		
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.type = type;
	}

	public DagRelationshipType getRelationshipType() {
		return type;
	}


	public DagNode getFromNode() {
		return fromNode;
	}


	public DagNode getToNode() {
		return toNode;
	}

}
