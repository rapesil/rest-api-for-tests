package com.peixoto.api.dto;

import lombok.Data;
import lombok.Generated;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Generated
public class UserDto {
    private String username;

    private String authhorities;
}
