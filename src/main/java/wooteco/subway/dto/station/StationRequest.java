package wooteco.subway.dto.station;

import javax.validation.constraints.NotBlank;

public class StationRequest {
    @NotBlank(message = "유효한 이름을 입력해주세요")
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}