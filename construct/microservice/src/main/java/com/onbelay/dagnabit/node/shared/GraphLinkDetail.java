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

public class GraphLinkDetail extends AbstractDetail {
	public static final String DEFAULT_TYPE = "link";

	private String name;
	private String description;
	private boolean isDescriptionNull = false;
	private String category;
	private boolean isCategoryNull = false;
	private String linkType;
	private boolean isLinkTypeNull = false;
	private String linkData;
	private boolean isLinkDataNull = false;
	
	public void copyFrom(GraphLinkDetail copy) {
		
		if (copy.name != null)
			this.name = copy.name;
		
		if (copy.isDescriptionNull) {
			description = null;
		} else {
			if (copy.description != null)
				this.description = copy.description;
		}
		
		if (copy.isCategoryNull) {
			category = null;
		} else {
			if (copy.category != null)
				this.category = copy.category;
		}
		
		if (copy.isLinkTypeNull) {
			linkType = null;
		} else {
			if (copy.linkType != null)
				this.linkType = copy.linkType;
		}
		
		
		if (copy.isLinkDataNull) {
			linkData = null;
		} else {
			if (copy.linkData != null)
				this.linkData = copy.linkData;
		}
		
	}
	
	public void setDefaults() {
		this.linkType = DEFAULT_TYPE;
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
		isDescriptionNull = super.isNull(description);
		this.description = description;
	}

	@Column(name = "LINK_CATEGORY")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		isCategoryNull = isNull(category);
		this.category = category;
	}

	@Column(name = "LINK_TYPE")
	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		isLinkTypeNull = isNull(linkType);
		this.linkType = linkType;
	}

	@Column(name = "LINK_DATA")
	public String getLinkData() {
		return linkData;
	}

	public void setLinkData(String linkData) {
		isLinkDataNull = isNull(linkData);
		this.linkData = linkData;
	}

	public void validate() throws RuntimeDagException {
		if (name == null)
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_MISSING_NAME.getCode());
		
		if (linkType == null)
			throw new RuntimeDagException(DagTransactionErrorCodes.GRAPH_LINK_MISSING_TYPE.getCode());
	}

	/**
	 * The default naming convention if the name is not provided is fromNode.name - link.name -> toNode.name
	 */
	public void createNameIfMissing(String fromNodeName, String toNodeName) {
		if (name == null)
			name = fromNodeName + " -[" + linkType + "]-> " + toNodeName;  
		
	}
	
}
