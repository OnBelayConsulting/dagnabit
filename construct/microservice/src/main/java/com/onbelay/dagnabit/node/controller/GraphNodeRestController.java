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
package com.onbelay.dagnabit.node.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.node.enums.EntityState;
import com.onbelay.dagnabit.node.services.GraphLinkService;
import com.onbelay.dagnabit.node.services.GraphNodeService;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphLinkSnapshotCollection;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabit.node.snapshot.GraphNodeSnapshotCollection;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * This REST controller is responsible for maintaining the node database elements: GraphNode and GraphLink.
 *  
 * @author lefeu
 *
 */
@org.springframework.web.bind.annotation.RestController
@Api()
public class GraphNodeRestController {
	private static final Logger logger = LogManager.getLogger();
	
	@Autowired
	private GraphNodeService graphNodeService;
	
	@Autowired
	private GraphLinkService graphLinkService;
	
	@ApiOperation(value="creates a new graphLink")
	@PostMapping(value="/api/graphLinks", produces="application/json", consumes="application/json" )
	public ResponseEntity<GraphLinkSnapshot> createGraphLink(
			@RequestHeader Map<String, String> headers,
			@RequestBody GraphLinkSnapshot graphLinkShapshot, 
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach( e -> {
				logger.error("Error on ", e.toString()); 
			});
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		
		Long key;
		try {
			key = graphLinkService.createGraphLink(graphLinkShapshot);
		} catch (RuntimeDagException r) {
			logger.error("Create/update failed ", r.getErrorMessage(), r);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, r.getErrorCode());
		} catch (Throwable r) {
			logger.error("Unknown error ", r.getMessage(), r);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, r.getMessage());
		}
		
		GraphLinkSnapshot updatedNode = graphLinkService.load(key);
		
		
		
		updatedNode.add(
				ControllerLinkBuilder.linkTo(
						ControllerLinkBuilder.methodOn(
								GraphNodeRestController.class).getGraphNode(key)).withSelfRel()

			);
		
		return new ResponseEntity<GraphLinkSnapshot>( updatedNode, HttpStatus.OK);
	}
	
	@ApiOperation(value="creates a new graphNode")
	@PostMapping(value="/api/graphNodes", produces="application/json", consumes="application/json" )
	public ResponseEntity<GraphNodeSnapshot> createGraphNode(
			@RequestHeader Map<String, String> headers,
			@RequestBody GraphNodeSnapshot graphNode, 
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach( e -> {
				logger.error("Error on ", e.toString()); 
			});
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
		
		Long key = graphNodeService.createGraphNode(graphNode);
		
		GraphNodeSnapshot updatedNode = graphNodeService.loadGraphNode(key);
		
		
		updatedNode.add(
				ControllerLinkBuilder.linkTo(
						ControllerLinkBuilder.methodOn(
								GraphNodeRestController.class).getGraphNode(key)).withSelfRel()

			);
		
		return new ResponseEntity<GraphNodeSnapshot>( updatedNode, HttpStatus.OK);
	}
	
	@ApiOperation(value="creates and/or updates one or more graphNodes")
	@PutMapping(value="/api/graphNodes", produces="application/json", consumes="application/json" )
	public ResponseEntity<List<Long>> createOrUpdateGraphNodes(
			@RequestHeader Map<String, String> headers,
			@RequestBody List<GraphNodeSnapshot> graphNodes, 
			BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach( e -> {
				logger.error("Error on ", e.toString()); 
			});
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}

		
		List<Long> keys = graphNodeService.saveOrUpdateGraphNodes(graphNodes);
		
		
		return new ResponseEntity<List<Long>>( keys, HttpStatus.OK);
	}
	
	@ApiOperation(value="sparse updates an existing graphNode")
	@PatchMapping(value= "/api/graphNodes/{id}", produces="application/json", consumes="application/json" )
	public ResponseEntity<GraphNodeSnapshot> updateGraphNode(
			@PathVariable Long id,
			@RequestHeader Map<String, String> headers,
			@RequestBody GraphNodeSnapshot graphNode, 
			BindingResult bindingResult) {
		
		
		graphNode.setEntityState(EntityState.MODIFIED);
		graphNode.setEntityId(id);
		
		graphNodeService.updateGraphNode(graphNode);
		
		GraphNodeSnapshot updatedNode = graphNodeService.loadGraphNode(graphNode.getEntityId());

		updatedNode.add(
				ControllerLinkBuilder.linkTo(
						ControllerLinkBuilder.methodOn(
								GraphNodeRestController.class).getGraphNode(id)).withSelfRel()

			);
		
		return new ResponseEntity<GraphNodeSnapshot>( updatedNode, HttpStatus.OK);
	}
	
	@ApiOperation(value="sparse updates an existing graphLink")
	@PatchMapping(value= "/api/graphLinks/{id}", produces="application/json", consumes="application/json" )
	public ResponseEntity<GraphLinkSnapshot> updateGraphLink(
			@PathVariable Long id,
			@RequestHeader Map<String, String> headers,
			@RequestBody GraphLinkSnapshot graphLink, 
			BindingResult bindingResult) {
		
		graphLink.setEntityState(EntityState.MODIFIED);
		graphLink.setEntityId(id);
		
		graphLinkService.updateGraphLink(graphLink);
		
		GraphLinkSnapshot updatedLink = graphLinkService.load(id);

		updatedLink.add(
				ControllerLinkBuilder.linkTo(
						ControllerLinkBuilder.methodOn(
								GraphNodeRestController.class).getGraphNode(id)).withSelfRel()

			);
		
		return new ResponseEntity<GraphLinkSnapshot>( updatedLink, HttpStatus.OK);
	}
	
	
	@ApiOperation(value="find graphNodes")
	@RequestMapping(value="/api/graphNodes", method=RequestMethod.GET )
	public ResponseEntity<GraphNodeSnapshotCollection> getGraphNodes(
			@RequestHeader Map<String, String> headers,
			@RequestParam(value = "category", required = false) String categoryName,
			@RequestParam(value = "type", required = false) String nodeType) {
		
		GraphNodeSnapshotCollection collection;
		try {
			if (categoryName == null && nodeType == null)
				collection = graphNodeService.findAll();
			else if (categoryName != null && nodeType == null )
				collection = graphNodeService.findByCategory(categoryName);
			else if (categoryName == null && nodeType != null ) 
				collection = graphNodeService.findByNodeType(nodeType);
			else
				collection = graphNodeService.findByNodeTypeAndCategory(nodeType, categoryName);
		} catch (RuntimeDagException r) {
			return new ResponseEntity<GraphNodeSnapshotCollection>(
					new GraphNodeSnapshotCollection(r.getErrorCode(), r.getErrorMessage()),
					HttpStatus.BAD_REQUEST);
		} catch (Throwable r) {
			return new ResponseEntity<GraphNodeSnapshotCollection>(
					new GraphNodeSnapshotCollection("Q001", r.getMessage()),
					HttpStatus.BAD_REQUEST);
		}

	
		for (GraphNodeSnapshot value : collection.getSnapshots()) { 
			
			value.add(
					ControllerLinkBuilder.linkTo(
							ControllerLinkBuilder.methodOn(
									GraphNodeRestController.class).getGraphLink(value.getEntityId())).withSelfRel()

				);
		}
		
		
		return new ResponseEntity<GraphNodeSnapshotCollection>(collection, HttpStatus.OK);
	}
	
	
	@ApiOperation(value="find graphLinks")
	@RequestMapping(value="/api/graphLinks", method=RequestMethod.GET )
	public ResponseEntity<GraphLinkSnapshotCollection> getGraphLinks(
			@RequestHeader Map<String, String> headers,
			@RequestParam(value = "category", required = false) String categoryName,
			@RequestParam(value = "type", required = false) String linkType) {
		
		
		GraphLinkSnapshotCollection collection;
		try {
			if (categoryName == null && linkType == null)
				collection = graphLinkService.findAll();
			else if (categoryName != null && linkType == null )
				collection = graphLinkService.findByCategory(categoryName);
			else if (categoryName == null && linkType != null ) 
				collection = graphLinkService.findByLinkType(linkType);
			else
				collection = graphLinkService.findByLinkTypeAndCategory(linkType, categoryName);
		} catch (RuntimeDagException r) {
			return new ResponseEntity<GraphLinkSnapshotCollection>(
					new GraphLinkSnapshotCollection(r.getErrorCode(), r.getErrorMessage()),
					HttpStatus.BAD_REQUEST);
		} catch (Throwable r) {
			return new ResponseEntity<GraphLinkSnapshotCollection>(
					new GraphLinkSnapshotCollection("Q001", r.getMessage()),
					HttpStatus.BAD_REQUEST);
		}

	
		for (GraphLinkSnapshot value : collection.getSnapshots()) { 
			
			value.add(
					ControllerLinkBuilder.linkTo(
							ControllerLinkBuilder.methodOn(
									GraphNodeRestController.class).getGraphLink(value.getEntityId())).withSelfRel()

				);
		}
		
		
		return new ResponseEntity<GraphLinkSnapshotCollection>(collection, HttpStatus.OK);
		
	}
	

	@ApiOperation(value="get an existing graphNode")
	@RequestMapping(value="/api/graphNodes/{id}", method=RequestMethod.GET)
	public ResponseEntity<GraphNodeSnapshot> getGraphNode(@PathVariable Long id) {
		
		GraphNodeSnapshot graphNode = graphNodeService.loadGraphNode(new Long(id));
		
		
		if (graphNode == null)
			return null;
		
		graphNode.add(
				ControllerLinkBuilder.linkTo(
						ControllerLinkBuilder.methodOn(
								GraphNodeRestController.class).getGraphNode(id)).withSelfRel());
		
		HttpHeaders  headers = new HttpHeaders();
		headers.add(HttpHeaders.ETAG, "" + id);
		headers.add(HttpHeaders.LAST_MODIFIED, "2016-01-01");
		
		return new ResponseEntity<GraphNodeSnapshot>(graphNode, headers, HttpStatus.OK);
	}
	

	@ApiOperation(value="get an existing graphLink")
	@RequestMapping(value="/api/graphLinks/{id}", method=RequestMethod.GET)
	public ResponseEntity<GraphLinkSnapshot> getGraphLink(@PathVariable Long id) {
		
		GraphLinkSnapshot graphLink = graphLinkService.load(new Long(id));
		
		
		if (graphLink == null)
			return null;
		
		graphLink.add(
				ControllerLinkBuilder.linkTo(
						ControllerLinkBuilder.methodOn(
								GraphNodeRestController.class).getGraphLink(id)).withSelfRel());
		
		HttpHeaders  headers = new HttpHeaders();
		headers.add(HttpHeaders.ETAG, "" + id);
		headers.add(HttpHeaders.LAST_MODIFIED, "2016-01-01");
		
		return new ResponseEntity<GraphLinkSnapshot>(graphLink, headers, HttpStatus.OK);
	}
	

}
