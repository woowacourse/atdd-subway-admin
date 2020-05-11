package wooteco.subway.admin.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Station {
    @Id
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Station(String name) {
        if (isNotValid(name)) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    private boolean isNotValid(String name) {
        return name == null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
