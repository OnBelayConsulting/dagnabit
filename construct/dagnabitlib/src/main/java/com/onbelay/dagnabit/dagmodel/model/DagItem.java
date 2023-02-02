package com.onbelay.dagnabit.dagmodel.model;

public interface DagItem {

    public String getName();

    public void setData(DagData data);

    public DagData getData();

    public void setWeight(int weight);

    public int getWeight();

    public Integer getReferenceNo();

    public void setReferenceNo(Integer referenceNo);

}
