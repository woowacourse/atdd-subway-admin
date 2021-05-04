package wooteco.subway.station.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import wooteco.subway.station.domain.Station;

import java.sql.PreparedStatement;
import java.util.Objects;

public class StationRepository {
    private final JdbcTemplate jdbcTemplate;

    public StationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isExist(Station station) {
        String query = "SELECT EXISTS(SELECT * FROM STATION WHERE name = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, station.getName());
    }

    public Long save(Station station) {
        String query = "INSERT INTO STATION(name) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(Connection -> {
            PreparedStatement ps = Connection.prepareStatement(query, new String[]{"id"});
            ps.setString(1, station.getName());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
