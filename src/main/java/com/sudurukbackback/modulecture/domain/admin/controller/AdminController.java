package com.sudurukbackback.modulecture.domain.admin.controller;

import com.sudurukbackback.modulecture.domain.admin.dto.request.AdminRegisterDto;
import com.sudurukbackback.modulecture.domain.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

