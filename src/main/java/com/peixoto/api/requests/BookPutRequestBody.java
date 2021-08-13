package com.peixoto.api.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class BookPutRequestBody {

    private Long id;
    private String title;
    private String author;
    private String category;
}
