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
package com.onbelay.dagnabit.node.services;

import java.util.List;

import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshotCollection;

public interface GraphLinkService {
	public static final String BEAN_NAME = "graphLinkService";

	
	public Long createGraphLink(GraphLinkSnapshot snapshot);
	
	public GraphLinkSnapshot load(Long entityId);
	
	public GraphLinkSnapshotCollection findAll();
	
	public GraphLinkSnapshotCollection findByCategory(String categoryName);

	public GraphLinkSnapshotCollection findByLinkType(String linkType);

	public GraphLinkSnapshotCollection findByLinkTypeAndCategory(String linkType, String categoryName);
	
	public List<Long> saveOrUpdateGraphLinks(List<GraphLinkSnapshot> snapshots);
	
	public Long updateGraphLink(GraphLinkSnapshot snapshot);
	
	
	
}
