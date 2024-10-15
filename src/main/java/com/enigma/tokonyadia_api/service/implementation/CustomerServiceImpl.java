package com.enigma.tokonyadia_api.service.implementation;

import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.repository.CustomerRepository;
import com.enigma.tokonyadia_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer getByID(String id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(Customer customer) {
        if (customerRepository.findById(customer.getId()).isPresent()) {
            return customerRepository.save(customer);
        }
        return null;
    }

    @Override
    public String deleteById(String id) {
        if (customerRepository.findById(id).isPresent()) {
            customerRepository.deleteById(id);
            return "Customer deleted";
        }
        return "Customer not found";
    }
}
