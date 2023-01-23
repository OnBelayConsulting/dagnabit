package com.onbelay.dagnabit.graphnode.exception;

import com.onbelay.dagnabit.common.exception.RuntimeDagException;

public class GraphNodeException extends RuntimeDagException {
    public GraphNodeException(String errorCode) {
        super(errorCode);
    }

    public GraphNodeException(String errorCode, String parm) {
        super(errorCode, parm);
    }

    public GraphNodeException(String errorCode, String parm, Throwable t) {
        super(errorCode, parm, t);
    }

    public GraphNodeException(String errorCode, Throwable t) {
        super(errorCode, t);
    }
}
