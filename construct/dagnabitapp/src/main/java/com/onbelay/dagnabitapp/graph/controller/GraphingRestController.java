package com.onbelay.dagnabitapp.graph.controller;

import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.snapshot.GraphNodeSnapshot;
import com.onbelay.dagnabitapp.graph.adapter.GraphingRestAdapter;
import com.onbelay.dagnabitapp.graph.snapshot.*;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphNodeRestAdapter;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphNodeCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/graphModels")public class GraphingRestController {
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private GraphingRestAdapter graphingRestAdapter;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ModelResult> createGraphModel(
            @RequestBody GraphModelSnapshot snapshot,
            BindingResult bindingResult)  {

        if (bindingResult.getErrorCount() > 0) {
            logger.error("Errors on create model POST");
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.toString());
            }

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        ModelResult result;
        try {
            result = graphingRestAdapter.createModel(snapshot);
        } catch (RuntimeDagException p) {
            result = new ModelResult(p.getErrorCode(), p.getParms());
        } catch (RuntimeException bre) {
            result = new ModelResult(bre.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (result.wasSuccessful()) {
            return new ResponseEntity(result, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }

    }


    @RequestMapping(value="/{modelName}/nodes", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ModelResult> addNodes(
            @PathVariable String modelName,
            @RequestBody List<DagNodeSnapshot> snapshots,
            BindingResult bindingResult)  {

        if (bindingResult.getErrorCount() > 0) {
            logger.error("Errors on adding DagNodes POST");
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.toString());
            }

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        ModelResult result;
        try {
            result = graphingRestAdapter.addNodes(
                    modelName,
                    snapshots);
        } catch (RuntimeDagException p) {
            result = new ModelResult(p.getErrorCode(), p.getParms());
        } catch (RuntimeException bre) {
            result = new ModelResult(bre.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (result.wasSuccessful()) {
            return new ResponseEntity(result, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }

    }



    @RequestMapping(value="/{modelName}/relationships", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<ModelResult> addRelationships(
            @PathVariable String modelName,
            @RequestBody List<DagRelationshipSnapshot> snapshots,
            BindingResult bindingResult)  {

        if (bindingResult.getErrorCount() > 0) {
            logger.error("Errors on adding DagRelationships POST");
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.toString());
            }

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        ModelResult result;
        try {
            result = graphingRestAdapter.addRelationships(
                    modelName,
                    snapshots);
        } catch (RuntimeDagException p) {
            result = new ModelResult(p.getErrorCode(), p.getParms());
        } catch (RuntimeException bre) {
            result = new ModelResult(bre.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (result.wasSuccessful()) {
            return new ResponseEntity(result, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }

    }



    @RequestMapping(value="/{modelName}/{relationshipName}/cycles", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<DagNodePathCollection> fetchCycles(
            @PathVariable String modelName,
            @PathVariable String relationshipName,
            @RequestParam(value = "startingNode", required = false) String query)  {

        DagNodePathCollection collection;
        try {
            collection = graphingRestAdapter.fetchCycleReport(
                    modelName,
                    relationshipName);
        } catch (RuntimeDagException p) {
            collection = new DagNodePathCollection(p.getErrorCode(), p.getParms());
        } catch (RuntimeException bre) {
            collection = new DagNodePathCollection(bre.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (collection.wasSuccessful()) {
            return new ResponseEntity(collection, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity(collection, HttpStatus.BAD_REQUEST);
        }

    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<GraphModelCollection> findGraphModels(
            @RequestHeader Map<String, String> headersIn,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "filter", required = false) String filter) {

        GraphModelCollection collection;
        try {
            collection = graphingRestAdapter.findGraphModels(
                    start,
                    limit,
                    filter);
        } catch (RuntimeDagException e) {
            collection = new GraphModelCollection(e.getErrorCode(), e.getParms());
        } catch (RuntimeException e) {
            collection = new GraphModelCollection(e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (collection.wasSuccessful()) {
            return new ResponseEntity<>(collection, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(collection, headers, HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value="/{modelName}/{startingNodeName}/{relationshipName}/descendents",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<DagNodeCollection> findDescendents(
            @RequestHeader Map<String, String> headersIn,
            @PathVariable String modelName,
            @PathVariable String startingNodeName,
            @PathVariable String relationshipName,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        DagNodeCollection collection;
        try {
            collection = graphingRestAdapter.findDescendents(
                    modelName,
                    startingNodeName,
                    relationshipName,
                    start,
                    limit);
        } catch (RuntimeDagException e) {
            collection = new DagNodeCollection(e.getErrorCode(), e.getParms());
        } catch (RuntimeException e) {
            collection = new DagNodeCollection(e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (collection.wasSuccessful()) {
            return new ResponseEntity<>(collection, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(collection, headers, HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value="/{modelName}/rootnodes",method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<DagNodeCollection> findRootNodes(
            @RequestHeader Map<String, String> headersIn,
            @PathVariable String modelName,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        DagNodeCollection collection;
        try {
            collection = graphingRestAdapter.findRootNodes(
                    modelName,
                    start,
                    limit);
        } catch (RuntimeDagException e) {
            collection = new DagNodeCollection(e.getErrorCode(), e.getParms());
        } catch (RuntimeException e) {
            collection = new DagNodeCollection(e.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (collection.wasSuccessful()) {
            return new ResponseEntity<>(collection, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(collection, headers, HttpStatus.BAD_REQUEST);
        }
    }

}
