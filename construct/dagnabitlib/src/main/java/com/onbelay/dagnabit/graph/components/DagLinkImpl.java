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
package com.onbelay.dagnabit.graph.components;

import com.onbelay.dagnabit.graph.model.DagLink;
import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagNode;

public class DagLinkImpl extends DagItemImpl implements DagLink {
	public static final String DEFAULT_TYPE = "link";

	private DagLinkType type;
	private DagNodeImpl fromNode;
	private DagNodeImpl toNode;

	public DagLinkImpl(
			DagNodeImpl fromNode, 
			DagLinkType type, 
			DagNodeImpl toNode) {
		
		super(
				fromNode.getName() + ":" + type.getName() + ":>" + toNode.getName()
				);
		
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.type = type;
	}

	public DagLinkType getDagLinkType() {
		return type;
	}


	public DagNode getFromNode() {
		return fromNode;
	}


	public DagNode getToNode() {
		return toNode;
	}

}
