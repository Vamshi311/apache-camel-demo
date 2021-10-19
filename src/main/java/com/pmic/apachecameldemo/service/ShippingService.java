package com.pmic.apachecameldemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.camel.ProducerTemplate;
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
public class ShippingService {

	private final OrderRepository orderRepository;

	private final ProducerTemplate producerTemplate;

	private final OrderMapper orderMapper;

	public ShippingService(OrderRepository orderRepository, ProducerTemplate producerTemplate) {
		this.producerTemplate = producerTemplate;
		this.orderRepository = orderRepository;
		this.orderMapper = Mappers.getMapper(OrderMapper.class);
	}

	public void startShipping(String message) {
		this.producerTemplate.sendBody("direct:start-shipping", message);
	}

	public List<OrderDto> getOrdersReadyForShippings() {
		Optional<List<OrderEntity>> ordersOptional = orderRepository.findAllByStatus(OrderStatus.PENDING);
		List<OrderEntity> orders = ordersOptional.orElse(new ArrayList<>());
		return orderMapper.mapToDtos(orders);
	}

	public void updateOrderStatus(List<Long> orderIds, OrderStatus status) {
		orderRepository.upadteOrderStatus(orderIds, status);
	}
}
