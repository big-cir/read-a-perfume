package io.perfume.api.brand.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.perfume.api.brand.adapter.out.persistence.brand.BrandEntity;
import io.perfume.api.brand.adapter.out.persistence.brand.BrandMapper;
import io.perfume.api.brand.adapter.out.persistence.brand.BrandQueryPersistenceAdapter;
import io.perfume.api.brand.application.port.out.BrandQueryRepository;
import io.perfume.api.brand.domain.Brand;
import io.perfume.api.configuration.TestQueryDSLConfiguration;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Import({BrandQueryPersistenceAdapter.class, BrandMapper.class, TestQueryDSLConfiguration.class})
@DataJpaTest
public class BrandQueryPersistenceAdapterTest {

  @Autowired private EntityManager em;
  @Autowired private BrandQueryRepository brandQueryRepository;

  @Test
  @DisplayName("Brand 1건을 조회한다.")
  @Transactional
  public void findByBrandId() {
    // given
    String brandName = "brand_name1";
    String brandStory = "story1";
    Long thumbnailId = 1L;

    BrandEntity brandEntity =
        BrandEntity.builder().name(brandName).story(brandStory).thumbnailId(thumbnailId).build();
    em.persist(brandEntity);
    em.flush();
    em.clear();

    // when
    Optional<Brand> optionalBrand = brandQueryRepository.findBrandById(brandEntity.getId());
    Brand brand = optionalBrand.orElse(null);

    // then
    assertNotNull(brand);
    assertThat(brand)
        .extracting("id", "name", "story", "thumbnailId")
        .containsExactly(brandEntity.getId(), brandName, brandStory, thumbnailId);
  }

  @Test
  @DisplayName("제거된 Brand는 조회할 수 없다.")
  @Transactional
  public void findByBrandId_NotFound() {
    // given
    String brandName = "brand_name1";
    String brandStory = "story1";
    Long thumbnailId = 1L;

    BrandEntity brandEntity =
        BrandEntity.builder()
            .name(brandName)
            .story(brandStory)
            .thumbnailId(thumbnailId)
            .deletedAt(LocalDateTime.now())
            .build();
    em.persist(brandEntity);
    em.flush();
    em.clear();

    // when
    Optional<Brand> optionalBrand = brandQueryRepository.findBrandById(brandEntity.getId());

    // then
    assertFalse(optionalBrand.isPresent());
  }
}
