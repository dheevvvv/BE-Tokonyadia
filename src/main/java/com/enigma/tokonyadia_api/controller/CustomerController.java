package com.enigma.tokonyadia_api.controller;


import com.enigma.tokonyadia_api.service.CustomerService;
import com.enigma.tokonyadia_api.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/new")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.create(customer);
    }
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Integer id) {
        return customerService.getByID(id);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAll();
    }

    @PutMapping("/update/{id}")
    public Customer updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        customer.setId(id);
        return customerService.update(customer);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCustomerById(@PathVariable Integer id) {
        return customerService.deleteById(id);
    }

}
