package com.myjavapapers.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.myjavapapers.beans.Customers;
import com.myjavapapers.dao.CustomerDao;

public class CustomersServiceTest {

	@Mock
	private CustomerDao customerDao;
	private CustomersService customersService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		customersService = new CustomersService(customerDao);
	}
	
	 @Test
	    public void shouldUpdateCustomersName()
	    {
	        Customers Customers = new Customers( 1, "Phillip" );
	        when( customerDao.fetchCustomers( 1 ) ).thenReturn( Customers );
	        boolean updated = customersService.update( 1, "David" );
	        assertTrue( updated );
	        verify( customerDao ).fetchCustomers( 1 );
	        ArgumentCaptor<Customers> CustomersCaptor = ArgumentCaptor.forClass( Customers.class );
		verify(customerDao).updateCustomers(CustomersCaptor.capture() );
	        Customers updatedCustomers = CustomersCaptor.getValue();
	        assertEquals( "David", updatedCustomers.getCustomerName() );
	        // asserts that during the test, there are no other calls to the mock object.
	        verifyNoMoreInteractions( customerDao );
	    }
	 
	    @Test
	    public void shouldNotUpdateIfCustomersNotFound()
	    {
	        when( customerDao.fetchCustomers( 1 ) ).thenReturn( null );
	        boolean updated = customersService.update( 1, "David" );
	        assertFalse( updated );
	        verify( customerDao ).fetchCustomers( 1 );
	        verifyZeroInteractions( customerDao );
	        verifyNoMoreInteractions( customerDao );
	    }
}
