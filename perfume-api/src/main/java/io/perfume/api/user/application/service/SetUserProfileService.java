package io.perfume.api.user.application.service;

import io.perfume.api.file.application.port.in.dto.SaveFileResult;
import io.perfume.api.file.application.service.SaveFileService;
import io.perfume.api.user.application.exception.NotFoundUserException;
import io.perfume.api.user.application.port.in.SetUserProfileUseCase;
import io.perfume.api.user.application.port.out.UserQueryRepository;
import io.perfume.api.user.application.port.out.UserRepository;
import io.perfume.api.user.domain.User;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class SetUserProfileService implements SetUserProfileUseCase {

  private final UserQueryRepository userQueryRepository;
  private final UserRepository userRepository;
  private final SaveFileService fileUploadService;

  public SetUserProfileService(
      UserQueryRepository userQueryRepository,
      UserRepository userRepository,
      SaveFileService fileUploadService) {
    this.userQueryRepository = userQueryRepository;
    this.userRepository = userRepository;
    this.fileUploadService = fileUploadService;
  }

  @Override
  public void setUserProfilePicture(String userId, MultipartFile image, LocalDateTime now) {
    if (userId == null) {
      new NotFoundUserException();
    } else {
      SaveFileResult saveFileResult =
          fileUploadService.singleFileUpload(Long.parseLong(userId), image, now);
      User user = userQueryRepository.loadUser(Long.parseLong(userId)).get();
      user.updateThumbnailId(saveFileResult.fileId());
      userRepository.save(user);
    }
  }
}
