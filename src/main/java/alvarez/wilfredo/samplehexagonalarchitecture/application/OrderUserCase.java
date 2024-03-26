package alvarez.wilfredo.samplehexagonalarchitecture.application;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import alvarez.wilfredo.samplehexagonalarchitecture.domain.Orders;
import alvarez.wilfredo.samplehexagonalarchitecture.infra.inputport.OrderInputPort;
import alvarez.wilfredo.samplehexagonalarchitecture.infra.outputport.EntityRepository;

@Component
@RequiredArgsConstructor
public class OrderUserCase implements OrderInputPort {

    private final EntityRepository entityRepository;

    @Override
    public Orders createOrder(String customerId, BigDecimal total) {
        Orders order = Orders.builder()
            .id( UUID.randomUUID().toString() )
            .customerId( customerId )
            .total( total )
            .build();

        return entityRepository.save( order );
    }
    
}
