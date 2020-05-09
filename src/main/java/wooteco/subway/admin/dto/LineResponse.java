package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;
    private String lineColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<LineStation> lineStations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime, String lineColor, LocalDateTime createdAt, LocalDateTime updatedAt, List<LineStation> lineStations) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.lineColor = lineColor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lineStations = lineStations;
    }

    public static LineResponse of(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getStartTime(), line.getEndTime(), line.getIntervalTime(), line.getLineColor(), line.getCreatedAt(), line.getUpdatedAt(), line.getStations());
    }

    public static List<LineResponse> listOf(List<Line> lines) {
        return lines.stream()
                .map(it -> LineResponse.of(it))
                .collect(Collectors.toList());
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

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
