

package com.enigma.tokonyadia_api.service;

import com.enigma.tokonyadia_api.entity.Customer;

import java.util.List;

public interface CustomerService {

    public Customer getByID(Integer id);
    public Customer create(Customer menu);
    public List<Customer> getAll();
    public Customer update(Customer menu);
    public String deleteById(Integer id);

}
