package com.onbelay.dagnabit.common.snapshot;

import com.onbelay.dagnabit.enums.EntityState;

public abstract class AbstractSnapshot {

    protected  Integer id;

    private EntityState entityState = EntityState.NEW;

    public AbstractSnapshot() {
    }

    public AbstractSnapshot(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EntityState getEntityState() {
        return entityState;
    }

    public void setEntityState(EntityState entityState) {
        this.entityState = entityState;
    }
}
