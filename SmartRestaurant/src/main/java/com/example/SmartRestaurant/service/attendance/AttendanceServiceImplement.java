package com.example.SmartRestaurant.service.attendance;

import com.example.SmartRestaurant.common.enums.AttendanceType;
import com.example.SmartRestaurant.common.enums.WorkScheduleStatus;
import com.example.SmartRestaurant.dto.request.AttendanceRequest;
import com.example.SmartRestaurant.dto.response.AttendanceResponse;
import com.example.SmartRestaurant.entity.AttendanceEntity;
import com.example.SmartRestaurant.entity.ShopEntity;
import com.example.SmartRestaurant.entity.WorkScheduleEntity;
import com.example.SmartRestaurant.exception.InvalidStatusException;
import com.example.SmartRestaurant.exception.NotFoundException;
import com.example.SmartRestaurant.exception.ValidateException;
import com.example.SmartRestaurant.mapper.AttendanceMapper;
import com.example.SmartRestaurant.repository.AttendanceRepository;
import com.example.SmartRestaurant.repository.ShopRepository;
import com.example.SmartRestaurant.repository.WorkScheduleRepository;
import com.example.SmartRestaurant.security.CurrentUserProvider;
import com.example.SmartRestaurant.service.authorization.AuthorizationService;
import com.example.SmartRestaurant.util.DateTimeUtils;
import com.example.SmartRestaurant.util.GeoUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

import static com.example.SmartRestaurant.validator.AttendanceValidator.validateRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class AttendanceServiceImplement implements AttendanceService {
    AttendanceRepository repository;
    ShopRepository shopRepository;
    WorkScheduleRepository workScheduleRepository;
    AuthorizationService authorizationService;
    CurrentUserProvider currentUserProvider;

    AttendanceMapper mapper;

    @Override
    public AttendanceResponse checkAttendance(AttendanceRequest request) {
        validateRequest(request);
        ShopEntity shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new NotFoundException("Cửa hàng"));
        //kiểm tra có phải nhân viên shop không
        authorizationService.checkOwnerOrPermissionInShop(shop, "PERM_CHECK_ATTENDANCE");
        WorkScheduleEntity workSchedule = workScheduleRepository.findById(request.getWorkScheduleId())
                .orElseThrow(() -> new NotFoundException("Ca làm việc"));
        //kiểm tra lịch làm việc có còn hoạt động không
        if (!workSchedule.getStatus().equals(WorkScheduleStatus.SCHEDULED)) {
            throw new InvalidStatusException("lich làm việc");
        }
        //kiểm tra lịch làm việc của thuộc shop không
        if (!workSchedule.getShift().getShop().getId().equals(request.getShopId())) {
            throw new ValidateException("Lịch làm việc không thuộc cửa hàng");
        }
        //kiểm tra hành động chấm công này có hợp lệ để chấm không trước ca hoặc sau ca 30 phút
        LocalDateTime startDateTime =
                DateTimeUtils.getStartDateTime(workSchedule.getDate(),
                        workSchedule.getShift().getStartTime());
        LocalDateTime endDateTime =
                DateTimeUtils.getEndDateTime(workSchedule.getDate(),
                        workSchedule.getShift().getStartTime(),
                        workSchedule.getShift().getEndTime());
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDateTime.minusMinutes(30)) || now.isAfter(endDateTime.plusMinutes(30))) {
            throw new ValidateException("Không thể thực hiện chấm công do ngoài khoảng thời gian cho phép");
        }
        //kiểm tra có được phân công ca làm việc này không
        if (!workSchedule.getEmployment().getUser().getId().equals(currentUserProvider.getCurrentUserId())) {
            throw new ValidateException("Nhân viên không thuộc ca làm việc này");
        }
        AttendanceEntity attendance = mapper.toEntity(request);
        //kiểm tra khoảng cách với địa chỉ quán quy định
        Double distance = GeoUtils.calculateDistance(
                shop.getLatitude(),
                shop.getLongitude(),
                attendance.getLatitude(),
                attendance.getLongitude()
        );
        if (distance > shop.getAttendanceRadius()) {
            throw new ValidateException("Không thể thực hiện chấm công ở khoảng cách này, vui lòng lại gần quán hơn");
        }
        //kiểm tra có lượt chấm công nào chưa
        Set<AttendanceEntity> attendancesInWorkSchedule =
                repository.findAllByWorkScheduleId(request.getWorkScheduleId());
        //kiểm tra chưa có lượt nào thì chỉ cho checkIn
        if (attendancesInWorkSchedule.size() == 0) {
            attendance.setType(AttendanceType.CHECK_IN);
        }
        //có 1 thì chỉ cho checkout
        else if (attendancesInWorkSchedule.size() == 1) {
            attendance.setType(AttendanceType.CHECK_OUT);
        }
        //kiểm tra rule có đủ 2 lượt check in-out chưa
        else {
            throw new ValidateException("Hành động không khả dụng, đã chấm công ra về");
        }
        //kiểm tra qr còn hạn không
        if (LocalDateTime.now().isAfter(request.getQrExpiredAt())) {
            throw new ValidateException("Qr đã hết hạn sử dụng");
        }
        attendance.setWorkSchedule(workSchedule);

        return mapper.toResponse(repository.save(attendance));
    }


}
