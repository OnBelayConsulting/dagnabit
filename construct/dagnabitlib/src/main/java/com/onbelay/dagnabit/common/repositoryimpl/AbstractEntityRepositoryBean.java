package com.onbelay.dagnabit.common.repositoryimpl;

import com.onbelay.dagnabit.common.model.AbstractEntity;
import com.onbelay.dagnabit.common.repository.AbstractEntityRepository;
import com.onbelay.dagnabit.common.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "abstractEntityRepository")
@Transactional
public class AbstractEntityRepositoryBean extends BaseRepository<AbstractEntity> implements AbstractEntityRepository {

    @Override
    public void update(AbstractEntity entity) {

    }


}
