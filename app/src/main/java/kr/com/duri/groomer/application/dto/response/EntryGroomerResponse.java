package kr.com.duri.groomer.application.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryGroomerResponse {
    private String groomerName;
    private String groomerGender;
    private Integer groomerAge;
    private List<String> license;
}
