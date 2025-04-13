package com.woopaca.noongil.adapter.address;

import com.woopaca.noongil.infrastructure.address.KakaoLocalClient;
import com.woopaca.noongil.infrastructure.address.KakaoLocalClient.Address;
import com.woopaca.noongil.infrastructure.address.KakaoLocalClient.AddressResponse;
import com.woopaca.noongil.domain.address.AddressCoordinateConverter;
import com.woopaca.noongil.domain.address.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class KakaoAddressCoordinateConverter implements AddressCoordinateConverter {

    private final KakaoLocalClient kakaoLocalClient;

    public KakaoAddressCoordinateConverter(KakaoLocalClient kakaoLocalClient) {
        this.kakaoLocalClient = kakaoLocalClient;
    }

    @Override
    public Optional<Coordinate> convertToCoordinate(String address) {
        if (!StringUtils.hasText(address)) {
            return Optional.empty();
        }

        AddressResponse response = kakaoLocalClient.addressToCoordinate(address);
        if (response == null) {
            return Optional.empty();
        }

        List<Address> addresses = response.addresses();
        if (CollectionUtils.isEmpty(addresses)) {
            log.warn("주소 변환 실패.(변환된 값 없음) address: {})", address);
            return Optional.empty();
        }

        try {
            return addresses.stream()
                    .findFirst()
                    .map(this::convertCoordinate);
        } catch (Exception e) {
            log.error("[KakaoAddressCoordinateConverter][convertToCoordinate] 좌표값 변환 실패. address: {}", address, e);
            return Optional.empty();
        }
    }

    private Coordinate convertCoordinate(Address address) {
        double latitude = Double.parseDouble(address.y());
        double longitude = Double.parseDouble(address.x());
        return new Coordinate(latitude, longitude);
    }
}
