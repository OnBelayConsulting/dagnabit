package com.onbelay.dagnabit.common.repository;

import com.onbelay.dagnabit.common.model.AbstractEntity;

public interface AbstractEntityRepository {

    public void save(AbstractEntity entity) ;

    public void update(AbstractEntity entity);

    public void delete(AbstractEntity entity);
}
