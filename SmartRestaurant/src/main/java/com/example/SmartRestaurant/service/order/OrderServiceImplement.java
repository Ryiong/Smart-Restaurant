package com.example.SmartRestaurant.service.order;

import com.example.SmartRestaurant.common.enums.*;
import com.example.SmartRestaurant.dto.request.OrderItemDeleteRequest;
import com.example.SmartRestaurant.dto.request.OrderRequest;
import com.example.SmartRestaurant.dto.response.OrderItemResponse;
import com.example.SmartRestaurant.dto.response.OrderResponse;
import com.example.SmartRestaurant.entity.OrderEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.entity.TableEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.OrderMapper;
import com.example.SmartRestaurant.repository.OrderRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.repository.TableRepository;
import com.example.SmartRestaurant.security.CurrentUserProvider;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import com.example.SmartRestaurant.service.orderitem.OrderItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.SmartRestaurant.validator.OrderValidator.validateOrderItemDeleteRequest;
import static com.example.SmartRestaurant.validator.OrderValidator.validateOrderRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class OrderServiceImplement implements OrderService {
    OrderRepository repository;

    AuthorizationService authorizationService;

    ShopRepository shopRepository;

    TableRepository tableRepository;

    OrderItemService orderItemService;

    OrderMapper mapper;

    CurrentUserProvider currentUserProvider;

    SimpMessagingTemplate messagingTemplate;

    @Override
    public OrderResponse create(OrderRequest request, Long shopId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_ORDER_CREATE");
        validateOrderRequest(request);
        TableEntity table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new NotFoundException("Bàn"));
        if (!table.getArea().getShop().getId().equals(shopId)) {
            throw new ValidateException("Bàn không thuộc cửa hàng");
        }
        if (table.getArea().getStatus().equals(AreaStatus.INACTIVE)) {
            throw new InvalidStatusException("khu vực");
        }
        if (table.getStatus().equals(TableStatus.INACTIVE)) {
            throw new InvalidStatusException("bàn");
        }
        //kiểm tra xem có order nào thuộc bàn này tại điểm tạo không
        List<OrderEntity> orderIsPendingInTable = repository.findAllByTableIdAndOrderStatus(
                request.getTableId(),
                OrderStatus.PENDING);
        OrderEntity order;
        if (orderIsPendingInTable.isEmpty()) {
            order = new OrderEntity();
            order.setTable(table);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setPaymentStatus(PaymentStatus.PENDING);
            order = repository.save(order);

        } else if (orderIsPendingInTable.size() == 1) {
            order = orderIsPendingInTable.get(0);
        } else {
            throw new ValidateException("Bàn có nhiều đơn hàng cùng lúc");
        }
        //set orderId cho các item và lưu các item
        List<OrderItemResponse> itemResponses = orderItemService.createAll(order,
                request.getItemRequests(),
                currentUserProvider.getCurrentUser().getUser());
        OrderResponse orderResponse = mapper.toResponse(order);
        orderResponse.setItemResponses(itemResponses);
        table.setOrderStatus(TableOrderStatus.OCCUPIED);
        tableRepository.save(table);

        return orderResponse;
    }

    @Override
    public void deleteAll(Long shopId, OrderItemDeleteRequest request) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_ORDER_ITEM_DELETE");
        validateOrderItemDeleteRequest(request);
        TableEntity table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new NotFoundException("Bàn"));
        if (!table.getArea().getShop().getId().equals(shopId)) {
            throw new ValidateException("Bàn không thuộc cửa hàng");
        }
        if (table.getArea().getStatus().equals(AreaStatus.INACTIVE)) {
            throw new InvalidStatusException("khu vực");
        }
        if (table.getStatus().equals(TableStatus.INACTIVE)) {
            throw new InvalidStatusException("bàn");
        }
        //kiểm tra xem có order nào thuộc bàn này tại điểm tạo không
        List<OrderEntity> orderIsPendingInTable = repository.findAllByTableIdAndOrderStatus(
                request.getTableId(),
                OrderStatus.PENDING);
        OrderEntity order;
        if (orderIsPendingInTable.isEmpty()) {
            throw new ValidateException("Hiện tại không có đơn hàng nào trong bàn");
        }
        if (orderIsPendingInTable.size() > 1) {
            throw new ValidateException("Bàn có nhiều đơn hàng cùng lúc");
        }
        order = orderIsPendingInTable.get(0);
        orderItemService.deleteAll(order, request.getOrderItemIds());
    }

    @Override
    public OrderResponse createByQR(OrderRequest request) {
        validateOrderRequest(request);
        TableEntity table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new NotFoundException("Bàn"));
        if (table.getArea().getStatus().equals(AreaStatus.INACTIVE)) {
            throw new InvalidStatusException("khu vực");
        }
        if (table.getStatus().equals(TableStatus.INACTIVE)) {
            throw new InvalidStatusException("bàn");
        }
        //kiểm tra xem có order nào thuộc bàn này tại điểm tạo không
        List<OrderEntity> orderIsPendingInTable = repository.findAllByTableIdAndOrderStatus(
                request.getTableId(),
                OrderStatus.PENDING);
        OrderEntity order;
        if (orderIsPendingInTable.isEmpty()) {
            order = new OrderEntity();
            order.setTable(table);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setPaymentStatus(PaymentStatus.PENDING);
            order = repository.save(order);

        } else if (orderIsPendingInTable.size() == 1) {
            order = orderIsPendingInTable.get(0);
        } else {
            throw new ValidateException("Bàn có nhiều đơn hàng cùng lúc");
        }
        //set orderId cho các item và lưu các item
        List<OrderItemResponse> itemResponses = orderItemService.createAll(order,
                request.getItemRequests(),
                null);
        OrderResponse orderResponse = mapper.toResponse(order);
        orderResponse.setItemResponses(itemResponses);
        table.setOrderStatus(TableOrderStatus.OCCUPIED);
        tableRepository.save(table);
        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAllByShopIdAndDate(Long shopId, LocalDate date) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_ORDER_VIEW");
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        List<OrderEntity> orders = repository.findAllByTable_Area_Shop_IdAndCreatedAtGreaterThanEqualAndCreatedAtBefore(
                shopId, start, end);
        return orders.stream().map(x -> mapper.toResponse(x)).collect(Collectors.toList());
    }

    @Override
    public void mergeOrderTable(Long shopId, Long fromId, Long toId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_ORDER_TABLE_MERGE");
        if (fromId.equals(toId)) {
            throw new ValidateException("Id bàn nguồn và bàn mục tiêu không được trùng nhau");
        }
        TableEntity fromTable = tableRepository.findById(fromId)
                .orElseThrow(() -> new NotFoundException("Bàn"));
        if (!fromTable.getArea().getShop().getId().equals(shopId)) {
            throw new ValidateException("Bàn không thuộc cửa hàng");
        }
        if (fromTable.getArea().getStatus().equals(AreaStatus.INACTIVE)) {
            throw new InvalidStatusException("khu vực");
        }
        if (fromTable.getStatus().equals(TableStatus.INACTIVE)) {
            throw new InvalidStatusException("bàn");
        }
        TableEntity toTable = tableRepository.findById(toId)
                .orElseThrow(() -> new NotFoundException("Bàn"));
        if (!toTable.getArea().getShop().getId().equals(shopId)) {
            throw new ValidateException("Bàn không thuộc cửa hàng");
        }
        if (toTable.getArea().getStatus().equals(AreaStatus.INACTIVE)) {
            throw new InvalidStatusException("khu vực");
        }
        if (toTable.getStatus().equals(TableStatus.INACTIVE)) {
            throw new InvalidStatusException("bàn");
        }
        //rule sẽ là nếu bàn source/target không có đơn hàng pending thì không cho merge
        List<OrderEntity> orderIsPendingInFromTable = repository.findAllByTableIdAndOrderStatus(
                fromId,
                OrderStatus.PENDING);
        if (orderIsPendingInFromTable.isEmpty()) {
            throw new ValidateException("Bàn nguồn không có đơn hàng để gộp");
        }
        if (orderIsPendingInFromTable.size() > 1) {
            throw new ValidateException("Bàn nguồn có nhiều đơn hàng cùng lúc");
        }

        OrderEntity fromOrder = orderIsPendingInFromTable.get(0);
        List<OrderEntity> orderIsPendingInToTable = repository.findAllByTableIdAndOrderStatus(
                toId,
                OrderStatus.PENDING);
        if (orderIsPendingInToTable.isEmpty()) {
            throw new ValidateException("Bàn mục tiêu không có đơn hàng để gộp");
        }
        if (orderIsPendingInToTable.size() > 1) {
            throw new ValidateException("Bàn mục tiêu có nhiều đơn hàng cùng lúc");
        }
        OrderEntity toOrder = orderIsPendingInToTable.get(0);
        orderItemService.mergeOrder(fromOrder, toOrder);
        fromOrder.setOrderStatus(OrderStatus.MERGED);
        fromTable.setOrderStatus(TableOrderStatus.EMPTY);
        tableRepository.save(fromTable);
    }

    @Override
    public void moveOrderTable(Long shopId, Long fromId, Long toId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_ORDER_TABLE_MOVE");
        if (fromId.equals(toId)) {
            throw new ValidateException("Id bàn nguồn và bàn mục tiêu không được trùng nhau");
        }
        TableEntity fromTable = tableRepository.findById(fromId)
                .orElseThrow(() -> new NotFoundException("Bàn"));
        if (!fromTable.getArea().getShop().getId().equals(shopId)) {
            throw new ValidateException("Bàn không thuộc cửa hàng");
        }
        if (fromTable.getArea().getStatus().equals(AreaStatus.INACTIVE)) {
            throw new InvalidStatusException("khu vực");
        }
        if (fromTable.getStatus().equals(TableStatus.INACTIVE)) {
            throw new InvalidStatusException("bàn");
        }
        TableEntity toTable = tableRepository.findById(toId)
                .orElseThrow(() -> new NotFoundException("Bàn"));
        if (!toTable.getArea().getShop().getId().equals(shopId)) {
            throw new ValidateException("Bàn không thuộc cửa hàng");
        }
        if (toTable.getArea().getStatus().equals(AreaStatus.INACTIVE)) {
            throw new InvalidStatusException("khu vực");
        }
        if (toTable.getStatus().equals(TableStatus.INACTIVE)) {
            throw new InvalidStatusException("bàn");
        }
        //rule sẽ là nếu bàn target không có đơn hàng pending và bàn source phải có order pending thì mới cho move
        List<OrderEntity> orderIsPendingInFromTable = repository.findAllByTableIdAndOrderStatus(
                fromId,
                OrderStatus.PENDING);
        if (orderIsPendingInFromTable.isEmpty()) {
            throw new ValidateException("Bàn nguồn không có đơn hàng để di chuyển");
        }
        if (orderIsPendingInFromTable.size() > 1) {
            throw new ValidateException("Bàn nguồn có nhiều đơn hàng cùng lúc");
        }

        OrderEntity fromOrder = orderIsPendingInFromTable.get(0);
        List<OrderEntity> orderIsPendingInToTable = repository.findAllByTableIdAndOrderStatus(
                toId,
                OrderStatus.PENDING);
        if (!orderIsPendingInToTable.isEmpty()) {
            throw new ValidateException("Bàn mục tiêu đã có người");
        }
        fromOrder.setTable(toTable);
        fromTable.setOrderStatus(TableOrderStatus.EMPTY);
        toTable.setOrderStatus(TableOrderStatus.OCCUPIED);
    }

    @Override
    public OrderResponse pay(Long shopId, Long orderId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_ORDER_PAYMENT");
        OrderEntity order = repository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Đơn hàng"));
        if (!order.getTable().getArea().getShop().getId().equals(shopId)) {
            throw new ValidateException("Đơn hàng không thuộc cửa hàng");
        }
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new InvalidStatusException("đơn hàng");
        }
        BigDecimal totalPrice = orderItemService.getTotalPriceByOrderId(orderId);
        order.setOrderStatus(OrderStatus.PAID);
        order.setPaymentStatus(PaymentStatus.COMPLETED);
        order.setPaidAt(LocalDateTime.now());
        order.getTable().setOrderStatus(TableOrderStatus.EMPTY);
        order.setPaymentMethod(PaymentMethod.CASH);
        OrderResponse orderResponse = mapper.toResponse(order);
        orderResponse.setTotalPrice(totalPrice);
        messagingTemplate.convertAndSend("/host/" + shopId, orderResponse);
        return orderResponse;
    }

}
