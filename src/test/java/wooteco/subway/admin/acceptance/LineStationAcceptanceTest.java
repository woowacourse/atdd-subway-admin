package wooteco.subway.admin.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class LineStationAcceptanceTest {
	@LocalServerPort
	int port;
	private List<LineStationResponse> lineResponses = new ArrayList<>();

	public static RequestSpecification given() {
		return RestAssured.given().log().all();
	}

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@DisplayName("지하철 노선에서 지하철역 추가 / 제외")
	@Test
	void manageLineStation() {
		// @formatter:off

		// Given 지하철역이 여러 개 추가되어있다.
		StationResponse stationResponse1 = createStation("신림");
		StationResponse stationResponse2 = createStation("설입");
		StationResponse stationResponse3 = createStation("사당");
		StationResponse stationResponse4 = createStation("서초");
		// And 지하철 노선이 추가되어있다.
		LineResponse lineResponse = createLine("2호선");
		// When 지하철 노선에 지하철역을 등록하는 요청을 한다.
		lineResponses.add(addStationOnLine(null, String.valueOf(stationResponse1.getId()), "10", "10"));
		lineResponses.add(addStationOnLine(String.valueOf(stationResponse1.getId()), String.valueOf(stationResponse2.getId()), "10", "10"));
		lineResponses.add(addStationOnLine(String.valueOf(stationResponse2.getId()), String.valueOf(stationResponse3.getId()), "10", "10"));
		lineResponses.add(addStationOnLine(String.valueOf(stationResponse3.getId()), String.valueOf(stationResponse4.getId()), "10", "10"));
		// Then 지하철역이 노선에 추가 되었다.
		LineResponse persistLine  = getLineBy(lineResponse.getId());
		assertEquals(lineResponses.size(), 4);

		// When 지하철 노선의 지하철역 목록 조회 요청을 한다.
		// Then 지하철역 목록을 응답 받는다.
		List<LineStation> lineStationResponses = new ArrayList(persistLine.getLineStations());
		// And 새로 추가한 지하철역을 목록에서 찾는다.
		assertEquals(lineResponses.get(0).getStationId(), stationResponse1.getId());
		assertEquals(lineResponses.get(1).getStationId(), stationResponse2.getId());
		assertEquals(lineResponses.get(2).getStationId(), stationResponse3.getId());
		assertEquals(lineResponses.get(3).getStationId(), stationResponse4.getId());

		// When 지하철 노선에 포함된 특정 지하철역을 제외하는 요청을 한다.
		deleteStationBy(lineResponse.getId(), stationResponse4.getId());
		// Then 지하철역이 노선에서 제거 되었다.
		persistLine = getLineBy(lineResponse.getId());
		lineResponses.remove(3);
		assertEquals(lineResponses.size(), 3);
		// When 지하철 노선의 지하철역 목록 조회 요청을 한다.
		// Then 지하철역 목록을 응답 받는다.
		// And 제외한 지하철역이 목록에 존재하지 않는다.
		assertThat(lineResponses.get(0).getStationId()).isNotEqualTo(stationResponse4.getId());
	}

	private void deleteStationBy(Long lineId, Long stationId) {
		given().
				when().
				delete("/line-stations/" + "?lineId=" + lineId + "&stationId=" + stationId).
				then().
				log().all();
	}

	private LineResponse getLineBy(Long lineId) {
		return given()
				.when().
					get("/lines/" + lineId).
				then().
					log().all().
					extract().as(LineResponse.class);
	}

	private LineStationResponse addStationOnLine(String preStationId, String stationId, String distance, String duration) {
		Map<String, String> params = new HashMap<>();
		params.put("preStationId" ,preStationId);
		params.put("stationId" ,stationId);
		params.put("distance", distance);
		params.put("duration", duration);

		return given().
					body(params).
					contentType(MediaType.APPLICATION_JSON_VALUE).
					accept(MediaType.APPLICATION_JSON_VALUE).
				when()
					.post("/line-stations").
				then().
					log().all().
					extract().as(LineStationResponse.class);
	}

	private LineResponse createLine(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");
		params.put("lineColor", "bg-pink-700");

		return given().
					body(params).
					contentType(MediaType.APPLICATION_JSON_VALUE).
					accept(MediaType.APPLICATION_JSON_VALUE).
				when().
					post("/lines").
				then().
					log().all().
					extract().as(LineResponse.class);
	}

	private StationResponse createStation(String stationName) {
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);

		return given().
				body(params).
				contentType(MediaType.APPLICATION_JSON_VALUE).
				accept(MediaType.APPLICATION_JSON_VALUE).
		when().
				post("/stations").
		then().
				log().all().
				extract().as(StationResponse.class);
	}
	// @formatter:on
}
