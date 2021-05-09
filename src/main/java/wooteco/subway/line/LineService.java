package wooteco.subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Line create(final Line line) {
        validateName(line.getName());

        final Long id = lineDao.save(line.getName(), line.getColor());
        return findById(id);
    }

    public void update(final Line line) {
        final Long id = line.getId();

        validateExisting(id);

        final String newName = line.getName();
        final String oldName = findById(id).getName();

        if(!oldName.equals(newName)){
            validateName(newName);
        }

        lineDao.update(line.getId(), line.getName(), line.getColor());
    }

    public void delete(final Long id) {
        validateExisting(id);

        lineDao.delete(id);
    }

    public Line findById(final Long id) {
        return lineDao.findById(id)
                .orElseThrow(() -> new LineException("존재하지 않는 노선입니다."));
    }

    public List<Line> findAll() {
        return lineDao.findAll();
    }

    private void validateName(final String name){
        if (lineDao.isExistingName(name)) {
            throw new LineException("이미 존재하는 노선 이름입니다.");
        }
    }

    private void validateExisting(final Long id){
        findById(id);
    }
}
