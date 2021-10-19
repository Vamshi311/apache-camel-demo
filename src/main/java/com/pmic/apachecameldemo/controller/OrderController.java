package com.pmic.apachecameldemo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pmic.apachecameldemo.domain.OrderDto;
import com.pmic.apachecameldemo.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	public final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public OrderDto addOrder(@RequestBody OrderDto orderDto) {
		return orderService.addOrder(orderDto);
	}

	@GetMapping("/pending/orders")
	public List<OrderDto> getPendingOrders() {
		return orderService.getPendingOrders();
	}
}
