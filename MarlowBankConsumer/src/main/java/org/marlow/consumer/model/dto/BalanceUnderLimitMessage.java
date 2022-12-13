package org.marlow.consumer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceUnderLimitMessage extends KafkaMessage {

    private String userEmail;

}
