package kr.com.duri.common.security.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SiteUserDto extends NaverUserDto {
    private String name;
    private String gender;
    private String birthday;
    private String mobile;
    private String mobileE164;
    private String birthyear;
}
