package io.perfume.api.review.adapter.out.persistence.entity;

import io.perfume.api.base.BaseTimeEntity;
import io.perfume.api.review.domain.type.DayType;
import io.perfume.api.review.domain.type.Strength;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "review")
@Getter
public class ReviewEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private String feeling;

  private String situation;

  private Strength strength;

  private Long duration;

  private DayType dayType;

  @Column(nullable = false)
  private Long perfumeId;

  @Column(nullable = false)
  private Long userId;

  protected ReviewEntity() {
  }

  public ReviewEntity(Long id, String feeling, String situation, Strength strength, Long duration,
                      DayType dayType, Long perfumeId, Long userId, LocalDateTime createdAt,
                      LocalDateTime updatedAt, LocalDateTime deletedAt) {
    super(createdAt, updatedAt, deletedAt);
    this.id = id;
    this.feeling = feeling;
    this.situation = situation;
    this.strength = strength;
    this.duration = duration;
    this.dayType = dayType;
    this.perfumeId = perfumeId;
    this.userId = userId;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy ?
        ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() :
        o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
        ((HibernateProxy) this).getHibernateLazyInitializer()
            .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    ReviewEntity that = (ReviewEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ?
        ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
        getClass().hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
        "id = " + id + ")";
  }
}
