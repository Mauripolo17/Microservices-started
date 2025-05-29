package com.example.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final AtomicLong COUNTER = new AtomicLong(1);

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {


            long requestId = COUNTER.getAndIncrement();
            long startTime = System.currentTimeMillis();
            var request = exchange.getRequest();
            logger.info("Request: {}", request.getHeaders());

            logger.info("[{}] Incoming {} {} from {}",
                    requestId,
                    request.getMethod(),
                    request.getURI().getPath(),
                    request.getRemoteAddress());

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                long duration = System.currentTimeMillis() - startTime;
                logger.info("[{}] Completed {} {} - Status: {} ({} ms)",
                        requestId,
                        request.getMethod(),
                        request.getURI().getPath(),
                        exchange.getResponse().getStatusCode(),
                        duration);
            }));
        };
    }

    public static class Config {
    }
}