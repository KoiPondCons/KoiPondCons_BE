package com.koiteampro.koipondcons;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.models.request.ConstructionOrderRequest;
import com.koiteampro.koipondcons.models.response.ConstructionOrderResponse;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.ConstructionOrderRepository;
import com.koiteampro.koipondcons.repositories.StaffConstructionDetailRepository;
import com.koiteampro.koipondcons.services.ConstructionOrderService;
import com.koiteampro.koipondcons.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ConstructionOrderTest {
    @InjectMocks
    private ConstructionOrderService constructionOrderService;

    @Mock
    private ConstructionOrderRepository constructionOrderRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private StaffConstructionDetailRepository staffConstructionDetailRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateConstructionOrder_Success() {
        ConstructionOrderRequest constructionOrderRequest = new ConstructionOrderRequest();
        ConstructionOrder constructionOrder = new ConstructionOrder();
        Customer customer = new Customer();
        customer.setId(1L);

        Mockito.when(modelMapper.map(constructionOrderRequest, ConstructionOrder.class)).thenReturn(constructionOrder);
        Mockito.when(constructionOrderRepository.save(constructionOrder)).thenReturn(constructionOrder);
        Mockito.when(customerService.getCurrentCustomer()).thenReturn(customer);
        Mockito.when(modelMapper.map(constructionOrder, ConstructionOrderResponse.class)).thenReturn(new ConstructionOrderResponse());

        ConstructionOrderResponse response = constructionOrderService.createConstructionOrder(constructionOrderRequest);

        assertNotNull(response);
        assertEquals(customer.getId(), response.getCustomer().getId());
    }
}
