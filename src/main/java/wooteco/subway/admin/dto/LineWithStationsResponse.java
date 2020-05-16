package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.domain.LineDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LineWithStationsResponse {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private String lineColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Station> stations;

    public LineWithStationsResponse() {
    }

    public LineWithStationsResponse(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime, String lineColor, LocalDateTime createdAt, LocalDateTime updatedAt, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.lineColor = lineColor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stations = stations;
    }

    public static LineWithStationsResponse of(LineDto lineDto) {
        return of(lineDto, new ArrayList<>());
    }

    public static LineWithStationsResponse of(Line line, List<Station> stations) {
        return new LineWithStationsResponse(line.getId(), line.getName(), line.getStartTime(),
                line.getEndTime(), line.getIntervalTime(), line.getLineColor(),
                line.getCreatedAt(), line.getUpdatedAt(), stations);
    }

    public static LineWithStationsResponse of(LineDto lineDto, List<Station> stations) {
        return new LineWithStationsResponse(lineDto.getId(), lineDto.getName(), lineDto.getStartTime(),
                lineDto.getEndTime(), lineDto.getIntervalTime(), lineDto.getLineColor(),
                lineDto.getCreatedAt(), lineDto.getUpdatedAt(), stations);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public String getLineColor() {
        return lineColor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Station> getStations() {
        return stations;
    }
}
