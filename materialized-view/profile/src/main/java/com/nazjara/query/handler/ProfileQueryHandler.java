package com.nazjara.query.handler;

import com.nazjara.dto.ProfileDto;
import com.nazjara.query.FindProfileQuery;
import com.nazjara.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileQueryHandler {

  private final IProfileService service;

  @QueryHandler
  public ProfileDto findProfile(FindProfileQuery query) {
    return service.fetchProfile(query.getMobileNumber());
  }
}
