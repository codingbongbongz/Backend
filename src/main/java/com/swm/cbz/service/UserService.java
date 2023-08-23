package com.swm.cbz.service;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.controller.exception.NotFoundException;
import com.swm.cbz.domain.Country;
import com.swm.cbz.domain.UserVideo;
import com.swm.cbz.domain.Users;
import com.swm.cbz.domain.Video;
import com.swm.cbz.dto.UserDTO;
import com.swm.cbz.dto.UserVideoResponseDTO;
import com.swm.cbz.repository.CountryRepository;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.UserVideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.swm.cbz.common.response.ErrorMessage.NOT_FOUND_USER_EXCEPTION;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final UserVideoRepository userVideoRepository;

    private final CountryRepository countryRepository;
    public UserService(UserRepository userRepository, S3Service s3Service, UserVideoRepository userVideoRepository, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
        this.userVideoRepository = userVideoRepository;
        this.countryRepository = countryRepository;
    }
    
    public List<Video> getVideosByUserId(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_EXCEPTION));

        List<UserVideo> userVideos = userVideoRepository.findByUsers(user);
        List<Video> videos = userVideos.stream()
                .map(UserVideo::getVideo)
                .collect(Collectors.toList());
        UserVideoResponseDTO responseDTO = new UserVideoResponseDTO();
        return videos;
    }


    public ApiResponse<Users> updateUserProfile(Long userId, UserDTO userDTO) {
        try {
            Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

            if (userDTO.getName() != null) {
                user.setName(userDTO.getName());
            }

            if (userDTO.getPassword() != null) {
                user.setPassword(userDTO.getPassword());
            }

            if (userDTO.getEmail() != null) {
                user.setEmail(userDTO.getEmail());
            }

            if (userDTO.getNickname() != null) {
                user.setNickname(userDTO.getNickname());
            }

            if (userDTO.getCountryId() != null) {
                Optional<Country> countryOptional = countryRepository.findById(userDTO.getCountryId());
                user.setCountry(countryOptional.orElse(null));
            }

            if (userDTO.getSocial() != null) {
                user.setSocial(userDTO.getSocial());
            }

            if(userDTO.getIntroduce() != null){
                user.setIntroduce(userDTO.getIntroduce());
            }

            MultipartFile profileImage = userDTO.getProfileImage();
            if(profileImage != null && !profileImage.isEmpty()) {
                String imageUrl = s3Service.uploadProfileImage(profileImage, userId);
                user.setProfileImageUrl(imageUrl);
            }

            userRepository.save(user);

            return ApiResponse.success(SuccessMessage.UPDATE_USER_SUCCESS, user);

        } catch (EntityNotFoundException e) {
            return ApiResponse.error(NOT_FOUND_USER_EXCEPTION, e.getMessage());

        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ApiResponse deleteUser(Long userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return ApiResponse.error(NOT_FOUND_USER_EXCEPTION, "User not found with ID: " + userId);
            }

            userRepository.deleteById(userId);

            return ApiResponse.success(SuccessMessage.DELETE_USER_SUCCESS, null);

        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ApiResponse<List<Users>> getAllUsers() {
        try {
            List<Users> users = userRepository.findAll();

            if (users.isEmpty()) {
                return ApiResponse.error(NOT_FOUND_USER_EXCEPTION, "No users available.");
            }

            return ApiResponse.success(SuccessMessage.GET_USER_LIST_SUCCESS, users);

        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public ApiResponse<Users> getUserById(Long userId) {
        try {
            Optional<Users> optionalUser = userRepository.findById(userId);

            return optionalUser.map(users -> ApiResponse.success(SuccessMessage.GET_USER_SUCCESS, users)).orElseGet(() -> ApiResponse.error(NOT_FOUND_USER_EXCEPTION, "User not found with ID: " + userId));

        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
