package io.perfume.api.brand.adapter.in.http;

import io.perfume.api.brand.adapter.in.http.dto.BrandResponse;
import io.perfume.api.brand.adapter.in.http.dto.GetBrandResponseDto;
import io.perfume.api.brand.adapter.in.http.dto.GetBrandsRequestDto;
import io.perfume.api.brand.application.port.in.FindBrandUseCase;
import io.perfume.api.brand.application.port.in.dto.GetBrandPageResult;
import io.perfume.api.brand.application.port.in.dto.GetBrandResult;
import io.perfume.api.brand.application.port.in.dto.GetPaginatedBrandCommand;
import io.perfume.api.common.page.CustomPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/brands")
public class FindBrandController {

  private final FindBrandUseCase findBrandUseCase;

  @GetMapping("/{id}")
  public BrandResponse get(@PathVariable Long id) {
    return BrandResponse.of(findBrandUseCase.findBrandById(id));
  }

  @GetMapping
  public ResponseEntity<CustomPage<GetBrandPageResult>> getPageBrands(final GetBrandsRequestDto dto) {
    CustomPage<GetBrandPageResult> response = findBrandUseCase.getPaginatedPageBrands(
            new GetPaginatedBrandCommand(dto.getPage(), dto.getSize()));

    return ResponseEntity.ok(response);
  }
}
