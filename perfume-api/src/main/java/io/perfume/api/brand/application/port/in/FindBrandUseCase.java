package io.perfume.api.brand.application.port.in;

import io.perfume.api.brand.application.port.in.dto.BrandForPerfumeResult;
import io.perfume.api.brand.application.port.in.dto.BrandResult;
import io.perfume.api.brand.application.port.in.dto.GetBrandPageResult;
import io.perfume.api.brand.application.port.in.dto.GetPaginatedBrandCommand;
import io.perfume.api.common.page.CustomPage;

public interface FindBrandUseCase {
  BrandResult findBrandById(Long id);

  BrandForPerfumeResult findBrandForPerfume(Long id);

  CustomPage<GetBrandPageResult> getPaginatedPageBrands(GetPaginatedBrandCommand command);
}
