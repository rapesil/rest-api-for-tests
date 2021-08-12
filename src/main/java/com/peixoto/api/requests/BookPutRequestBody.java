package com.peixoto.api.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class BookPutRequestBody {

    private Long id;
    private String title;
    private String author;
    private String category;
}
