package wooteco.subway.station;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(final Station station) {
        this(station.getId(), station.getName());
    }

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
