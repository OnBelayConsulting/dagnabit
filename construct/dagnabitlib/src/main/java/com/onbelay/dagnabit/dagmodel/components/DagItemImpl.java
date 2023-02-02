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
package com.onbelay.dagnabit.dagmodel.components;

import com.onbelay.dagnabit.dagmodel.model.DagData;

public abstract class DagItemImpl {
	
    protected String name;
    private DagData data;
    private int weight = 0;
    private Integer referenceNo;

	protected DagItemImpl(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String code) {
		this.name = code;
	}
	
    
    public DagData getData() {
		return data;
	}

	public void setData(DagData data) {
		this.data = data;
	}


    public Integer getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(Integer referenceNo) {
        this.referenceNo = referenceNo;
    }

    public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DagNodeImpl other = (DagNodeImpl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
    public String toString() {
        return name;
    }


}
