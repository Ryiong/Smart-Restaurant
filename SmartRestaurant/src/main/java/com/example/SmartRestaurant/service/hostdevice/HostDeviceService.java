package com.example.SmartRestaurant.service.hostdevice;

import com.example.SmartRestaurant.dto.request.HostDeviceRequest;
import com.example.SmartRestaurant.dto.response.HostDeviceResponse;

public interface HostDeviceService {

    HostDeviceResponse create(Long shopId, HostDeviceRequest request);

}
