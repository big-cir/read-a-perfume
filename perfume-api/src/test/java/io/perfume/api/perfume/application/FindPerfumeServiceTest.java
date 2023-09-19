package io.perfume.api.perfume.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import io.perfume.api.brand.application.port.in.FindBrandUseCase;
import io.perfume.api.brand.application.port.in.dto.BrandForPerfumeResult;
import io.perfume.api.note.application.port.in.FindCategoryUseCase;
import io.perfume.api.note.application.port.in.dto.CategoryResult;
import io.perfume.api.perfume.application.port.in.dto.PerfumeResult;
import io.perfume.api.perfume.application.port.out.PerfumeQueryRepository;
import io.perfume.api.perfume.application.service.FindPerfumeService;
import io.perfume.api.perfume.domain.Concentration;
import io.perfume.api.perfume.domain.NotePyramidIds;
import io.perfume.api.perfume.domain.Perfume;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindPerfumeServiceTest {

  @InjectMocks
  private FindPerfumeService findPerfumeService;
  @Mock
  private PerfumeQueryRepository perfumeQueryRepository;
  @Mock
  private FindCategoryUseCase findCategoryUseCase;
  @Mock
  private FindBrandUseCase findBrandUseCase;

  @Test
  void findPerfumeById() {
    // given
    Perfume perfume = Perfume.builder()
        .name("테싯 오 드 퍼퓸")
        .story("마음을 차분하게 가라앉혀주고 우리 몸의 감각을 일깨워주는 흙내음과 시트러스 노트의 따뜻하고 생기 넘치는 블렌드")
        .concentration(Concentration.EAU_DE_PERFUME)
        .price(150000L)
        .capacity(50L)
        .brandId(1L)
        .categoryId(1L)
        .thumbnailId(1L)
        .notePyramidIds(NotePyramidIds.builder()
            .topNoteIds(List.of(1L))
            .middleNoteIds(List.of(2L))
            .baseNoteIds(List.of(3L))
            .build())
        .build();

    given(perfumeQueryRepository.findPerfumeById(1L)).willReturn(perfume);
    CategoryResult category = new CategoryResult(1L, "시트러스", "testurl.com", "# 상큼 # 청량 # 싱그러움");
    given(findCategoryUseCase.findCategoryById(anyLong()))
        .willReturn(category);
    BrandForPerfumeResult brand = new BrandForPerfumeResult("CHANEL");
    given(findBrandUseCase.findBrandForPerfume(anyLong())).willReturn(brand);
    // when
    PerfumeResult perfumeResult = findPerfumeService.findPerfumeById(1L);

    // then
    assertEquals(perfume.getName(), perfumeResult.name());
    assertEquals(perfume.getStory(), perfumeResult.story());
    assertEquals(perfume.getConcentration(), perfumeResult.concentration());
    assertEquals(perfume.getPrice(), perfumeResult.price());
    assertEquals(perfume.getCapacity(), perfumeResult.capacity());
    assertEquals(brand.name(), perfumeResult.brandName());
    assertEquals(category.name(), perfumeResult.categoryName());
    assertEquals(category.description(), perfumeResult.categoryDescription());
  }
}
