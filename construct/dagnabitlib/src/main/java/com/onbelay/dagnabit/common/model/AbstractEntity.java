package com.onbelay.dagnabit.common.model;

import com.onbelay.dagnabit.common.component.ApplicationContextFactory;
import com.onbelay.dagnabit.common.repository.AbstractEntityRepository;
import com.onbelay.dagnabit.common.repository.Repository;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity<T> {

    private Long version;

    @Version
    @Column(name = "OPTIMISTIC_LOCK_NO")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    protected void validate() {

    }

    public void delete() {
        getEntityRepository().delete(this);
    }

    @Transient
    public abstract Integer getEntityId();

    protected void save() {
        validate();
        getEntityRepository().save(this);
    }

    protected void update() {
        validate();
        getEntityRepository().update(this);
    }


    private static AbstractEntityRepository getEntityRepository() {
        return (AbstractEntityRepository) ApplicationContextFactory.getBean("abstractEntityRepository");
    }

}
