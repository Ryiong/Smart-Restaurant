package com.example.SmartRestaurant.service.workschedule;

import com.example.SmartRestaurant.common.enums.EmploymentStatus;
import com.example.SmartRestaurant.common.enums.ShiftStatus;
import com.example.SmartRestaurant.common.enums.WorkScheduleAttendanceStatus;
import com.example.SmartRestaurant.common.enums.WorkScheduleStatus;
import com.example.SmartRestaurant.dto.request.WorkScheduleActionRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleExplainRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleRequest;
import com.example.SmartRestaurant.dto.request.WorkScheduleUpdateRequest;
import com.example.SmartRestaurant.dto.response.WorkScheduleAttendanceResponse;
import com.example.SmartRestaurant.dto.response.WorkScheduleResponse;
import com.example.SmartRestaurant.entity.EmploymentEntity;
import com.example.SmartRestaurant.entity.ShiftEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.entity.WorkScheduleEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.WorkScheduleMapper;
import com.example.SmartRestaurant.repository.EmploymentRepository;
import com.example.SmartRestaurant.repository.ShiftRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.repository.WorkScheduleRepository;
import com.example.SmartRestaurant.security.CurrentUserProvider;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import com.example.SmartRestaurant.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.SmartRestaurant.validator.WorkScheduleValidator.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class WorkScheduleServiceImplement implements WorkScheduleService {
    WorkScheduleRepository repository;

    ShopRepository shopRepository;

    EmploymentRepository employmentRepository;
    ShiftRepository shiftRepository;
    AuthorizationService authorizationService;

    WorkScheduleMapper mapper;
    CurrentUserProvider currentUserProvider;

    @Override
    public List<WorkScheduleResponse> create(Long shopId, LocalDate date, List<WorkScheduleRequest> requests) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_WORK_SCHEDULE_CREATE");
        requests.forEach(x -> validateRequest(x));
        Set<Long> employmentIds = requests
                .stream()
                .map(x -> x.getEmploymentId())
                .collect(Collectors.toSet());
        //kiểm tra employmentId có bị trùng trong request không
        if (employmentIds.size() != requests.size()) {
            throw new ValidateException("Có nhân viên bị phân công nhiều lần trong ngày");
        }
        Set<EmploymentEntity> employments =
                employmentRepository.findAllByIdInAndStatusAndShopId(
                        employmentIds,
                        EmploymentStatus.ACTIVE,
                        shopId);
        if (employments.size() != employmentIds.size()) {
            throw new NotFoundException("Quan hệ nhân viên-cửa hàng");
        }
        Map<Long, EmploymentEntity> employmentMap = new HashMap<>();
        employments.forEach(x -> {
            employmentMap.put(x.getId(), x);
        });
        Set<Long> shiftIds = requests
                .stream()
                .map(x -> x.getShiftId())
                .collect(Collectors.toSet());
        Set<ShiftEntity> shifts =
                shiftRepository.findAllByIdInAndStatusAndShopId(
                        shiftIds,
                        ShiftStatus.ACTIVE,
                        shopId);
        if (shifts.size() != shiftIds.size()) {
            throw new NotFoundException("Ca làm");
        }
        Map<Long, ShiftEntity> shiftMap = new HashMap<>();
        shifts.forEach(x -> {
            shiftMap.put(x.getId(), x);
        });
        Set<WorkScheduleEntity> workSchedulesInDay =
                repository.findAllByDateAndEmployment_Shop_IdAndStatus(date, shopId, WorkScheduleStatus.SCHEDULED);
        Set<WorkScheduleEntity> workSchedulesInLastDay =
                repository.findAllByDateAndEmployment_Shop_IdAndStatus(
                        date.minusDays(1),
                        shopId,
                        WorkScheduleStatus.SCHEDULED);
        Set<WorkScheduleEntity> workSchedulesInNextDay =
                repository.findAllByDateAndEmployment_Shop_IdAndStatus(
                        date.plusDays(1),
                        shopId,
                        WorkScheduleStatus.SCHEDULED);
        for (WorkScheduleRequest request : requests) {
            //tìm ra các lịch làm của nhân viên này
            List<WorkScheduleEntity> workScheduleInDayByEmployment = workSchedulesInDay
                    .stream()
                    .filter(x -> x.getEmployment().getId().equals(request.getEmploymentId()))
                    .collect(Collectors.toList());
            if (!workScheduleInDayByEmployment.isEmpty()) {
                throw new ValidateException("Nhân viên "
                        + employmentMap.get(request.getEmploymentId()).getUser().getName()
                        + " đã có ca làm vào ngày "
                        + date
                );
            }
            //tìm ra các lịch làm của nhân viên vào ngày trước đó
            List<WorkScheduleEntity> workScheduleInLastDayByEmployment = workSchedulesInLastDay
                    .stream()
                    .filter(x -> x.getEmployment().getId().equals(request.getEmploymentId()))
                    .collect(Collectors.toList());
            if (!workScheduleInLastDayByEmployment.isEmpty()) {
                //kiểm tra xem ca cũ đã kết thúc trước khi ca mới bắt đầu chưa
                LocalDateTime oldEndTime = DateTimeUtils.getEndDateTime(date.minusDays(1),
                        workScheduleInLastDayByEmployment.get(0).getShift().getStartTime(),
                        workScheduleInLastDayByEmployment.get(0).getShift().getEndTime());
                LocalDateTime newStartTime = DateTimeUtils.getStartDateTime(date,
                        shiftMap.get(request.getShiftId()).getStartTime());
                if (oldEndTime.isAfter(newStartTime)) {
                    throw new ValidateException("Ca làm của ngày "
                            + date.minusDays(1)
                            + " chưa kết thúc để bắt đầu ca "
                            + shiftMap.get(request.getShiftId()).getStartTime()
                            + "-"
                            + shiftMap.get(request.getShiftId()).getEndTime()
                    );
                }
            }
            //tìm ra các lịch làm việc vào ngày kế tiếp của nhân viên
            List<WorkScheduleEntity> workScheduleInNextDayByEmployment = workSchedulesInNextDay
                    .stream()
                    .filter(x -> x.getEmployment().getId().equals(request.getEmploymentId()))
                    .collect(Collectors.toList());
            if (!workScheduleInNextDayByEmployment.isEmpty()) {
                //kiểm tra xem ca cũ đã kết thúc trước khi ca mới bắt đầu chưa
                LocalDateTime newEndTime = DateTimeUtils.getEndDateTime(date,
                        shiftMap.get(request.getShiftId()).getStartTime(),
                        shiftMap.get(request.getShiftId()).getEndTime());
                LocalDateTime nextStartTime = DateTimeUtils.getStartDateTime(date.plusDays(1),
                        workScheduleInNextDayByEmployment.get(0).getShift().getStartTime());
                if (newEndTime.isAfter(nextStartTime)) {
                    throw new ValidateException("Ca làm của ngày "
                            + date.plusDays(1)
                            + " đã bắt đầu trước ca "
                            + shiftMap.get(request.getShiftId()).getStartTime()
                            + "-"
                            + shiftMap.get(request.getShiftId()).getEndTime()
                            + " kết thúc"
                    );
                }
            }
        }
        //thỏa mãn hết thì mới lưu db
        List<WorkScheduleEntity> onSaveWorkScheduleEntity = new ArrayList<>();
        for (WorkScheduleRequest request : requests) {
            WorkScheduleEntity workSchedule = mapper.toEntity(request);
            workSchedule.setEmployment(employmentMap.get(request.getEmploymentId()));
            workSchedule.setShift(shiftMap.get(request.getShiftId()));
            workSchedule.setStatus(WorkScheduleStatus.SCHEDULED);
            onSaveWorkScheduleEntity.add(workSchedule);
        }
        return repository.saveAll(onSaveWorkScheduleEntity)
                .stream()
                .map(x -> mapper.toResponse(x))
                .collect(Collectors.toList());
    }

    @Override
    public WorkScheduleResponse update(Long shopId, Long workScheduleId, WorkScheduleUpdateRequest request) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_WORK_SCHEDULE_UPDATE");
        validateUpdateRequest(request);
        WorkScheduleEntity workSchedule = repository.findByIdAndAndEmployment_Shop_IdAndStatus(
                workScheduleId,
                shopId,
                WorkScheduleStatus.SCHEDULED
        );
        if (workSchedule == null) {
            throw new NotFoundException("Lịch làm việc");
        }
        ShiftEntity newShift = shiftRepository.findByIdAndShopIdAndStatus(
                request.getShiftId(),
                shopId,
                ShiftStatus.ACTIVE
        );
        if (newShift == null) {
            throw new NotFoundException("Ca làm");
        }
        Set<WorkScheduleEntity> workScheduleInLastDay = repository.findAllByDateAndEmployment_Shop_IdAndStatus(
                workSchedule.getDate().minusDays(1),
                shopId,
                WorkScheduleStatus.SCHEDULED
        );
        Set<WorkScheduleEntity> workScheduleInNextDay = repository.findAllByDateAndEmployment_Shop_IdAndStatus(
                workSchedule.getDate().plusDays(1),
                shopId,
                WorkScheduleStatus.SCHEDULED
        );
        if (!workScheduleInLastDay.isEmpty()) {
            List<WorkScheduleEntity> workScheduleInLastDayByEmployment = workScheduleInLastDay
                    .stream()
                    .filter(x -> x.getEmployment().getId().equals(workSchedule.getEmployment().getId()))
                    .collect(Collectors.toList());
            if (!workScheduleInLastDayByEmployment.isEmpty()) {
                ShiftEntity lastDayShift = workScheduleInLastDayByEmployment.get(0).getShift();
                LocalDateTime oldEndTime = DateTimeUtils.getEndDateTime(
                        workSchedule.getDate().minusDays(1),
                        lastDayShift.getStartTime(),
                        lastDayShift.getEndTime());
                LocalDateTime newStartTime = DateTimeUtils.getStartDateTime(
                        workSchedule.getDate(),
                        newShift.getStartTime());
                if (oldEndTime.isAfter(newStartTime)) {
                    throw new ValidateException("Ca làm của ngày "
                            + workSchedule.getDate().minusDays(1)
                            + " chưa kết thúc để bắt đầu ca "
                            + newShift.getStartTime()
                            + "-"
                            + newShift.getEndTime()
                    );
                }
            }
        }
        if (!workScheduleInNextDay.isEmpty()) {
            List<WorkScheduleEntity> workScheduleInNextDayByEmployment = workScheduleInNextDay
                    .stream()
                    .filter(x -> x.getEmployment().getId().equals(workSchedule.getEmployment().getId()))
                    .collect(Collectors.toList());
            if (!workScheduleInNextDayByEmployment.isEmpty()) {
                ShiftEntity nextDayShift = workScheduleInNextDayByEmployment.get(0).getShift();
                LocalDateTime newEndTime = DateTimeUtils.getEndDateTime(
                        workSchedule.getDate(),
                        newShift.getStartTime(),
                        newShift.getEndTime());
                LocalDateTime nextStartTime = DateTimeUtils.getStartDateTime(
                        workSchedule.getDate().plusDays(1),
                        nextDayShift.getStartTime());
                if (newEndTime.isAfter(nextStartTime)) {
                    throw new ValidateException("Ca làm của ngày "
                            + workSchedule.getDate().plusDays(1)
                            + " đã bắt đầu trước ca "
                            + newShift.getStartTime()
                            + "-"
                            + newShift.getEndTime()
                            + " kết thúc"
                    );
                }
            }

        }
        //thoả mãn hết sẽ cho update
        workSchedule.setShift(newShift);
        return mapper.toResponse(repository.save(workSchedule));
    }

    @Override
    public void delete(Long workScheduleId) {
        WorkScheduleEntity workSchedule = repository.findById(workScheduleId)
                .orElseThrow(() -> new NotFoundException("Ca làm việc"));
        authorizationService.checkOwnerOrPermissionInShop(workSchedule.getShift().getShop()
                , "PERM_WORK_SCHEDULE_DELETE");
        if (workSchedule.getStatus() == WorkScheduleStatus.CANCELLED) {
            throw new InvalidStatusException("ca làm viêc");
        }
        workSchedule.setStatus(WorkScheduleStatus.CANCELLED);
        repository.save(workSchedule);
    }

    @Override
    public Set<WorkScheduleResponse> getByShopIdAndDate(Long shopId, LocalDate date) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_WORK_SCHEDULE_VIEW");
        Set<WorkScheduleEntity> workSchedules = repository.findAllByDateAndEmployment_Shop_IdAndStatus(
                date,
                shopId,
                WorkScheduleStatus.SCHEDULED);
        return workSchedules.stream()
                .map(x -> mapper.toResponse(x))
                .collect(Collectors.toSet());
    }

    @Override
    public List<WorkScheduleAttendanceResponse> getAllByShopIdAndEmploymentIdFromDateToDate(Long shopId,
                                                                                            Long employmentId,
                                                                                            LocalDate startDate,
                                                                                            LocalDate endDate) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_WORK_SCHEDULE_ATTENDANCE_VIEW");
        EmploymentEntity employment = employmentRepository.findByIdAndShopId(employmentId, shopId)
                .orElseThrow((() -> new NotFoundException("Nhân viên không tìm thấy trong cửa hàng")));
        List<WorkScheduleEntity> workSchedules = repository.findAllByEmploymentIdFromDateToDate(employmentId,
                startDate,
                endDate);
        return workSchedules.stream().map(x -> mapper.toAttendancResponse(x)).collect(Collectors.toList());
    }

    @Override
    public List<WorkScheduleAttendanceResponse> updateAttendanceStatus(Long shopId,
                                                                       Long employmentId,
                                                                       List<WorkScheduleActionRequest> requests) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_WORK_SCHEDULE_UPDATE_STATUS");
        EmploymentEntity employment = employmentRepository.findById(employmentId)
                .orElseThrow(() -> new NotFoundException("Quan hệ nhân viên-cửa hàng"));
        if (!employment.getShop().getId().equals(shopId)) {
            throw new ValidateException("Nhân viên không tồn tại trong cửa hàng");
        }
        requests.forEach(x -> validateWorkScheduleActionRequest(x));
        Set<Long> workScheduleRequestIds = requests.stream().map(x -> x.getId()).collect(Collectors.toSet());
        if (workScheduleRequestIds.size() != requests.size()) {
            throw new ValidateException("Có công bị trùng");
        }
        List<WorkScheduleEntity> workSchedules = repository.findAllByIdInAndEmploymentId(workScheduleRequestIds,
                employmentId
        );
        if (workScheduleRequestIds.size() != workSchedules.size()) {
            throw new NotFoundException("Công");
        }
        Map<Long, WorkScheduleEntity> workScheduleMap = new HashMap<>();
        workSchedules.forEach(x -> {
            workScheduleMap.put(x.getId(), x);
        });
        LocalDateTime now = LocalDateTime.now();
        requests.forEach(x -> {
            WorkScheduleEntity workSchedule = workScheduleMap.get(x.getId());
            if (!workSchedule.getAttendanceStatus().equals(WorkScheduleAttendanceStatus.PENDING)) {
                throw new InvalidStatusException("công");
            }
            if (workSchedule.getAttendanceStatus().equals(x.getStatus())) {
                throw new InvalidStatusException("công");
            }
            if (x.getStatus().equals(WorkScheduleAttendanceStatus.APPROVED)) {
                boolean enoughAttendance =
                        workSchedule.getAttendances().size() == 2;

                boolean hasExplanation =
                        workSchedule.getExplaination() != null
                                && !workSchedule.getExplaination().isBlank();

                if (!enoughAttendance && !hasExplanation) {
                    throw new ValidateException(
                            "Công không hợp lệ và chưa có giải trình"
                    );
                }
            }
            if (x.getStatus().equals(WorkScheduleAttendanceStatus.REJECTED)) {
                workSchedule.setRejectReason(x.getRejectReason());
            }
            workSchedule.setAttendanceStatus(x.getStatus());
            workSchedule.setReviewedAt(now);
            workSchedule.setReviewedBy(currentUserProvider.getCurrentUser().getUser());
        });
        return repository.saveAll(workSchedules)
                .stream()
                .map(x -> mapper.toAttendancResponse(x))
                .collect(Collectors.toList());
    }

    @Override
    public void explain(Long shopId, Long workScheduleId, WorkScheduleExplainRequest explainRequest) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_WORK_SCHEDULE_EXPLAIN");
        WorkScheduleEntity workSchedule = repository.findById(workScheduleId)
                .orElseThrow(() -> new NotFoundException("Công"));
        EmploymentEntity employment = workSchedule.getEmployment();
        validateWorkScheduleExplainRequest(explainRequest);
        if (!employment.getUser().getId().equals(currentUserProvider.getCurrentUserId())) {
            throw new AccessDeniedException("Không phải chủ sở hữu công, không thể sửa");
        }
        if (!employment.getShop().getId().equals(shopId)) {
            throw new ValidateException("Công không thuộc cửa hàng");
        }
        if (workSchedule.getAttendanceStatus().equals(WorkScheduleAttendanceStatus.APPROVED)) {
            throw new InvalidStatusException("công");
        }
        workSchedule.setExplaination(explainRequest.getExplaination());
        workSchedule.setExplainedAt(LocalDateTime.now());
        workSchedule.setAttendanceStatus(WorkScheduleAttendanceStatus.PENDING);
        repository.save(workSchedule);
    }
}
