# dagnabit
A package to explore Directed Acyclic Graphs 

Dagnabit supports a fluent style of navigating a graph.

For example:
```
List<DagNode> johnsDescendants = model
		.navigate()
		.from(model.findNode(“John Smith”)
		.by(model.getLinkType(“isParentOf”)
		.descendants();
```  
Will perform a depth-first search of all the descendants based on navigating the "isParentOf" relationship.

Dagnabit support a number of traversing strategies and can navigate both the two and from directions. 

Complete documentation may be found at :http://onbelayconsulting.ca/dagnabit-a-library-for-traversing-graphs-easily/


