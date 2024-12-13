package kr.com.duri.groomer.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.response.EntryResponse;
import kr.com.duri.groomer.application.facade.EntryFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/entry")
public class EntryController {
    private final EntryFacade entryFacade;

    //입점 대기 목록 조회
    @GetMapping("/waiting")
    public CommonResponseEntity<List<EntryResponse>> getWaitingEntries() {
        return CommonResponseEntity.success(entryFacade.getWaitingEntries());
    }

    // 입점 승인 목록 조회
    @GetMapping("/approved")
    public CommonResponseEntity<List<EntryResponse>> getApprovedEntries() {
        return CommonResponseEntity.success(entryFacade.getApprovedEntries());
    }

    //입점 승인 처리
    @PostMapping("/approve/{shopId}")
    public CommonResponseEntity<String> approveEntry(@PathVariable Long shopId){
        entryFacade.approveEntry(shopId);
        return CommonResponseEntity.success("입점 승인이 완료되었습니다.");
    }

    //입점 거절 처리
    @PostMapping("/reject/{shopId}")
    public CommonResponseEntity<String> rejectEntry(@PathVariable Long shopId){
        entryFacade.rejectEntry(shopId);
        return CommonResponseEntity.success("입점 거절이 완료되었습니다.");
    }
}
