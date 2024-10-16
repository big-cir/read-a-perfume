package io.perfume.api.brand.adapter.in.http.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GetBrandsRequestDto {
        @Min(1)
        Integer page = 1;

        @Min(1)
        @Max(20)
        Integer size = 20;
}
