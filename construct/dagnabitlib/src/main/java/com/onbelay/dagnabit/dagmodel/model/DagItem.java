package com.onbelay.dagnabit.dagmodel.model;

import java.math.BigDecimal;

public interface DagItem {

    public String getName();

    public void setData(DagData data);

    public DagData getData();

    public void setWeight(BigDecimal weight);

    public BigDecimal getWeight();

    public Integer getReferenceNo();

    public void setReferenceNo(Integer referenceNo);

}
