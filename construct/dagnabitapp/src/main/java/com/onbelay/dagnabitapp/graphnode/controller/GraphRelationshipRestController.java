package com.onbelay.dagnabitapp.graphnode.controller;

import com.onbelay.dagnabit.common.exception.RuntimeDagException;
import com.onbelay.dagnabit.common.snapshot.TransactionResult;
import com.onbelay.dagnabit.graphnode.snapshot.GraphRelationshipSnapshot;
import com.onbelay.dagnabitapp.graphnode.adapter.GraphRelationshipRestAdapter;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphNodeCollection;
import com.onbelay.dagnabitapp.graphnode.snapshot.GraphRelationshipCollection;
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
@RequestMapping("/api/relationships")public class GraphRelationshipRestController {
    private static final Logger logger = LogManager.getLogger();
    
    @Autowired
    private GraphRelationshipRestAdapter graphRelationshipRestAdapter;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ResponseEntity<TransactionResult> saveGraphRelationship(
            @RequestBody GraphRelationshipSnapshot snapshot,
            BindingResult bindingResult)  {

        if (bindingResult.getErrorCount() > 0) {
            logger.error("Errors on create/update GraphRelationship POST");
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.toString());
            }

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        TransactionResult result;
        try {
            result = graphRelationshipRestAdapter.saveGraphRelationship(snapshot);
        } catch (RuntimeDagException p) {
            result = new TransactionResult(p.getErrorCode(), p.getParms());
        } catch (RuntimeException bre) {
            result = new TransactionResult(bre.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (result.wasSuccessful()) {
            return new ResponseEntity(result, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }

    }


    @RequestMapping(method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public ResponseEntity<TransactionResult> saveGraphRelationships(
            @RequestBody List<GraphRelationshipSnapshot> snapshots,
            BindingResult bindingResult)  {

        if (bindingResult.getErrorCount() > 0) {
            logger.error("Errors on create/update GraphRelationship PUT");
            for (ObjectError error : bindingResult.getAllErrors()) {
                logger.error(error.toString());
            }

            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        TransactionResult result;
        try {
            result = graphRelationshipRestAdapter.saveGraphRelationships(snapshots);
        } catch (RuntimeDagException p) {
            result = new TransactionResult(p.getErrorCode(), p.getParms());
        } catch (RuntimeException bre) {
            result = new TransactionResult(bre.getMessage());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");

        if (result.wasSuccessful()) {
            return new ResponseEntity(result, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }

    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<GraphRelationshipCollection> findGraphRelationships(
            @RequestHeader Map<String, String> headersIn,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "query", required = false) String query) {

        GraphRelationshipCollection collection;
        try {
            collection = graphRelationshipRestAdapter.findGraphRelationships(
                    start,
                    limit,
                    query);
        } catch (RuntimeDagException e) {
            collection = new GraphRelationshipCollection(e.getErrorCode(), e.getParms());
        } catch (RuntimeException e) {
            collection = new GraphRelationshipCollection(e.getMessage());
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
