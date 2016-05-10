package org.rpowell.blockchain.services.impl.http;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.rpowell.blockchain.App;
import org.rpowell.blockchain.domain.Owner;
import org.rpowell.blockchain.services.http.IOwnerHttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestOperations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for owner http service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
public class OwnerHttpServiceImplTest {

    @Mock
    private RestOperations template;

    @Autowired
    @InjectMocks
    private IOwnerHttpService httpService;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Ignore
    public void getOwners() throws Exception {
        // Given: Mocked HTTP response
        Mockito.when(template.getForObject(anyString(), any())).thenReturn(new Owner[0]);
        // When
        List<Owner> owners = httpService.getOwners();
        // Then
        verify(template, times(1)).getForObject(anyString(), Mockito.<Class<?>> any());
        assertNotNull(owners);
    }

    @Test
    @Ignore
    public void getOwnersWithData() throws Exception {
        // Given: Mocked HTTP response
        Owner[] owners = new Owner[1];
        Owner owner = new Owner();
        owner.setNick("Test");
        owners[0] = owner;

        owner.setBitcoinaddress("Test");
        Mockito.when(template.getForObject(anyString(), any())).thenReturn(owners);
        // When
        List<Owner> returnedOwners = httpService.getOwners();
        // Then
        verify(template, times(1)).getForObject(anyString(), Mockito.<Class<?>> any());
        assertNotNull(returnedOwners);
    }
}