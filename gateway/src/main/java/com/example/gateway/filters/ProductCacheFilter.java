package com.example.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ProductCacheFilter implements GatewayFilter {


    private final RedisTemplate<String, Object> redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCacheFilter.class);

    public ProductCacheFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().toString();

        if (path.equals("/api/product") && method.equals("GET") ) {
            Object cachedProducts = redisTemplate.opsForValue().get("products::allProducts");

            if (cachedProducts != null) {
                try {
                    byte[] responseBody = new ObjectMapper().writeValueAsBytes(cachedProducts);
                    ServerHttpResponse response = exchange.getResponse();
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    DataBuffer buffer = response.bufferFactory().wrap(responseBody);
                    LOGGER.info("Cached response for GET /api/product - HOLA");
                    return response.writeWith(Mono.just(buffer));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }

        // Continuar con el flujo si no hay en cache
        return chain.filter(exchange);
    }
}
