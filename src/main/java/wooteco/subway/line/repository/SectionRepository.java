package wooteco.subway.line.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.line.dto.SectionRequest;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class SectionRepository {
    private static final int NO_EXIST_COUNT = 0;
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Long> stationIdRowMapperByUpStationId = (resultSet, rowNum) -> resultSet.getLong("up_station_id");
    private final RowMapper<Long> stationIdRowMapperByDownStationId = (resultSet, rowNum) -> resultSet.getLong("down_station_id");

    public SectionRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(final Long lineId, final Long upStationId, final Long downStationId, final int distance) {
        String query = "INSERT INTO section(line_id, up_station_id, down_station_id, distance) VALUES(?, ?, ? ,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(Connection -> {
            PreparedStatement ps = Connection.prepareStatement(query, new String[]{"id"});
            ps.setLong(1, lineId);
            ps.setLong(2, upStationId);
            ps.setLong(3, downStationId);
            ps.setInt(4, distance);
            return ps;
        }, keyHolder);
    }

    public List<Long> getStationIdsByLineId(final Long id) {
        String query = "SELECT up_station_id FROM section WHERE line_id = ?";
        Set<Long> stationIds = new HashSet<>(jdbcTemplate.query(query, stationIdRowMapperByUpStationId, id));

        query = "SELECT down_station_id FROM section WHERE line_id = ?";
        stationIds.addAll(jdbcTemplate.query(query, stationIdRowMapperByDownStationId, id));
        return new ArrayList<>(stationIds);
    }

    public void saveBaseOnUpStation(final Long lineId, final SectionRequest sectionRequest) {
        try {
            String query = "SELECT down_station_id FROM section WHERE line_id = ? AND up_station_id = ?";
            Long beforeConnectedStationId = Objects.requireNonNull(jdbcTemplate.queryForObject(query, Long.class, lineId, sectionRequest.getUpStationId()));
            saveSectionBetweenStationsBaseOnUpStation(lineId, sectionRequest, beforeConnectedStationId);
        } catch (EmptyResultDataAccessException e) {
            save(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        }
    }

    private void saveSectionBetweenStationsBaseOnUpStation(final Long lineId, final SectionRequest sectionRequest, final Long beforeConnectedStationId) {
        String query = "SELECT distance FROM section WHERE line_id = ? AND up_station_id = ?";
        int currentShortestDistance = Objects.requireNonNull(jdbcTemplate.queryForObject(query, Integer.class, lineId, sectionRequest.getUpStationId()));
        if (currentShortestDistance <= sectionRequest.getDistance()) {
            throw new IllegalArgumentException("기존에 존재하는 구간의 길이가 더 짧습니다.");
        }
        save(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        save(lineId, sectionRequest.getDownStationId(), beforeConnectedStationId, currentShortestDistance - sectionRequest.getDistance());
        deleteSection(lineId, sectionRequest.getUpStationId(), beforeConnectedStationId);
    }

    public void saveBaseOnDownStation(final Long lineId, final SectionRequest sectionRequest) {
        try {
            String query = "SELECT up_station_id FROM section WHERE line_id = ? AND down_station_id = ?";
            Long beforeConnectedStationId = Objects.requireNonNull(jdbcTemplate.queryForObject(query, Long.class, lineId, sectionRequest.getDownStationId()));
            saveSectionBetweenStationsBaseOnDownStation(lineId, sectionRequest, beforeConnectedStationId);
        } catch (EmptyResultDataAccessException e) {
            save(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        }
    }

    private void saveSectionBetweenStationsBaseOnDownStation(final Long lineId, final SectionRequest sectionRequest, final Long beforeConnectedStationId) {
        String query = "SELECT distance FROM section WHERE line_id = ? AND down_station_id = ?";
        int currentShortestDistance = Objects.requireNonNull(jdbcTemplate.queryForObject(query, Integer.class, lineId, sectionRequest.getDownStationId()));
        if (currentShortestDistance <= sectionRequest.getDistance()) {
            throw new IllegalArgumentException("기존에 존재하는 구간의 길이가 더 짧습니다.");
        }
        save(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        deleteSection(lineId, beforeConnectedStationId, sectionRequest.getDownStationId());
        save(lineId, beforeConnectedStationId, sectionRequest.getUpStationId(), currentShortestDistance - sectionRequest.getDistance());
    }

    private void deleteSection(final Long lineId, final Long upStationId, final Long downStationId) {
        String query = "DELETE FROM section WHERE line_id = ? AND up_station_id = ? AND down_station_id = ?";
        int affectedRowCount = jdbcTemplate.update(query, lineId, upStationId, downStationId);
        if (affectedRowCount == NO_EXIST_COUNT) {
            throw new NotFoundException("존재하지 않는 구간을 삭제하려 했습니다.");
        }
    }
}
