package com.woopaca.noongil.domain.address;

import java.util.Optional;

public interface AddressCoordinateConverter {

    Optional<Coordinate> convertToCoordinate(String address);
}
