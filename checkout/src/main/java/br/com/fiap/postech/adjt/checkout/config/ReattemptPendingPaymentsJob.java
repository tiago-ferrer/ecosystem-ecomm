package br.com.fiap.postech.adjt.checkout.config;

import br.com.fiap.postech.adjt.checkout.kafka.PaymentProducer;
import br.com.fiap.postech.adjt.checkout.model.Checkout;
import br.com.fiap.postech.adjt.checkout.model.Order;
import br.com.fiap.postech.adjt.checkout.service.CheckoutService;
import br.com.fiap.postech.adjt.checkout.service.OrderService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
public class ReattemptPendingPaymentsJob {
    private final OrderService orderService;
    private final CheckoutService checkoutService;
    private final PaymentProducer paymentProducer;

    public ReattemptPendingPaymentsJob(OrderService orderService,
                                       CheckoutService checkoutService,
                                       PaymentProducer paymentProducer) {
        this.orderService = orderService;
        this.checkoutService = checkoutService;
        this.paymentProducer = paymentProducer;
    }

    @Scheduled(cron = "0 0 9 ? * MON") // Scheduled for every Monday 9am
    public void reattemptPendingPayments() {
        List<Order> pendingOrders = orderService.findPendingOrders();
        pendingOrders.forEach(order -> {
            Checkout checkout = checkoutService.findByOrderId(order.getOrderId());
            paymentProducer.sendPaymentRequest(order, checkout);
        });
    }
}
