package com.pmic.apachecameldemo.routes;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.stereotype.Component;

import com.pmic.apachecameldemo.domain.OrderDto;
import com.pmic.apachecameldemo.processor.ShippingProcessor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShipRoutes extends RouteBuilder {

	private static final String FULFILLMENT_CENTER_ROUTES = "fullfillment-center-routes";
	private static final String FULFILLMENT_CENTER = "fullfillment-center";
	private static final String FULFILLMENT_CENTER_FILE = "fullfillment-center-file";
	private static final String FULFILLMENT_CENTER_RABBIT = "fullfillment-center-rabbit";
	private static final String FULFILLMENT_CENTER_REST = "fullfillment-center-rest";

	private final ShippingProcessor shippingProcessor;

	public ShipRoutes(ShippingProcessor shippingProcessor) {
		this.shippingProcessor = shippingProcessor;
	}

	@Override
	public void configure() throws Exception {
		JacksonDataFormat orderListformat = new ListJacksonDataFormat(OrderDto.class);

		from("direct:start-shipping") // route to initiate shipments
				.routeId("start-shipping-route").log("Received event for starting shipping ${body}")
				.process(shippingProcessor)
				// routes to which we need to send orders
				.setHeader(FULFILLMENT_CENTER_ROUTES, constant(
						"direct:fulfillment-center-file,direct:fulfillment-center-rabbit,direct:fulfillment-center-rest"))
				.recipientList(header(FULFILLMENT_CENTER_ROUTES)); // sends the exchange to multiple routes at one go in
																	// parallel

		// route orders to fulfillment centers file
		from("direct:fulfillment-center-file").routeId("fulfillment-center-file-route")
				.setHeader(FULFILLMENT_CENTER, constant(FULFILLMENT_CENTER_FILE)).process(this::filterOrders) // retrieve
																												// only
																												// fulfillment
																												// center
																												// file
																												// orders
				.log("sending oorders to fulfillment center file ${body}").marshal(orderListformat)
				.setHeader(Exchange.FILE_NAME, constant("fulfillment-center-file.json")) // set the file name here
				.to("{{fulfillment-center-file-route}}");

		// route orders to fulfillment centers rabbit
		from("direct:fulfillment-center-rabbit").routeId("fulfillment-center-rabbit-route")
				.setHeader(FULFILLMENT_CENTER, constant(FULFILLMENT_CENTER_RABBIT)).process(this::filterOrders) // retrieve
																												// only
																												// fulfillment
																												// center
																												// rabbit
																												// orders
				.log("sending orders to fulfillment center rabbit ${body}").marshal(orderListformat)
				.to("{{ship-route}}");

		// route orders to fulfillment centers rest
		from("direct:fulfillment-center-rest").routeId("fulfillment-center-rest-route")
				.setHeader(FULFILLMENT_CENTER, constant(FULFILLMENT_CENTER_REST)).process(this::filterOrders)
				.log("sending orders to fulfillment center rest ${body} to {{ship-route}}").marshal(orderListformat)
				.setHeader(Exchange.HTTP_METHOD, simple("POST"))
				.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
				// .to("{{fulfillment-center-rest-route}}");
				.to("netty-http:http://localhost:8082/shipping");

	}

	private void filterOrders(Exchange exchange) {
		String fulfillmentCenter = exchange.getIn().getHeader(FULFILLMENT_CENTER, String.class);
		List<OrderDto> orders = exchange.getIn().getBody(List.class);
		orders = orders.stream().filter(o -> o.getFulfillmentCenter().equalsIgnoreCase(fulfillmentCenter))
				.collect(Collectors.toList());
		exchange.getIn().setBody(orders);
	}

}
