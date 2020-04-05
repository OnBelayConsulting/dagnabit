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
package com.onbelay.dagnabit.graph.service;

import java.util.List;

import com.onbelay.dagnabit.graph.snapshot.AnalysisSnapshot;
import com.onbelay.dagnabit.graph.snapshot.DagNavigationCriteria;
import com.onbelay.dagnabit.graph.snapshot.DagNodeSnapshot;
import com.onbelay.dagnabit.graph.snapshot.DagPathSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;

public interface DagService {

	
	/**
	 * Create a DagModel from all the nodes and links in the database
	 * @return a handle subsequently used to access the DagModel for operations.
	 */
	public String loadModel();
	
	/**
	 * Create a DagModel from all the nodes and links that are assigned to the given category.
	 * @param category
	 * @return a handle subsequently used to access the DagModel for operations.
	 */
	public String loadModel(String category);
	
	/**
	 * Create a DagModel  from the list of nodes and links.
	 * @param nodes
	 * @param linkSummaries
	 * @return a handle subsequently used to access the DagModel for operations.
	 */
	public String createModel(
			List<GraphNodeSnapshot> nodes,
			List<GraphLinkSnapshot> linkSummaries);
	
	
	/**
	 * forget this model and delete from memory
	 * @param handle - a handle to an existing DagModel
	 */
	public void forgetModel(String handle);
	
	/**
	 * Fetch all nodes that have from links (from this node to another node) and no to links (no node link points to this node)
	 * @param handle - a handle to an existing DagModel
	 * @return a list of nodes
	 */
	public List<DagNodeSnapshot> fetchRootNodes(String handle);

	
	/**
	 * Fetch all the nodes that have only 'To' links and no from links. Opposite of Root nodes.
	 * @param handle - a handle to an existing DagModel
	 * @return
	 */
	public List<DagNodeSnapshot> fetchLeafNodes(String handle);


	/**
	 * Fetch all nodes that have no from or to links. In other words the node is not attached to the graph.
	 * @param handle - a handle to an existing DagModel
	 * @return
	 */
	public List<DagNodeSnapshot> fetchSolitaryNodes(String handle);
	
	
	/**
	 * Analyze this model  for cycles using all links available. 
	 * If there are cycles then the cycles are returned as paths.
	 * @param handle - a handle to an existing DagModel
	 * @return an analysis
	 */
	public AnalysisSnapshot analyse(String handle);

	

	/**
	 * Analyze this model for cycles using the provided linkType
	 * @param handle - a handle to an existing DagModel
	 * @param linkType - linkType to navigate. Other links will be ignored
	 * @return an analysis
	 */
	public AnalysisSnapshot analyse(String handle, String linkType);

	/**
	 * Return zero to many paths (node -link> node -link> node ... ) based on the supplied critera.
	 * @param handle - a handle to an existing DagModel
	 * @param criteria
	 * @return a list of zero to many paths
	 */
	public List<DagPathSnapshot> navigate(String handle, DagNavigationCriteria criteria);

}
