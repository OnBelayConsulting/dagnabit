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
package com.onbelay.dagnabit.node.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onbelay.dagnabit.node.model.GraphLink;

@Repository(value = "graphLinkRepository")
public interface GraphLinkRepository extends JpaRepository<GraphLink, Long>{
	
	public static final String BEAN_NAME = "graphLinkRepository";
	
	public List<GraphLink> findByDetailCategory(String category);

	public List<GraphLink> findByDetailLinkType(String linkType);
	
	public List<GraphLink> findByDetailLinkTypeAndDetailCategory(String linkType, String category);
	
}
