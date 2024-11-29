package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.domain.entity.Groomer;

public interface GroomerService {
    Groomer getGroomerByShopId(Long shopId);
}
