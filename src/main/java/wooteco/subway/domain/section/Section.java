package wooteco.subway.domain.section;

import wooteco.subway.domain.station.Station;

import java.util.Objects;

public class Section {

    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;
    private Long lineId;

    public Section(Station upStation, Station downStation, int distance, Long lineId) {
        this(null, upStation, downStation, distance, lineId);
    }

    public Section(Long id, Station upStation, Station downStation, int distance, Long lineId) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.lineId = lineId;
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

    public Long getLineId() {
        return lineId;
    }

    public long getUpStationId() {
        return upStation.getId();
    }

    public long getDownStationId() {
        return downStation.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && upStation.equals(section.upStation) && downStation.equals(section.downStation) && lineId.equals(section.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance, lineId);
    }
}