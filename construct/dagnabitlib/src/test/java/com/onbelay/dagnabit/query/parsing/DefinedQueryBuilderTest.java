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
package com.onbelay.dagnabit.query.parsing;

import com.onbelay.dagnabit.query.snapshot.DefinedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;


import junit.framework.TestCase;

public class DefinedQueryBuilderTest extends TestCase {
	private static final Logger logger = LogManager.getLogger(DefinedQueryBuilderTest.class);
	
	@Test
	public void testSimpleWhereExpression() {
		
		String expressionText = "WHERE dealNo = 2";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		logger.error(query.toString());
		
		assertEquals("FROM Deal WHERE dealNo = 2", query.toString());
	}

	
	@Test
	public void testSimpleWhereAndOrderExpression() {
		
		String expressionText = "WHERE dealNo = 2 ORDER BY name ASC";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		logger.debug(query.toString());
		
		assertEquals("FROM Deal WHERE dealNo = 2 ORDER BY name ASC", query.toString());
	}
	
	@Test
	public void testSimpleOrderExpression() {
		
		String expressionText = "ORDER BY name";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		logger.debug(query.toString());
		
		assertEquals("FROM Deal ORDER BY name ASC", query.toString());
	}

	
	@Test
	public void testWhereExpressionWithNull() {
		
		String expressionText = "WHERE dealNo IS NULL";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		assertEquals("FROM Deal WHERE dealNo IS NULL", query.toString());
		
	}
	
	@Test
	public void testSimpleWhereExpressionWithBrackets() {
		
		String expressionText = "WHERE (dealNo = 2)";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		assertEquals("FROM Deal WHERE (dealNo = 2)", query.toString());
	}
	
	@Test
	public void testSimpleWhereExpressionWithMultipleBrackets() {
		
		String expressionText = "WHERE ((dealNo = 2))";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		assertEquals("FROM Deal WHERE ((dealNo = 2))", query.toString());
	}
	
	@Test
	public void testSimpleWhereExpressionAndOrderByWithBrackets() {
		
		String expressionText = "WHERE (dealNo = 2) ORDER BY name";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		assertEquals("FROM Deal WHERE (dealNo = 2) ORDER BY name ASC", query.toString());
	}
	
	@Test
	public void testAndWhereExpression() {
		
		String expressionText = "WHERE dealNo = 2 AND ticketNo = 'myticket'";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		assertEquals("FROM Deal WHERE dealNo = 2 AND ticketNo = 'myticket'", query.toString());
	}
	@Test
	public void testAndWhereExpressionWithBrackets() {
		
		String expressionText = "WHERE (dealNo = 2) AND (ticketNo = 'myticket')";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		assertEquals("FROM Deal WHERE (dealNo = 2) AND (ticketNo = 'myticket')", query.toString());
	}
	
	@Test
	public void testWhereExpressionWithBoolean() {
		
		String expressionText = "WHERE dealNo = true";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		logger.error(query.toString());
		assertEquals("FROM Deal WHERE dealNo = true", query.toString());
	}
	
	@Test
	public void testWhereExpressionWithBooleanCaps() {
		
		String expressionText = "WHERE dealNo = TRUE";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		logger.error(query.toString());
		assertEquals("FROM Deal WHERE dealNo = true", query.toString());
	}
	
	@Test
	public void testWhereExpressionWithInNumbers() {
		
		String expressionText = "WHERE dealNo IN (1, 3, 4)";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		logger.error(query.toString());
		assertEquals("FROM Deal WHERE dealNo IN (1,3,4)", query.toString());
	}
	
	@Test
	public void testWhereExpressionWithInStrings() {
		
		String expressionText = "WHERE dealNo IN ('me', 'my', 'them')";
		
		DefinedQueryBuilder builder = new DefinedQueryBuilder("Deal", expressionText);
		
		DefinedQuery query = builder.build();
		
		logger.error(query.toString());
		assertEquals("FROM Deal WHERE dealNo IN ('me','my','them')", query.toString());
	}
	
}
