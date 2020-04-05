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
package com.onbelay.dagnabit.graph.serviceimpl;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.onbelay.dagnabit.graph.model.DagModel;
import com.onbelay.dagnabit.graph.service.DagModelManager;

@Service(value="dagModelManager")
public class DagModelManagerBean implements DagModelManager {
	
	private ConcurrentHashMap<String, DagModel> dagModels = new ConcurrentHashMap<String, DagModel>();
	
	public void addModel(String handle, DagModel model) {
		dagModels.put(handle, model);
	}
	
	public DagModel getModel(String handle) {
		return dagModels.get(handle);
	}
	
	public void forgetModel(String handle) {
		dagModels.remove(handle);
	}

}
