package wooteco.subway.station;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.Line;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class StationH2Dao implements StationDao {

    private final JdbcTemplate jdbcTemplate;

    public StationH2Dao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Station save(Station station) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO STATION (name) VALUES (?)";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, station.getName());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Station(id, station.getName());
    }

    @Override
    public List<Station> findAll() {
        String sql = "SELECT * FROM STATION";
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Station station = new Station(
                            rs.getLong("id"),
                            rs.getString("name")
                    );
                    return station;
                });
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM STATION WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Station findByName(String name) {
        String sql = "SELECT * FROM STATION WHERE name=?";
        return jdbcTemplate.queryForObject(sql, Station.class, name);
    }
}
