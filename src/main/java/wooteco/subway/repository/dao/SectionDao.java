package wooteco.subway.repository.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SectionDao {

    private static final RowMapper<Section> SECTION_ROW_MAPPER = (resultSet, rowNum) -> new Section(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getLong("up_station_id"),
            resultSet.getLong("down_station_id"),
            resultSet.getInt("distance")
    );

    private final JdbcTemplate jdbcTemplate;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Section insert(Section section) {
        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
        Long lineId = section.getLineId();
        Long upStationId = section.getUpStationId();
        Long downStationId = section.getDownStationId();
        int distance = section.getDistance();

        Map<String, Object> params = new HashMap<>(4);
        params.put("line_id", lineId);
        params.put("up_station_id", upStationId);
        params.put("down_station_id", downStationId);
        params.put("distance", distance);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Section(id, lineId, upStationId, downStationId, distance);
    }

    public List<Section> findAllByLineId(Long lineId) {
        String query = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(query, SECTION_ROW_MAPPER, lineId);
    }


    public void updateUpStationId(Long targetId, Long upStationId, Long downStationId, Long lineId) {
        String query = "UPDATE section SET up_station_id = ? WHERE up_station_id = ? AND down_station_id = ? AND line_id = ?";
        jdbcTemplate.update(query, targetId, upStationId, downStationId, lineId);
    }

    public void updateDownStationId(Long targetId, Long upStationId, Long downStationId, Long lineId) {
        String query = "UPDATE section SET down_station_id = ? WHERE up_station_id = ? AND down_station_id = ? AND line_id = ?";
        jdbcTemplate.update(query, targetId, upStationId, downStationId, lineId);
    }
}
