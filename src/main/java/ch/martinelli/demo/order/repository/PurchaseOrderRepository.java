package ch.martinelli.demo.order.repository;

import ch.martinelli.demo.order.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("select o from PurchaseOrder o join fetch o.items i join fetch o.customer c join fetch i.product")
    List<PurchaseOrder> findAllFetchRelations();
}
