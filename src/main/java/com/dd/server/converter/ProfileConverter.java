package com.dd.server.converter;

import com.dd.server.domain.Profile;
import com.dd.server.dto.JoinDto;

public class ProfileConverter {
    public static Profile JoinDtoToProfile(JoinDto joinDto){
        Profile profile = new Profile();
        profile.setId(joinDto.getId());
        profile.setEmail(joinDto.getEmail());
        profile.setName(joinDto.getName());
        profile.setImage(null);
        return profile;
    }
}
