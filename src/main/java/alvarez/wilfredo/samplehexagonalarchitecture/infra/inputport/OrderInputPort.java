package alvarez.wilfredo.samplehexagonalarchitecture.infra.inputport;

import java.math.BigDecimal;

import alvarez.wilfredo.samplehexagonalarchitecture.domain.Orders;

public interface OrderInputPort {
    Orders createOrder( String customerId, BigDecimal total );
}
