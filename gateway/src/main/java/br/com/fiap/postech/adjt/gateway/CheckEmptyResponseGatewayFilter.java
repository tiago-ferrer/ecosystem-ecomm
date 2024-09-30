//package br.com.fiap.postech.adjt.gateway;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class CheckEmptyResponseGatewayFilter implements GatewayFilter {
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // Intercepta a resposta após o processamento da cadeia de filtros
//        return chain.filter(exchange).then(Mono.defer(() -> {
//            ServerHttpResponse response = exchange.getResponse();
//
//            // Verifica se o status da resposta é 200 OK
//            if (response.getStatusCode() == HttpStatus.OK) {
//                // Captura o corpo da resposta
//                return response.getBody()
//                        .collectList()
//                        .flatMap(dataBuffers -> {
//                            // Concatena os DataBuffers em um único DataBuffer
//                            DataBuffer joinedDataBuffer = response.bufferFactory().join(dataBuffers);
//                            byte[] content = new byte[joinedDataBuffer.readableByteCount()];
//                            joinedDataBuffer.read(content);
//                            joinedDataBuffer.release();
//
//                            if (content.length == 0) {
//                                // Resposta vazia: define o status como 400 Bad Request
//                                response.setStatusCode(HttpStatus.BAD_REQUEST);
//                                byte[] bytes = "Resposta vazia da API externa".getBytes();
//                                DataBuffer buffer = response.bufferFactory().wrap(bytes);
//                                return response.writeWith(Mono.just(buffer));
//                            } else {
//                                // Reescreve o corpo original na resposta
//                                DataBuffer buffer = response.bufferFactory().wrap(content);
//                                return response.writeWith(Mono.just(buffer));
//                            }
//                        });
//            }
//
//            // Se o status não for 200 OK, não faz nada
//            return Mono.empty();
//        }));
//    }
//}
