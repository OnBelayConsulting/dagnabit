package com.onbelay.dagnabit.common.assembler;

import com.onbelay.dagnabit.common.model.AbstractEntity;
import com.onbelay.dagnabit.common.snapshot.AbstractSnapshot;
import com.onbelay.dagnabit.enums.EntityState;

public abstract class AbstractEntityAssembler {

    protected void initialize(
            AbstractSnapshot snapshot,
            AbstractEntity entity) {

        snapshot.setEntityState(EntityState.UNMODIFIED);
        snapshot.setId(entity.getEntityId());
    }

}
