package com.example.SmartRestaurant.service.permisison;

import com.example.SmartRestaurant.dto.response.PermissionResponse;
import com.example.SmartRestaurant.mapper.PermissionMapper;
import com.example.SmartRestaurant.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class PermissionServiceImplement implements PermissionService {
    PermissionRepository repository;
    PermissionMapper mapper;

    @Override
    public List<PermissionResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(x -> mapper.toResponse(x))
                .collect(Collectors.toList());
    }
}
