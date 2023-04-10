package com.github.wenqiglantz.service.customer.restcontroller;

import com.github.wenqiglantz.service.customer.data.CustomerVO;
import com.github.wenqiglantz.service.customer.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/customers",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    private final CustomerService customerService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCustomer(@RequestBody CustomerVO customerVO, UriComponentsBuilder uriBuilder)
        throws Exception {
        CustomerVO newCustomerVO = customerService.saveCustomer(customerVO);
        URI location = uriBuilder
                .path("/customers/{customerId}")
                .buildAndExpand(newCustomerVO.getCustomerId())
                .toUri();
        return ResponseEntity.created(location)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(CustomerVO.builder()
                        .customerId(newCustomerVO.getCustomerId())
                        .firstName(newCustomerVO.getFirstName())
                        .lastName(newCustomerVO.getLastName())
                        .build());
    }

    @GetMapping(produces = JSON)
    public ResponseEntity<List<CustomerVO>> getCustomers() {
        return ResponseEntity.ok(customerService.getCustomers());
    }

    @GetMapping(value = "/{customerId}")
    public ResponseEntity getCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @PutMapping(value = "/{customerId}", consumes = JSON)
    public ResponseEntity updateCustomer(@PathVariable String customerId, @RequestBody CustomerVO customerVO)
        throws Exception {
        customerService.updateCustomer(customerId, customerVO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable String customerId) throws Exception {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
