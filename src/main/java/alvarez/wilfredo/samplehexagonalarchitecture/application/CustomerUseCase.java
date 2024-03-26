package alvarez.wilfredo.samplehexagonalarchitecture.application;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import alvarez.wilfredo.samplehexagonalarchitecture.domain.Customer;
import alvarez.wilfredo.samplehexagonalarchitecture.infra.inputport.CustomerInputPort;
import alvarez.wilfredo.samplehexagonalarchitecture.infra.outputport.EntityRepository;

@Component
@RequiredArgsConstructor
public class CustomerUseCase implements CustomerInputPort {

    private final EntityRepository entityRepository;

    @Override
    public Customer createCustomer(String name, String country) {
        Customer customer = Customer.builder()
            .id( UUID.randomUUID().toString() )
            .name( name )
            .country( country )
            .build();

        return entityRepository.save( customer );
    }

    @Override
    public Customer getById(String customerId) {
        return entityRepository.getById( customerId, Customer.class );
    }

    @Override
    public List<Customer> getAll() {
        return entityRepository.getAll( Customer.class );
    }
    
}
