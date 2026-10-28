package com.example.SmartRestaurant.service.table;

import com.example.SmartRestaurant.common.enums.TableStatus;
import com.example.SmartRestaurant.dto.request.TableRequest;
import com.example.SmartRestaurant.dto.response.TableResponse;
import com.example.SmartRestaurant.entity.AreaEntity;
import com.example.SmartRestaurant.entity.TableEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.TableMapper;
import com.example.SmartRestaurant.repository.AreaRepository;
import com.example.SmartRestaurant.repository.TableRepository;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.SmartRestaurant.validator.TableValidator.validateTableRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class TableServiceImplement implements TableService {

    TableRepository repository;
    AuthorizationService authorizationService;
    TableMapper mapper;
    AreaRepository areaRepository;

    @Override
    public TableResponse create(TableRequest tableRequest, Long parentId) {
        AreaEntity area = areaRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Khu vực"));

        authorizationService.checkOwnerOrPermissionInShop(area.getShop(), "PERM_TABLE_CREATE");
        validateTableRequest(tableRequest);

        TableEntity table = mapper.toEntity(tableRequest);
        if (repository.existsByNameAndAreaId(table.getName(), parentId)) {
            throw new ValidateException("Tên bàn đã tồn tại trong khu vực");
        }
        table.setArea(area);
        table.setStatus(TableStatus.ACTIVE);
        return mapper.toResponse(repository.save(table));
    }

    @Override
    public TableResponse update(Long id, TableRequest tableRequest) {
        TableEntity table = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bàn"));

        authorizationService.checkOwnerOrPermissionInShop(table.getArea().getShop(), "PERM_TABLE_UPDATE");
        validateTableRequest(tableRequest);
        TableEntity newTable = mapper.toEntity(tableRequest);
        if (repository.existsByNameAndAreaIdAndIdNot(
                newTable.getName()
                , table.getArea().getId()
                , table.getId())) {
            throw new ValidateException("Tên bàn đã tồn tại trong khu vực");
        }
        table.setName(newTable.getName());
        table.setQrEnabled(newTable.isQrEnabled());
        return mapper.toResponse(repository.save(table));
    }

    @Override
    public void delete(Long id) {
        TableEntity table = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bàn"));

        authorizationService.checkOwnerOrPermissionInShop(table.getArea().getShop(), "PERM_TABLE_DELETE");
        if (table.getStatus() != TableStatus.ACTIVE) {
            throw new InvalidStatusException("bàn");
        }
        table.setStatus(TableStatus.INACTIVE);
        repository.save(table);
    }

    @Override
    public TableResponse getById(Long id) {
        TableEntity table = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bàn"));

        authorizationService.checkOwnerOrPermissionInShop(table.getArea().getShop(), "PERM_TABLE_VIEW");
        return mapper.toResponse(table);
    }

    @Override
    public Page<TableResponse> getAllByAreaId(Long areaId, TableStatus status, Pageable pageable) {
        AreaEntity area = areaRepository.findById(areaId)
                .orElseThrow(() -> new NotFoundException("Khu vực"));

        authorizationService.checkOwnerOrPermissionInShop(area.getShop(), "PERM_TABLE_VIEW");
        Page<TableEntity> tables = status == null
                ? repository.findAllByAreaId(areaId, pageable)
                : repository.findAllByAreaIdAndStatus(areaId, status, pageable);
        return tables.map(x -> mapper.toResponse(x));
    }

    @Override
    public void activate(Long tableId) {
        TableEntity table = repository.findById(tableId)
                .orElseThrow(() -> new NotFoundException("Bàn"));

        authorizationService.checkOwnerOrPermissionInShop(table.getArea().getShop(), "PERM_TABLE_ACTIVATE");
        if (table.getStatus() != TableStatus.INACTIVE) {
            throw new InvalidStatusException("bàn");
        }
        table.setStatus(TableStatus.ACTIVE);
        repository.save(table);
    }
}
