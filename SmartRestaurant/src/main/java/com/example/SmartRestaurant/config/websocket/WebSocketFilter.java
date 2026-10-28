package com.example.SmartRestaurant.config.websocket;

import com.example.SmartRestaurant.common.enums.HostDeviceStatus;
import com.example.SmartRestaurant.config.jwt.JwtService;
import com.example.SmartRestaurant.entity.HostDeviceEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.repository.HostDeviceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketFilter implements ChannelInterceptor {
    JwtService jwtService;

    HostDeviceRepository hostDeviceRepository;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class);
        // truy cập vào header của message mà client gửi lên

        if (accessor != null) {
            switch (accessor.getCommand()) {
                case CONNECT:
                    //kiểm tra có truy cập được header không và trạng thái stomp có phải là connect không
                    String auth = accessor.getFirstNativeHeader("Authorization");
                    //lấy jwt
                    if (auth == null || !auth.startsWith("Bearer ")) {
                        throw new IllegalArgumentException("Missing JWT");
                    }
                    String token = auth.substring(7);
                    // Xác thực JWT bằng JwtService hiện có
                    Long hostDeviceId = jwtService.extractHostDeviceId(token);
                    HostDeviceEntity hostDevice = hostDeviceRepository.findById(hostDeviceId)
                            .orElseThrow(() -> new NotFoundException("Máy chủ"));
                    // Sau đó kiểm tra HostDevice ACTIVE
                    if (!hostDevice.getStatus().equals(HostDeviceStatus.ACTIVE)) {
                        throw new InvalidStatusException("máy chủ");
                    }
                    // Gắn Principal vào accessor
                    accessor.setUser(
                            new UsernamePasswordAuthenticationToken(
                                    hostDeviceId,
                                    null,
                                    Collections.emptyList()
                            )
                    );
                    break;
                case SUBSCRIBE:
                    if (accessor.getUser() == null) {
                        throw new ValidateException("Chưa xác thực máy chủ");
                    }
                    String hostId = accessor.getUser().getName();
                    String destination = accessor.getDestination();
                    if (destination.isBlank() || !destination.equals("/host/" + hostId)) {
                        throw new ValidateException("Không được subcribe tới channel này");
                    }
                    break;
            }

        }

        return message;
    }
}
