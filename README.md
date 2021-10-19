# Apache Camel Notes
**Introduction**

Apache Camel is an open source integration framework that allows you to integrate technologically diverse systems using a large library of components. 
It is packed with several hundred components that are used to access databases, message queues, APIs or basically anything under the sun. Helping you integrate with everything. 
If the communicating system changes it's protocol, camel already has that component and all we need to do is changing the component. 
It helps us avoid writing the system specific huge code for communicating with it.

**Use case**

An application receives order requests from UI and a FTP batch process. Once requests are received, it processes them and saves them in database. I don't want to ship our orders as soon as I get them. It is not economical. So, at mid night, I trigger the shipment of orders by sending an event to our application. Upon receiving the event, it retrieves the orders from database and sends them to shipment centers. Each shipment center accepts shipment requests in different formats because we acquired them recently and they have their own software to process the shipments and we honor them. Later a new shipment center is added and it accepts orders as events to Apache Kafka and one existing shipment center changes it's protocol from SOAP to REST. Camel provides those components and all we need to change is component which is basically a URI.

* Main application (Order Management Application) will have three endpoints. 1. receive requests from UI, 2. receive requests from file and 3. receive event for shipment of orders for the day.
* One shipment center will receive shipment requests via rest calls.
* One shipment center will receive shipment requests via SOAP calls.
* One shipment center will receive shipment requests via FTP.
* One shipment center will receive shipment requests via events in RabbitMq.

```
 UI----Order Mgt App -------            |__REST
 File--|		    |		|__SOAP-- REST (Modified)
			    CAMEL------	|__RABBITMQ
					|__FILE
					|__KAFKA (New)
							
```
