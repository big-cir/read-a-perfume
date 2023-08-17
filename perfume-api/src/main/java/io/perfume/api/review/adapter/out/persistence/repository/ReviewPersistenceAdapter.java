package io.perfume.api.review.adapter.out.persistence.repository;

import io.perfume.api.base.PersistenceAdapter;
import io.perfume.api.review.adapter.out.persistence.mapper.ReviewMapper;
import io.perfume.api.review.domain.Review;

@PersistenceAdapter
public class ReviewPersistenceAdapter {

  private final ReviewRepository reviewRepository;

  private final ReviewMapper reviewMapper;

  public ReviewPersistenceAdapter(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
    this.reviewRepository = reviewRepository;
    this.reviewMapper = reviewMapper;
  }

  public Review save(Review review) {
    var createdEntity = reviewRepository.save(reviewMapper.toEntity(review));

    return reviewMapper.toDomain(createdEntity);
  }
}
