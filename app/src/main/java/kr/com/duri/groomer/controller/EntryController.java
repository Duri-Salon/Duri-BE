package kr.com.duri.groomer.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.response.EntryResponse;
import kr.com.duri.groomer.application.facade.EntryFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/entry")
public class EntryController {
    private final EntryFacade entryFacade;

    @GetMapping("/waiting")
    public CommonResponseEntity<List<EntryResponse>> getWaitingEntries() {
        return CommonResponseEntity.success(entryFacade.getWaitingEntries());
    }
}
