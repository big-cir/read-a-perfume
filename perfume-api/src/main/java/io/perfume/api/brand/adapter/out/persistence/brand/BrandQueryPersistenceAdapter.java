package io.perfume.api.brand.adapter.out.persistence.brand;

import static io.perfume.api.brand.adapter.out.persistence.brand.QBrandEntity.brandEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.perfume.api.base.PersistenceAdapter;
import io.perfume.api.brand.application.port.out.BrandQueryRepository;
import io.perfume.api.brand.domain.Brand;
import io.perfume.api.common.page.CustomPage;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
public class BrandQueryPersistenceAdapter implements BrandQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final BrandMapper brandMapper;

  @Override
  public Optional<Brand> findBrandById(Long id) {
    BrandEntity result =
        jpaQueryFactory
            .selectFrom(brandEntity)
            .where(brandEntity.id.eq(id), brandEntity.deletedAt.isNull())
            .fetchOne();

    return Optional.ofNullable(brandMapper.toDomain(result));
  }

  @Override
  public List<Brand> findAll() {
    List<BrandEntity> result =
        jpaQueryFactory.selectFrom(brandEntity).where(brandEntity.deletedAt.isNull()).fetch();

    return result.stream().map(brandMapper::toDomain).toList();
  }

  @Override
  public CustomPage<Brand> findByPage(final Pageable pageable) {
    List<Brand> fetchBrands = findFetchBrands(pageable);
    final Long totalBrandCount = countBrands();
    long totalBrands = 0L;

    if (!Objects.equals(totalBrandCount, null)) totalBrands = totalBrandCount;

    return new CustomPage<>(new PageImpl<>(fetchBrands, pageable, totalBrands));
  }

  private List<Brand> findFetchBrands(final Pageable pageable) {
    return jpaQueryFactory
        .selectFrom(brandEntity)
        .where(brandEntity.deletedAt.isNull())
        .orderBy(brandEntity.id.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch()
        .stream()
        .map(brandMapper::toDomain)
        .toList();
  }

  private Long countBrands() {
    return jpaQueryFactory
        .select(brandEntity.count())
        .from(brandEntity)
        .where(brandEntity.deletedAt.isNull())
        .fetchOne();
  }
}
