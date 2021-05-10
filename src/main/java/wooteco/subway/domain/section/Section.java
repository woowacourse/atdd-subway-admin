package wooteco.subway.domain.section;

import wooteco.subway.domain.station.Station;

public class Section {

    private final Long id;
    private final Long lineId;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section(Station upStation, Station downStation, int distance) {
        this (null, null, upStation, downStation, distance);
    }

    public Section(Long id, Long lineId, Station upStation, Station downStation, int distance) {
        validateDistance(distance);
        validateStation(upStation, downStation);
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("지하철 구간의 거리는 0 혹은 음수가 될 수 없습니다.");
        }
    }

    private void validateStation(Station upStation, Station downStation) {
        if (upStation == null || downStation == null) {
            throw new IllegalArgumentException("상행역과 하행역은 null이 될 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }
}
