package com.nazjara.mapper;

import com.nazjara.dto.ProfileDto;
import com.nazjara.entity.Profile;

public class ProfileMapper {

  public static ProfileDto mapToProfileDto(Profile profile) {
    var profileDto = new ProfileDto();
    profileDto.setName(profile.getName());
    profileDto.setMobileNumber(profile.getMobileNumber());
    profileDto.setAccountNumber(profile.getAccountNumber());
    profileDto.setCardNumber(profile.isCardNumber());
    profileDto.setLoanNumber(profile.getLoanNumber());
    return profileDto;
  }
}
