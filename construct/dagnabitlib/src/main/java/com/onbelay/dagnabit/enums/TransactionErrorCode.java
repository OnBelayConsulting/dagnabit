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
package com.onbelay.dagnabit.enums;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TransactionErrorCode {

    SUCCESS                                       ("0", "Success: transaction was successful"),
    MISSING_GRAPH_NODE                            ("DAG-E0001", "GraphNode not found."),
    MISSING_GRAPH_NODE_CATEGORY                   ("DAG-E0002", "Missing GraphNode category (Mandatory)"),
    MISSING_GRAPH_NODE_NAME                       ("DAG-E0003", "Missing GraphNode name (Mandatory"),
    MISSING_GRAPH_RELATIONSHIP                    ("DAG-E0101", "GraphRelationship not found."),
    MISSING_GRAPH_RELATIONSHIP_TYPE               ("DAG-E0102", "Missing GraphRelationship type (Mandatory)"),
    MISSING_GRAPH_RELATIONSHIP_NAME               ("DAG-E0103", "Missing GraphRelationship name (Mandatory"),

    QUERY_MORE_THAN_ONE_RESULT                    ("DAG-E1000", "Query returned more than one result."),
    ENTITY_DELETE_FAIL                            ("DAG-E1001", "Entity deletion failed."),
    SYSTEM_FAILURE                                ("DAG-9999", "Error: Application error has occurred");

    private String code;
    private String description;

    private static final Map<String, TransactionErrorCode> lookup
            = new HashMap<String, TransactionErrorCode>();

    static {
        for (TransactionErrorCode c : EnumSet.allOf(TransactionErrorCode.class))
            lookup.put(c.code, c);
    }


    private TransactionErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String toString() {
        return code + ":" + description;
    }

    public String getCode() {
        return code;
    }

    public static List<String> getTransactionCodes() {
        ArrayList<String> list = new ArrayList<String>();
        for (TransactionErrorCode c : EnumSet.allOf(TransactionErrorCode.class))
            list.add(c.getCode() + " : " + c.getDescription());
        return list;
    }

    public String getDescription() {
        return description;
    }

    public static TransactionErrorCode lookUp(String code) {
        return lookup.get(code);
    }
}
