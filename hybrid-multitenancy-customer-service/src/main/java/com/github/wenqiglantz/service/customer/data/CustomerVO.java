package com.github.wenqiglantz.service.customer.data;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"customerId", "firstName", "lastName"})
public class CustomerVO {

    private String customerId;

    private String firstName;

    private String lastName;
}
