package co.edu.unbosque.microservice_order.repository;


import co.edu.unbosque.microservice_order.model.entity.Order;
import co.edu.unbosque.microservice_order.model.enums.OrderSide;
import co.edu.unbosque.microservice_order.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Buscar todas las órdenes por usuario
    List<Order> findByUserId(Long userId);

    // Buscar una orden por su ID de Alpaca
    Order findByAlpacaOrderId(String alpacaOrderId);



    // Buscar ordenes por usuario y estado
    List<Order> findByUserIdAndOrderStatus(Integer userId, OrderStatus orderStatus);

    List<Order> findByUserIdAndAlpacaStatusIn(Integer userId, List<String> statuses);



    //Buscar ordenes por tipo compra o venta
    List<Order> findAllByOrderSide(OrderSide orderSide);


}
