package com.pmic.apachecameldemo.processor;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.pmic.apachecameldemo.domain.OrderDto;
import com.pmic.apachecameldemo.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderProcessor implements Processor {

	private static final String FULFILLMENT_CENTER_SOAP = "fullfillment-center-soap";
	private final OrderService orderService;

	public OrderProcessor(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		List<OrderDto> orders = exchange.getIn().getBody(List.class);
		log.info("Received {} via file process", orders);
		isValidInput(orders);
		orderService.addOrders(orders);
		log.info("Finished processing orders from file");
	}

	private void isValidInput(List<OrderDto> orders) throws Exception {
		// throw exception if fulfillment center is soap based.
		boolean hasSoapBasedOrder = orders.stream()
				.filter(o -> o.getFulfillmentCenter().equals(FULFILLMENT_CENTER_SOAP)).findFirst().isPresent();
		if (hasSoapBasedOrder) {
			throw new Exception("Soap based fulfillment center orders are not supported");
		}
	}

}
