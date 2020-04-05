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
package com.onbelay.dagnabit.graph.snapshot;

public class DagLinkSnapshot {

	private String name;
	private String linkType;
	
	private String fromNodeName;
	private String toNodeName;
	
	public DagLinkSnapshot() {
		
	}
	
	
	
	public DagLinkSnapshot(String name, String linkType, String fromNodeName, String toNodeName) {
		super();
		this.name = name;
		this.linkType = linkType;
		this.fromNodeName = fromNodeName;
		this.toNodeName = toNodeName;
	}



	public DagLinkSnapshot(String fromNodeName, String toNodeName) {
		super();
		this.fromNodeName = fromNodeName;
		this.toNodeName = toNodeName;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public String getLinkType() {
		return linkType;
	}



	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}



	public String getFromNodeName() {
		return fromNodeName;
	}
	public void setFromNodeName(String fromNodeName) {
		this.fromNodeName = fromNodeName;
	}
	public String getToNodeName() {
		return toNodeName;
	}
	public void setToNodeName(String toNodeName) {
		this.toNodeName = toNodeName;
	}
	
	
	
}
