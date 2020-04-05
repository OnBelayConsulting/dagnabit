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
package com.onbelay.dagnabit.node.shared;

import javax.persistence.Column;

import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.node.enums.DagTransactionErrorCodes;

public class GraphNodeDetail extends AbstractDetail {
	public static final String DEFAULT_TYPE = "node";

	private String name;
	private String description;
	private boolean isDescriptionNull = false;
	private String category;
	private boolean isCategoryNull = false;
	private String nodeType;
	private boolean isNodeTypeNull = false;
	private String nodeData;
	private boolean isNodeDataNull = false;
	

	/**
	 * Validate required fields
	 */
	public void validate() throws RuntimeDagException {
		if (name == null)
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_NODE_MISSING_NAME.getCode());
		
		if (nodeType == null)
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_NODE_MISSING_TYPE.getCode());
	}
	

	/**
	 * Called to set defaults on a sparse create only
	 */
	public void setDefaults() {
		this.nodeType = DEFAULT_TYPE;
	}

	
	public void copyFrom(GraphNodeDetail copy) {
		if (copy.name != null)
			this.name = copy.name;
		
		if (copy.isDescriptionNull) {
			this.description = null;
		} else {
			if (copy.description != null)
				this.description = copy.description;
		}
		
		if (copy.isCategoryNull) {
			this.category = null;
		} else {
			if (copy.category != null)
				this.category = copy.category;
		}
		
		if (copy.isNodeTypeNull) {
			this.nodeType = null;
		} else {
			if (copy.nodeType != null)
				this.nodeType = copy.nodeType;
		}
		
		if (copy.isNodeDataNull) {
			this.nodeData = null;
		} else {
			if (copy.nodeData != null)
				this.nodeData = copy.nodeData;
		}
		
	}
	
	@Column(name = "NAME_TXT")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "DESCRIPTION_TXT")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		isDescriptionNull = isNull(description);
		this.description = description;
	}

	@Column(name = "NODE_CATEGORY")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		isCategoryNull = isNull(category);
		this.category = category;
	}

	@Column(name = "NODE_TYPE")
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		isNodeTypeNull = isNull(nodeType);
		this.nodeType = nodeType;
	}

	@Column(name = "NODE_DATA")
	public String getNodeData() {
		return nodeData;
	}

	public void setNodeData(String nodeData) {
		isNodeDataNull = isNull(nodeData);
		this.nodeData = nodeData;
	}
	
}
