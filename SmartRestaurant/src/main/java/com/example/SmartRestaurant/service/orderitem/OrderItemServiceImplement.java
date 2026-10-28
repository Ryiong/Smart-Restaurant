package com.example.SmartRestaurant.service.orderitem;

import com.example.SmartRestaurant.common.enums.OrderItemStatus;
import com.example.SmartRestaurant.common.enums.ProductStatus;
import com.example.SmartRestaurant.dto.request.OrderItemRequest;
import com.example.SmartRestaurant.dto.response.OrderItemResponse;
import com.example.SmartRestaurant.entity.OrderEntity;
import com.example.SmartRestaurant.entity.OrderItemEntity;
import com.example.SmartRestaurant.entity.ProductEntity;
import com.example.SmartRestaurant.entity.UserEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.OrderItemMapper;
import com.example.SmartRestaurant.repository.OrderItemRepository;
import com.example.SmartRestaurant.repository.ProductRepository;
import com.example.SmartRestaurant.security.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class OrderItemServiceImplement implements OrderItemService {
    OrderItemRepository repository;
    ProductRepository productRepository;

    OrderItemMapper mapper;
    CurrentUserProvider currentUserProvider;

    @Override
    public List<OrderItemResponse> createAll(OrderEntity order,
                                             List<OrderItemRequest> itemRequests,
                                             UserEntity createdBy) {
        Set<Long> productIds = itemRequests
                .stream()
                .map(x -> x.getProductId())
                .collect(Collectors.toSet());
        if (productIds.size() != itemRequests.size()) {
            throw new ValidateException("Có sản phẩm bị trùng");
        }
        List<ProductEntity> products = productRepository.findAllByIdInAndAndStatus(productIds, ProductStatus.ACTIVE);
        if (products.size() != productIds.size()) {
            throw new NotFoundException("Sản phẩm");
        }
        Map<Long, ProductEntity> productMap = new HashMap<>();
        products.forEach(x -> {
            productMap.put(x.getId(), x);
        });
        List<OrderItemEntity> preSaveOrderItemList = new ArrayList<>();
        itemRequests.forEach(x -> {
            OrderItemEntity orderItem = mapper.toEntity(x);
            orderItem.setOrder(order);
            ProductEntity product = productMap.get(x.getProductId());
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(x.getQuantity())));
            orderItem.setUser(createdBy);
            orderItem.setStatus(OrderItemStatus.ACTIVE);
            preSaveOrderItemList.add(orderItem);
        });
        return repository.saveAll(preSaveOrderItemList)
                .stream()
                .map(x -> mapper.toResponse(x))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll(OrderEntity order, List<Long> orderItemIds) {
        Set<Long> itemIds = new HashSet<>(orderItemIds);
        if (itemIds.size() != orderItemIds.size()) {
            throw new ValidateException("Có món bị trùng");
        }
        List<OrderItemEntity> orderItems = repository.findAllByIdInAndOrderId(itemIds, order.getId());
        if (orderItems.size() != itemIds.size()) {
            throw new NotFoundException("Món trong đơn hàng");
        }
        orderItems.forEach(x -> {
            if (x.getStatus().equals(OrderItemStatus.INACTIVE)) {
                throw new InvalidStatusException("món trong dơn hàng");
            }
            x.setStatus(OrderItemStatus.INACTIVE);
            x.setDeletedAt(LocalDateTime.now());
            x.setDeletedBy(currentUserProvider.getCurrentUser().getUser());
        });
        repository.saveAll(orderItems);
    }

    @Override
    public void mergeOrder(OrderEntity fromOrder, OrderEntity toOrder) {
        List<OrderItemEntity> itemsOfFromOrder = repository.findAllByOrderId(fromOrder.getId());
        itemsOfFromOrder.forEach(x -> x.setOrder(toOrder));
        repository.saveAll(itemsOfFromOrder);
    }

    @Override
    public BigDecimal getTotalPriceByOrderId(Long orderId) {
        List<OrderItemEntity> items = repository.findAllByOrderIdAndStatus(orderId, OrderItemStatus.ACTIVE);
        if (items.size() == 0) {
            throw new ValidateException("Đơn hàng không có sản phẩm nào");
        }
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItemEntity item : items) {
            totalPrice = totalPrice.add(item.getTotalPrice());
        }
        return totalPrice;
    }
}
