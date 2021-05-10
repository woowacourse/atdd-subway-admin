package wooteco.subway.line.repository.infra;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.DuplicatedNameException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.repository.LineRepository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JDBCLineRepository implements LineRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Line> actorRowMapper = (resultSet, rowNum) ->
            new Line(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("color"),
                    new ArrayList<>()
            );

    public JDBCLineRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Line save(final Line line) {
        try {
            String query = "INSERT INTO line(name, color) VALUES(?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            this.jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"id"});
                ps.setString(1, line.getName());
                ps.setString(2, line.getColor());
                return ps;
            }, keyHolder);
            return findById(keyHolder.getKey().longValue());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedNameException("이미 존재하는 지하철 노선 이름입니다.", e.getCause());
        }
    }

    @Override
    public List<Line> findAll() {
        String query = "SELECT * FROM line";
        return this.jdbcTemplate.query(query, actorRowMapper);
    }

    @Override
    public Line findById(final Long id) {
        String query = "SELECT * FROM line WHERE id = ?";
        return this.jdbcTemplate.queryForObject(query, actorRowMapper, id);
    }

    @Override
    public void delete(final Long id) {
        String query = "DELETE FROM line WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }

    @Override
    public void update(final Line line) {
        String query = "UPDATE line SET name = ?, color = ? WHERE id = ?";
        this.jdbcTemplate.update(query, line.getName(), line.getColor(), line.getId());
    }

    @Override
    public void deleteAll() {
        String query = "DELETE FROM line";
        this.jdbcTemplate.update(query);
    }
}
