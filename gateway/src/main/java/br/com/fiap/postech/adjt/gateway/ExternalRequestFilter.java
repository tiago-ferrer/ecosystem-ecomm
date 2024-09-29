package br.com.fiap.postech.adjt.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.InetAddress;

@Configuration
public class ExternalRequestFilter implements Ordered, WebFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String xForwardedForHeader = request.getHeaders().getFirst("X-Forwarded-For");
        String clientIp = xForwardedForHeader != null ? xForwardedForHeader : request.getRemoteAddress().getAddress().getHostAddress();

        if (!isInternalIp(clientIp)) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);

            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private boolean isInternalIp(String ip) {
        String gatewayIP = getGatewayIp().substring(0, 11);

        return ip.startsWith(gatewayIP);
    }

    private String getGatewayIp() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
}
