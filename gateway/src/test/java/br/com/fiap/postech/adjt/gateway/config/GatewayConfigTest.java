package br.com.fiap.postech.adjt.gateway.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class GatewayConfigTest {

    @Autowired
    private GatewayConfig gatewayConfig;

    @Autowired
    private RouteLocatorBuilder routeLocatorBuilder;

    @BeforeEach
    void setUp() {
        routeLocatorBuilder = mock(RouteLocatorBuilder.class);
        gatewayConfig = new GatewayConfig();
    }

    @Test
    void testCustomRouteLocator() {

        //Arrange
        RouteLocator routeLocator = mock(RouteLocator.class);
        RouteLocatorBuilder.Builder builder = Mockito.mock(RouteLocatorBuilder.Builder.class);

        when(routeLocatorBuilder.routes()).thenReturn(builder);
        when(builder.route(anyString(), any())).thenReturn(builder);
        when(builder.build()).thenReturn(routeLocator);

        //Act
        RouteLocator result = gatewayConfig.customRouteLocator(routeLocatorBuilder);

        //Assert
        assertNotNull(result);
        verify(routeLocatorBuilder, times(1)).routes();
        verify(builder, times(5)).route(anyString(), any());
        verify(builder, times(1)).build();
    }
}