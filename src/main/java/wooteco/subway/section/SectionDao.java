package wooteco.subway.section;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.NoSuchDataException;

@Repository
public class SectionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public long save(Section section) {
        String query = "INSERT INTO section(line_id, up_station_id, down_station_id, distance) VALUES(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
            ps.setLong(1, section.getLineId());
            ps.setLong(2, section.getUpStationId());
            ps.setLong(3, section.getDownStationId());
            ps.setInt(4, section.getDistance());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Section findBySectionId(long sectionId) {
        String query = "SELECT * FROM section WHERE id = ?";
        return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> {
            Section section = new Section(
                resultSet.getLong("line_id"),
                resultSet.getLong("up_station_id"),
                resultSet.getLong("down_station_id"),
                resultSet.getInt("distance")
            );
            return section;
        }, sectionId);
    }

    public List<Section> findSectionsByLineId(long lineId) {
        String query = "SELECT * FROM section WHERE line_id = (?)";

        return jdbcTemplate.query(query, (resultSet, rowNum) -> new Section(
            resultSet.getLong("line_id"),
            resultSet.getLong("up_station_id"),
            resultSet.getLong("down_station_id"),
            resultSet.getInt("distance")
        ), lineId);
    }

    public void delete(Long lineId, Long upStationId) {
        String query = "DELETE FROM section WHERE line_id = (?) AND up_station_id = (?)";
        int affectedRowNumber = jdbcTemplate.update(query, lineId, upStationId);

        if (affectedRowNumber == 0) {
            throw new NoSuchDataException("없는 구간입니다.");
        }
    }

    public Section findByUpStationId(Long upStationId) {
        String query = "SELECT * FROM section WHERE up_station_id = ?";

        return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> new Section(
            resultSet.getLong("line_id"),
            resultSet.getLong("up_station_id"),
            resultSet.getLong("down_station_id"),
            resultSet.getInt("distance")
        ), upStationId);
    }

    public Section findByDownStationId(Long downStationId) {
        String query = "SELECT * FROM section WHERE down_station_id = ?";

        return jdbcTemplate.queryForObject(query, (resultSet, rowNum) -> new Section(
            resultSet.getLong("line_id"),
            resultSet.getLong("up_station_id"),
            resultSet.getLong("down_station_id"),
            resultSet.getInt("distance")
        ), downStationId);
    }

    public void update(long sectionId, long nextSectionId, int newDistance) {
        String query = "UPDATE section SET down_station_id = (?), new_distance = (?)  WHERE id = (?)";
        int affectedRowNumber = jdbcTemplate
            .update(query, nextSectionId, newDistance, sectionId);
        if (affectedRowNumber == 0) {
            throw new NoSuchDataException("존재하지 않아 변경할 수 없습니다.");
        }
    }

    public Long findIdByStationIds(long upStationId, long downStationId) {
        String query = "SELECT id FROM section WHERE up_station_id = (?) AND down_station_id = (?)";
        return jdbcTemplate.queryForObject(query, Long.class, upStationId, downStationId);
    }
}
