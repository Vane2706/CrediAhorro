package upeu.edu.pe.gateway.beans;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class GatewayBeans {
    @Bean
    @Profile(value = "eureka-off")
    public RouteLocator routeLocatorEurekaOff (RouteLocatorBuilder builder){
        return builder
                .routes()
                .route(route -> route
                        .path("/admin-service/*")
                        .uri("http://localhost:8081")
                )
                .route(route -> route
                        .path("/report-ms/*")
                        .uri("http://localhost:8082")
                )
                .build();
    }
    @Bean
    @Profile(value = "eureka-on")
    public RouteLocator routeLocatorEurekaOn (RouteLocatorBuilder builder){
        return builder
                .routes()
                .route(route -> route
                        .path("/admin-service/**")
                        .uri("lb://admin-service")  //load balance = lb = balanceo de carga
                )
                .route(route -> route
                        .path("/report-ms/**")
                        .uri("lb://report-ms")  //load balance = lb = balanceo de carga
                )
                .build();
    }




}
