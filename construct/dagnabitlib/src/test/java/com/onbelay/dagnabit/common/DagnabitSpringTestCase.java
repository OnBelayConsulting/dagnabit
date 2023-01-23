package com.onbelay.dagnabit.common;

import com.onbelay.dagnabit.entity.TransactionalSpringTestCase;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@ComponentScan(basePackages = {"com.onbelay.*"})
@EntityScan(basePackages = {"com.onbelay.*"})
@RunWith(SpringRunner.class)
@TestPropertySource( locations="classpath:application-integrationtest.properties")
@SpringBootTest
@Ignore("Do not run *TestCase classes with JUnit")
public class DagnabitSpringTestCase extends TransactionalSpringTestCase {
	
	public void setUp() {
		
	}

}
