package org.example.billingservice.web;

import org.example.billingservice.entities.Bill;
import org.example.billingservice.repositories.BillRepository;
import org.example.billingservice.repositories.ProductItemRepository;
import org.example.billingservice.service.CustomarRestClient;
import org.example.billingservice.service.ProductRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillRestController {


    @Autowired
private BillRepository billRepository;
    @Autowired
private ProductItemRepository productItemRepository;
    @Autowired
private CustomarRestClient customerRestClient;
    @Autowired
private ProductRestClient productRestClient;

@GetMapping( "/fullBill/{id}")
public Bill bill(@PathVariable  Long id){
    Bill bill=billRepository.findById(id).get();
    bill.setCustomer(customerRestClient.findCustomerById(bill.getCustomerId()));
    bill.getProductItems().forEach(productItem->{
        productItem.setProduct(productRestClient.findProductById(productItem.getProductId()));
    });
    return bill;

}

}
