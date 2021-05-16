package wooteco.subway.section;

import wooteco.subway.exception.InvalidInsertException;
import wooteco.subway.line.Line;
import wooteco.subway.station.Station;

public class Section {
    private Long id;
    private Line line;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        validateStations(upStation, downStation);
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Long id, Line line, Section section) {
        this(id, line, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public Section(Station upStation, Station downStation, int distance) {
        this(null, null, upStation, downStation, distance);
    }

    private void validateStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new InvalidInsertException("구간의 상행과 하행이 같을 수 없습니다.");
        }
    }

    public boolean isBefore(Section first) {
        return downStation.equals(first.getUpStation());
    }

    public boolean isAfter(Section last) {
        return upStation.equals(last.getDownStation());
    }

    public boolean isSameAsUpId(Long stationId) {
        return getUpStationId().equals(stationId);
    }

    public boolean isSameAsDownId(Long stationId) {
        return getDownStationId().equals(stationId);
    }

    public boolean hasLongerDistanceThan(Section oldSection) {
        return this.distance >= oldSection.distance;
    }

    public int plusDistance(Section after) {
        return this.distance + after.distance;
    }

    public int subtractDistance(Section newSection) {
        return this.distance - newSection.distance;
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
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

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }
}
