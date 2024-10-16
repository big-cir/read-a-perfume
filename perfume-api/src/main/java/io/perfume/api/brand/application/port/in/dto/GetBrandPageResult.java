package io.perfume.api.brand.application.port.in.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.perfume.api.brand.domain.Brand;
import java.time.LocalDateTime;

public record GetBrandPageResult(
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime createdAt,

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime updatedAt,

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime deleteAt,
    Long id,
    String name,
    String story,
    String brandUrl,
    Long thumbnailId
) {

    public static GetBrandPageResult from(Brand brand) {
        return new GetBrandPageResult(
            brand.getCreatedAt(),
            brand.getUpdatedAt(),
            brand.getDeletedAt(),
            brand.getId(),
            brand.getName(),
            brand.getStory(),
            brand.getBrandUrl(),
            brand.getThumbnailId()
        );
    }
}
