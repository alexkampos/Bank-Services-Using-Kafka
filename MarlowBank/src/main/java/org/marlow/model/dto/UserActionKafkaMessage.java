package org.marlow.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marlow.model.enums.UserAction;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserActionKafkaMessage {

    private String userEmail;

    private UserAction userAction;

    private BigDecimal amount;

}
