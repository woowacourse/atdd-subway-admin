package wooteco.subway.section.domain;

import wooteco.subway.section.service.IllegalSectionDistanceException;
import wooteco.subway.station.domain.Station;

import java.util.Objects;

public class Section {
    private final Long id;
    private final Long lineId;
    private final Station upStation;
    private final Station downStation;
    private final Integer distance;

    public Section(final Long lineId, final Station upStation, final Station downStation) {
        this(lineId, upStation, downStation, null);
    }

    public Section(final Long lineId, final Station upStation, final Station downStation, final Integer distance) {
        this(null, lineId, upStation, downStation, distance);
    }

    public Section(final Long id, final Long lineId, final Station upStation, final Station downStation, final Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public int getDistance() {
        return distance;
    }

    public Section adjustBy(final Section section) {
        validateDistance(section);
        int newSectionDistance = this.getDistanceGap(section);

        if (this.hasSameUpStation(section)) {
            return new Section(
                    this.getId(),
                    section.getLineId(),
                    new Station(section.getDownStationId()),
                    new Station(this.getDownStationId()),
                    newSectionDistance);
        }
        return new Section(
                this.getId(),
                section.getLineId(),
                new Station(this.getUpStationId()),
                new Station(section.getUpStationId()),
                newSectionDistance);
    }

    private void validateDistance(final Section section) {
        if (this.isShorterOrEqualTo(section)) {
            throw new IllegalSectionDistanceException();
        }
    }

    private boolean isShorterOrEqualTo(final Section that) {
        return this.distance <= that.distance;
    }

    private int getDistanceGap(final Section that) {
        return Math.abs(this.distance - that.distance);
    }

    private boolean hasSameUpStation(final Section that) {
        return this.upStation.equals(that.upStation);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
