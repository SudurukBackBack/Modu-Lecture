package com.sudurukbackback.modulecture.domain.admin.controller;

import com.sudurukbackback.modulecture.domain.admin.dto.request.AdminRegisterDto;
import com.sudurukbackback.modulecture.domain.admin.dto.response.UserListDto;
import com.sudurukbackback.modulecture.domain.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/code")
    public ResponseEntity<?> generateCode() {
        String code = adminService.generateCode();
        return ResponseEntity.ok().body(code);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(
            @Valid @RequestBody AdminRegisterDto request
    ) {
        adminService.registerAdmin(request);

        return ResponseEntity.ok("Admin Registration Success");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user-list")
    public ResponseEntity<Page<UserListDto>> getUserList(
            @RequestParam(value = "keyword", required = false) String keyword,
            final Pageable pageable
    ) {
        Page<UserListDto> userList = adminService.getUserList(keyword, pageable);

        return ResponseEntity.ok().body(userList);
    }
}

