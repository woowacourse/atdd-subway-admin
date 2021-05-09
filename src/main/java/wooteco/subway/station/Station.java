package wooteco.subway.station;

import wooteco.subway.exception.IllegalInputException;

public class Station {
    private Long id;
    private String name;

    public Station() {
    }

    public Station(Long id, String name) {
        this(name);
        this.id = id;
    }

    public Station(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalInputException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

