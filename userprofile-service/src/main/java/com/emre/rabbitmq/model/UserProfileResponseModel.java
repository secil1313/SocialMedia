package com.emre.rabbitmq.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseModel implements Serializable { //Post Service e dönecek veriler için.
    private String userId;
    private String username;
    private String avatar;
}
