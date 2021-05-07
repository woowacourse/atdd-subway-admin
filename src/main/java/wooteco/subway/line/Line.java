package wooteco.subway.line;

import wooteco.subway.station.Station;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    public Line(){

    }

    public Line(final String name, final String color) {
        this(null, name, color, Collections.emptyList());
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, Collections.emptyList());
    }

    public Line(final Long id, final String name, final String color, final List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return id.equals(line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
