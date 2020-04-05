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
package com.onbelay.dagnabit.persistence;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.onbelay.dagnabit.common.component.ApplicationContextFactory;

import junit.framework.TestCase;

public abstract class TransactionalSpringTestCase extends TestCase {
	
	
	private static Logger logger = LogManager.getFormatterLogger(TransactionalSpringTestCase.class);	
	protected TransactionStatus transactionStatus;
	protected EntityManagerFactory masterEntityMgrFactory;
	private boolean commit = false;
	
	protected void sleep(long millisecs) {
	    try {
            Thread.sleep(millisecs);
        } catch (InterruptedException e) {
        }
	}
	
	/**
	 * Asserts dates are equal by comparing year, month, day, hour, min, second.
	 * @param expected
	 * @param provided
	 */
	protected void assertDateTimeEquals(Date expected, Date provided) {
		Calendar expectedCal = Calendar.getInstance();
		expectedCal.setTime(expected);
		Calendar providedCal = Calendar.getInstance();
		providedCal.setTime(provided);
		assertEquals("Year", expectedCal.get(Calendar.YEAR), providedCal.get(Calendar.YEAR));
		assertEquals("Month", expectedCal.get(Calendar.MONTH), providedCal.get(Calendar.MONTH));
		assertEquals("Day", expectedCal.get(Calendar.DAY_OF_MONTH), providedCal.get(Calendar.DAY_OF_MONTH));
		assertEquals("Hour", expectedCal.get(Calendar.HOUR_OF_DAY), providedCal.get(Calendar.HOUR_OF_DAY));
		assertEquals("Minute", expectedCal.get(Calendar.MINUTE), providedCal.get(Calendar.MINUTE));
		assertEquals("Second", expectedCal.get(Calendar.SECOND), providedCal.get(Calendar.SECOND));
	}
	
    /**
     * Asserts dates are equal by comparing year, month, day, hour, min, second.
     * @param expected
     * @param provided
     */
    protected void assertDateEquals(Date expected, Date provided) {
        Calendar expectedCal = Calendar.getInstance();
        expectedCal.setTime(expected);
        Calendar providedCal = Calendar.getInstance();
        providedCal.setTime(provided);
        assertEquals("year", expectedCal.get(Calendar.YEAR), providedCal.get(Calendar.YEAR));
        assertEquals("month", expectedCal.get(Calendar.MONTH) + 1, providedCal.get(Calendar.MONTH) + 1);
        assertEquals("day", expectedCal.get(Calendar.DAY_OF_MONTH), providedCal.get(Calendar.DAY_OF_MONTH));        
    }
	

	@Before
	public void beforeRun() throws Throwable {
	    initiateSession();
		setUpTransaction();
	}
	
	@After
	public void afterRun() throws Throwable {
		JpaTransactionManager transMgr = (JpaTransactionManager) getBean("transactionManager");
		if (!transactionStatus.isCompleted()) {
			if (commit)
				transMgr.commit(transactionStatus);
			else
				transMgr.rollback(transactionStatus);
		}
		
		terminateSession();
		
	}
	
	public boolean isCommit() {
		return commit;
	}

	public void setCommit(boolean commit) {
		this.commit = commit;
	}

	public int executeBulkQuery(String queryText) {
		EntityManager masterEntityMgr = EntityManagerFactoryUtils.getTransactionalEntityManager(masterEntityMgrFactory);
		Query query = masterEntityMgr.createQuery(queryText);
		return query.executeUpdate();
	}
	
	public void flush() {
		EntityManager masterEntityMgr = EntityManagerFactoryUtils.getTransactionalEntityManager(masterEntityMgrFactory);
		masterEntityMgr.flush();
	}
	
	public void clearCache() {
		EntityManager masterEntityMgr = EntityManagerFactoryUtils.getTransactionalEntityManager(masterEntityMgrFactory);
		masterEntityMgr.clear();
	}
	
	protected Object getBean(String name) {
		return ApplicationContextFactory.getBean(name);
	}
   /**
    * This method will bind an entityManager to the thread if there is no entityManager previously on the thread.
    */
    protected void initiateSession() {
        masterEntityMgrFactory = (EntityManagerFactory) ApplicationContextFactory.getBean("entityManagerFactory");
        EntityManagerHolder emHolder =(EntityManagerHolder) TransactionSynchronizationManager.getResource(masterEntityMgrFactory);
        if (emHolder != null && emHolder.getEntityManager() != null) {
            logger.debug("Session already exists. Not managed by me.");
            return;
        }
        
        logger.trace("Creating new session. Managed by TransactionalSpringTestCase.");
        EntityManager entityManager = masterEntityMgrFactory.createEntityManager();
        TransactionSynchronizationManager.bindResource(masterEntityMgrFactory, new EntityManagerHolder(entityManager));
        
    }
    
	private void setUpTransaction() {
		JpaTransactionManager transMgr = (JpaTransactionManager) getBean("transactionManager");
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName("master");
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		if (masterEntityMgrFactory != transMgr.getEntityManagerFactory())
		    logger.debug("entity mgrs from session and transMgr not the same");
		
		masterEntityMgrFactory = transMgr.getEntityManagerFactory();
		transactionStatus = transMgr.getTransaction(def);
		
        flush();
        
        flush();
        clearCache();
        
	}

    /**
     * this method will successfully terminate a JPA entity manager on the thread.
     */
    protected void terminateSession() {
        EntityManagerHolder emHolder =(EntityManagerHolder) TransactionSynchronizationManager.getResource(masterEntityMgrFactory);
        if (emHolder == null || emHolder.getEntityManager() == null) {
            logger.trace("No session to terminate");
            return;
        }
        
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            logger.trace("Current transaction is active.");
        }

        logger.trace("Terminating session created by TransactionalSpringTestCase");
        try {
            emHolder.getEntityManager().close();
        }
        catch (PersistenceException ex) {
            logger.trace("Could not close JPA EntityManager" + ex.getMessage());
        }
        catch (Throwable ex) {
            logger.trace("Unexpected exception on closing JPA EntityManager" + ex);
        } finally {
            TransactionSynchronizationManager.unbindResource(masterEntityMgrFactory);
        }
    }

    public void commit() {
		JpaTransactionManager transMgr = (JpaTransactionManager) getBean("transactionManager");
		transMgr.commit(transactionStatus);
    }
    public void rollback() {
		JpaTransactionManager transMgr = (JpaTransactionManager) getBean("transactionManager");
		transMgr.rollback(transactionStatus);
    }


}
