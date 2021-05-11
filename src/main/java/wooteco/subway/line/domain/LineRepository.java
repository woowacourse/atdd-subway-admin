package wooteco.subway.line.domain;

import java.util.List;

public interface LineRepository {
    Line save(Line line);

    List<Line> findAll();

    Line findById(Long id);

    void update(Line line);

    void deleteById(Long id);
}
