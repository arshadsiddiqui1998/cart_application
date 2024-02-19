package com.osc.user.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
@AllArgsConstructor
@Builder
public class ValidateOtpDto {

    private String userId;
    private String otp;
}
