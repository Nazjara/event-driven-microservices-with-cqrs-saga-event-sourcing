package com.nazjara.service;

import com.nazjara.dto.ProfileDto;
import com.nazjara.entity.Profile;
import com.nazjara.event.AccountDataChangedEvent;
import com.nazjara.event.CustomerDataChangedEvent;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.ProfileMapper;
import com.nazjara.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements IProfileService {

  private ProfileRepository profileRepository;

  @Override
  public ProfileDto fetchProfile(String mobileNumber) {
    var profile = profileRepository.findByMobileNumberAndActiveSw(mobileNumber, true)
        .orElseThrow(
            () -> new ResourceNotFoundException("Profile", "mobileNumber", mobileNumber)
        );

    return ProfileMapper.mapToProfileDto(profile);
  }

  @Override
  public void handleCustomerDataChangedEvent(CustomerDataChangedEvent event) {
    var profile = profileRepository.findByMobileNumberAndActiveSw(event.getMobileNumber(), true)
        .orElseGet(Profile::new);

    profile.setMobileNumber(event.getMobileNumber());

    if (profile.getName() != null) {
      profile.setName(event.getName());
    }

    profile.setActiveSw(event.isActiveSw());
    profileRepository.save(profile);
  }

  @Override
  public void handleAccountDataChangedEvent(AccountDataChangedEvent event) {
    var profile = profileRepository.findByMobileNumberAndActiveSw(event.getMobileNumber(), true)
        .orElseThrow(() -> new ResourceNotFoundException("Profile", "mobileNumber", event.getMobileNumber()));

    profile.setMobileNumber(event.getMobileNumber());
    profile.setAccountNumber(event.getAccountNumber());
    profileRepository.save(profile);
  }
}
