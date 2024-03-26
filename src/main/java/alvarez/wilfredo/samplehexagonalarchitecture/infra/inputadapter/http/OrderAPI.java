package alvarez.wilfredo.samplehexagonalarchitecture.infra.inputadapter.http;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import alvarez.wilfredo.samplehexagonalarchitecture.domain.Orders;
import alvarez.wilfredo.samplehexagonalarchitecture.infra.inputport.OrderInputPort;

@RestController
@RequestMapping(value = "order")
@RequiredArgsConstructor
public class OrderAPI {
    
    private final OrderInputPort orderInputPort;

    @PostMapping(value = "create", produces=MediaType.APPLICATION_JSON_VALUE)
    public Orders create( @RequestParam String customerId, @RequestParam BigDecimal total ) {
        return orderInputPort.createOrder(customerId, total);
    }
    
}
