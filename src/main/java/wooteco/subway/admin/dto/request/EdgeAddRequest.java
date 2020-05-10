package wooteco.subway.admin.dto.request;

public class EdgeAddRequest {
    private String preStationName;
    private String stationName;
    private int distance;
    private int duration;

    private EdgeAddRequest() {
    }


    public EdgeAddRequest(String preStationName, String stationName, int distance, int duration) {
        this.preStationName = preStationName;
        this.stationName = stationName;
        this.distance = distance;
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public String getPreStationName() {
        return preStationName;
    }

    public String getStationName() {
        return stationName;
    }
}
