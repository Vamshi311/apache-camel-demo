package com.pmic.apachecameldemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import com.pmic.apachecameldemo.domain.OrderDto;
import com.pmic.apachecameldemo.entity.OrderEntity;
import com.pmic.apachecameldemo.entity.OrderStatus;
import com.pmic.apachecameldemo.mapper.OrderMapper;
import com.pmic.apachecameldemo.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
		this.orderMapper = Mappers.getMapper(OrderMapper.class);
	}

	public OrderDto addOrder(OrderDto orderDto) {
		log.info("Received order {}", orderDto);
		OrderEntity orderEntity = orderMapper.mapFromDto(orderDto);
		orderEntity = orderRepository.save(orderEntity);
		OrderDto savedOrder = orderMapper.mapToDto(orderEntity);
		log.info("Processed order {}", savedOrder);
		return savedOrder;
	}

	public List<OrderDto> addOrders(List<OrderDto> orderDtos) {
		log.info("Received orders {}", orderDtos);
		List<OrderEntity> orders = orderMapper.mapFromDtos(orderDtos);
		orders = orderRepository.saveAll(orders);
		List<OrderDto> savedOrders = orderMapper.mapToDtos(orders);
		log.info("Processed orders {}", savedOrders);
		return savedOrders;
	}

	public List<OrderDto> getPendingOrders() {
		Optional<List<OrderEntity>> ordersOptional = orderRepository.findAllByStatus(OrderStatus.PENDING);
		List<OrderEntity> orders = ordersOptional.orElse(new ArrayList());
		return orderMapper.mapToDtos(orders);
	}
}
