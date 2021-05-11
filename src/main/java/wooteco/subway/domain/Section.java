package wooteco.subway.domain;

import java.util.Objects;

public class Section {
    private final int distance;
    private long id;
    private long lineId;
    private Station upStation;
    private Station downStation;

    public Section(long id, long lineId, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean hasAny(Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    public boolean isUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public long getId() {
        return id;
    }

    public long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return this.distance;
    }

    public Station getUpStation() {
        return this.upStation;
    }

    public void setUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public Station getDownStation() {
        return this.downStation;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
