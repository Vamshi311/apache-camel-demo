package com.pmic.apachecameldemo.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.pmic.apachecameldemo.domain.OrderDto;
import com.pmic.apachecameldemo.entity.OrderStatus;
import com.pmic.apachecameldemo.service.ShippingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShippingProcessor implements Processor {

	private final ShippingService shippingService;

	public ShippingProcessor(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// retrieving the orders for shipping
		List<OrderDto> orders = shippingService.getOrdersReadyForShippings();
		List<Long> orderIds = orders.stream().map(o -> o.getId()).collect(Collectors.toList());

		// updating the status of orders
		shippingService.updateOrderStatus(orderIds, OrderStatus.IN_PROGRESS);

		// set the exchange body to orders that need to ship. we will route them to
		// fulfillment centers
		exchange.getIn().setBody(orders);
	}

}
