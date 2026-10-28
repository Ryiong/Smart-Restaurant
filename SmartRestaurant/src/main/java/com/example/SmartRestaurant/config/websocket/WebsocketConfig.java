package com.example.SmartRestaurant.config.websocket;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    WebSocketFilter webSocketFilter;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");//sau này cần thay bằng miền của fe
        ;//cửa vào ws
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/host");//nơi mà message từ app đi ra ngoài qua broker
        registry.setApplicationDestinationPrefixes("/app");//nơi nhận message từ ngoài vào app
    }
    //stomp nó sẽ là quy cách đóng gói message và lưu động trong websocket

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketFilter);
        //đăng ký filter cho các message nhận vào
    }
}
