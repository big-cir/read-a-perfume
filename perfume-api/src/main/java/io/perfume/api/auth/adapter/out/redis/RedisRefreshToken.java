package io.perfume.api.auth.adapter.out.redis;

import io.perfume.api.auth.domain.RefreshToken;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@RedisHash(value = "refresh", timeToLive = 1209600) // TODO: properties로 빼기
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RedisRefreshToken {
  @Id private UUID tokenId;
  private Long userId;

  private RedisRefreshToken(UUID tokenId, Long userId) {
    this.tokenId = tokenId;
    this.userId = userId;
  }

  public static RedisRefreshToken fromRefreshToken(RefreshToken refreshToken) {
    return new RedisRefreshToken(refreshToken.getTokenId(), refreshToken.getUserId());
  }
}
