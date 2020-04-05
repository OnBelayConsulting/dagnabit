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
package com.onbelay.dagnabit.node.snapshot;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GraphLinkSnapshotCollection extends AbstractJSONCollection<GraphLinkSnapshot> {
	
	public static String itemType = "application/dagnabit.graphLink";
	
	
	public GraphLinkSnapshotCollection(List<GraphLinkSnapshot> snapshots) {
		super(itemType, snapshots);
	}
	
	public GraphLinkSnapshotCollection(List<GraphLinkSnapshot> snapshots, int totalItems) {
		super(itemType, snapshots);
		setTotalItems(totalItems);
	}
	
	public GraphLinkSnapshotCollection(int totalItems) {
		super(itemType);
		setTotalItems(totalItems);
	}
	
	
	
	public GraphLinkSnapshotCollection(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	
}
