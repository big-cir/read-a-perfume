package io.perfume.api.brand.application.port.out;

import io.perfume.api.brand.domain.Brand;
import io.perfume.api.common.page.CustomPage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;

public interface BrandQueryRepository {
  Optional<Brand> findBrandById(Long id);

  List<Brand> findAll();

  CustomPage<Brand> findByPage(final Pageable pageable);
}
