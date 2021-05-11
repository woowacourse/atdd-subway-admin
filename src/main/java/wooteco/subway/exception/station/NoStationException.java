package wooteco.subway.exception.station;

import wooteco.subway.exception.SubwayException;

public class NoStationException extends SubwayException {

    public NoStationException() {
        super("존재하지 않는 역입니다.");
    }
}


