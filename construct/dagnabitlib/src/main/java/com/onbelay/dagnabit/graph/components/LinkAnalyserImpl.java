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

import com.onbelay.dagnabit.graph.model.DagLinkType;
import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.model.DagNodeType;
import com.onbelay.dagnabit.graph.model.LinkAnalyser;
import com.onbelay.dagnabit.graph.model.LinkAnalysis;
import com.onbelay.dagnabit.graph.model.NavigationResult;
import com.onbelay.dagnabit.graph.model.NodeSearchResult;

public class LinkAnalyserImpl implements LinkAnalyser {

	private DagLinkType linkType;
	private DagNodeType nodeType;
	
	private DagModel model;
	
	
	
	public LinkAnalyserImpl(DagModel model) {
		super();
		this.model = model;
	}

	@Override
	public LinkAnalyser by(DagLinkType linkType) {
		this.linkType = linkType;
		return this;
	}

	@Override
	public LinkAnalyser forNodeType(DagNodeType nodeType) {
		this.nodeType = nodeType;
		return this;
	}

	@Override
	public LinkAnalysis result() {
    	LinkAnalysisImpl analysisResult = new LinkAnalysisImpl();
    	
    	if (linkType == null) {
	    	for (DagLinkType linkType : model.getLinkTypeMap().values()) {
	        	DagLinkRouteFinder finder = new DagLinkRouteFinder(
	        			model,
	        			nodeType,
	        			linkType);
	        	NavigationResult result = finder.discoverToRelationships();
	        	
	        	for (NodeSearchResult nodeSearchResult : result.getNodeSearchResults().values()) {
	        		analysisResult.addCycleByLinkType(
	        				nodeSearchResult.getDagLinkType(),
	        				nodeSearchResult.getCycles());
	        	}
	        	
	    	}
    	} else {
        	DagLinkRouteFinder finder = new DagLinkRouteFinder(
        			model,
        			nodeType,
        			linkType);
        	NavigationResult result = finder.discoverToRelationships();
        	
        	for (NodeSearchResult nodeSearchResult : result.getNodeSearchResults().values()) {
        		analysisResult.addCycleByLinkType(
        				nodeSearchResult.getDagLinkType(),
        				nodeSearchResult.getCycles());
        	}
    	}
    	return analysisResult;
    	
	}

}
