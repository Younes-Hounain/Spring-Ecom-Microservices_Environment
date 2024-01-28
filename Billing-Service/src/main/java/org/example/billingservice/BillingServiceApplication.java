package org.example.billingservice;

import org.example.billingservice.models.Customer;
import org.example.billingservice.models.Product;
import org.example.billingservice.repositories.BillRepository;
import org.example.billingservice.repositories.ProductItemRepository;
import org.example.billingservice.service.CustomarRestClient;
import org.example.billingservice.service.ProductRestClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collection;
import java.util.Date;

@EnableFeignClients
@SpringBootApplication
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository, ProductItemRepository productItemRepository, CustomarRestClient customarRestClient, ProductRestClient productRestClient) {

        return args -> {
            Collection<Product> products = productRestClient.AllProducts().getContent();
            Long customerID = 1L;
            Customer customer = customarRestClient.findCustomerById(customerID);
            Bill bill = new Bill();
            bill.setBillDate(new Date());

            if (customer == null){
                throw new RuntimeException("Customer not found");
            }

            bill.setCustomerId(customer.getId());
            Bill savedBill =  billRepository.save(bill);

            products.forEach(product -> {
                ProductItem productItem = new ProductItem();
                productItem.setPrice(product.getPrice());
                productItem.setQuantity(1 + (int)(Math.random() * 10));
                productItem.setDiscount(Math.random() * 10);
                productItem.setBill(savedBill);
                productItem.setProductId(product.getId());
                productItemRepository.save(productItem);
            });
        };
    }

}
