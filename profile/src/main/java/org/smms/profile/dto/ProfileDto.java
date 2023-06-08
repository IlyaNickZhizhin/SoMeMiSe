package org.smms.profile.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    
    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
    private Set<ProfileSummaryDto> following;
    private Set<ProfileSummaryDto> followers;

    public Set<ProfileSummaryDto> getFriends() {
        if (following == null || followers == null) {
            return null;
        }
        final Set<ProfileSummaryDto> friends = new HashSet<>(following);
        friends.retainAll(followers);
        return friends;
    }
}
