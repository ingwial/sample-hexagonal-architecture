package alvarez.wilfredo.samplehexagonalarchitecture.infra.inputport;

import java.util.List;

import alvarez.wilfredo.samplehexagonalarchitecture.domain.Customer;

public interface CustomerInputPort {

    Customer createCustomer(String name, String country);

    Customer getById(String customerId);

    List<Customer> getAll();
    
}
