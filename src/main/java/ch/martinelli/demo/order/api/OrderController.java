package ch.martinelli.demo.order.api;

import ch.martinelli.demo.order.repository.PurchaseOrderRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.Transient;
import java.util.List;

@RequestMapping("orders")
@RestController
class OrderController {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ModelMapper modelMapper;

    OrderController(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    @GetMapping("/osiv")
    List<PurchaseOrderDTO> getPurchaseOrders() {
        var purchaseOrders = purchaseOrderRepository.findAll();

        return purchaseOrders.stream()
                .map(order -> modelMapper.map(order, PurchaseOrderDTO.class))
                .toList();
    }

    @GetMapping("/fetch")
    List<PurchaseOrderDTO> getPurchaseOrdersFetchRelations() {
        var purchaseOrders = purchaseOrderRepository.findAllFetchRelations();

        return purchaseOrders.stream()
                .map(order -> modelMapper.map(order, PurchaseOrderDTO.class))
                .toList();
    }
}
