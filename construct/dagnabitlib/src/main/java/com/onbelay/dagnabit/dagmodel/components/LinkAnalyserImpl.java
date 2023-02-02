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

import com.onbelay.dagnabit.dagmodel.model.DagRelationshipType;
import com.onbelay.dagnabit.dagmodel.model.DagNodeCategory;
import com.onbelay.dagnabit.dagmodel.model.LinkAnalyser;
import com.onbelay.dagnabit.dagmodel.model.LinkAnalysis;
import com.onbelay.dagnabit.dagmodel.model.NavigationResult;
import com.onbelay.dagnabit.dagmodel.model.NodeSearchResult;

public class LinkAnalyserImpl implements LinkAnalyser {

	private DagRelationshipType relationshipType;
	private DagNodeCategory nodeCategory;
	
	private DagModelImpl model;
	
	
	
	public LinkAnalyserImpl(DagModelImpl model) {
		super();
		this.model = model;
	}

	@Override
	public LinkAnalyser by(DagRelationshipType relationshipType) {
		this.relationshipType = relationshipType;
		return this;
	}

	@Override
	public LinkAnalyser forNodeType(DagNodeCategory nodeCategory) {
		this.nodeCategory = nodeCategory;
		return this;
	}

	@Override
	public LinkAnalysis result() {
    	LinkAnalysisImpl analysisResult = new LinkAnalysisImpl();
    	
    	if (relationshipType == null) {
	    	for (DagRelationshipType linkType : model.getLinkTypeMap().values()) {
	        	DagLinkRouteFinder finder = new DagLinkRouteFinder(
	        			model,
	        			linkType,
						nodeCategory);
	        	NavigationResult result = traverseFromRoot(finder);
	        	
	        	for (NodeSearchResult nodeSearchResult : result.getNodeSearchResults().values()) {
	        		analysisResult.addCycleByLinkType(
	        				nodeSearchResult.getRelationshipType(),
	        				nodeSearchResult.getCycles());
	        	}
	        	
	    	}
    	} else {
        	DagLinkRouteFinder finder = new DagLinkRouteFinder(
        			model,
					relationshipType,
					nodeCategory);
        	NavigationResult result = traverseFromRoot(finder);
        	
        	for (NodeSearchResult nodeSearchResult : result.getNodeSearchResults().values()) {
        		analysisResult.addCycleByLinkType(
        				nodeSearchResult.getRelationshipType(),
        				nodeSearchResult.getCycles());
        	}
    	}
    	return analysisResult;
    	
	}
	
	private NavigationResult traverseFromRoot(DagLinkRouteFinder finder) {
		
		NavigationResult navigationResult = new NavigationResult();
		
		for (DagNodeImpl node : model.getNodeMap().values()) {
			navigationResult.add(
					node, 
					finder.discoverFromRelationships(node));
		}
		return navigationResult;
	}

}
