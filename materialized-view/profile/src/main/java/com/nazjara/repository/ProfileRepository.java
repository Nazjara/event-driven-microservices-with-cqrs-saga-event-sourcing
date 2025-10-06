package com.nazjara.repository;

import com.nazjara.entity.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

  Optional<Profile> findByMobileNumberAndActiveSw(String mobileNumber, boolean active);
}
