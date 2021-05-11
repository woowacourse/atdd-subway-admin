# 지하철 미션

## 1단계 기능 요구 사항

### 도메인

1. 역
    - [x] 역 입력 기능
        - [x] [예외] 역 이름이 중복되면 예외
        - [x] [예외] 역 이름은 20자를 초과하면 예외
        - [x] 띄어쓰기가 2개가 연속되었을 경우 1개로 줄여주는 기능
        - [x] [예외] 영어, 한글, 숫자, 일부 특수문자만(띄어쓰기 1개, 괄호, ·)이외의 문자를 입력하면 예외
        - [x] 첫 글자와 마지막 글자에 공백이 들어가면 trim하는 기능
    - [x] 역 삭제 기능
        - [x] [예외] 없는 ID의 역을 삭제하려고 하면 예외
2. 노선
    - [x] 노선 생성 기능
        - [x] [예외] 노선 명이 겹치면 예외
        - [x] [예외] 노선 색상이 겹치면 예외
        - [x] [예외] 노선 명이 20자를 넘어가면 예외
        - [x] 노선 명이 띄어쓰기가 2개 이상일 경우 1개로 줄여주는 기능
        - [x] [예외] 노선 명이 영어, 한글, 숫자, 일부 특수문자만(띄어쓰기 1개, 괄호, ·)이외의 문자를 입력하면 예외
        - [x] 노선명에 첫 글자와 마지막 글자에 공백이 들어가면 trim하는 기능
    - [x] 노선 조회 기능
        - [x] 개별 노선 조회 기능
            - [x] id, 이름, 색상을 반환해야한다.
        - [x] 모든 노선 조회 기능
            - [x] id, 이름, 색상을 포함한 노선 목록을 반환해야한다.
    - [x] 노선 수정 기능
        - [x] 노선 이름을 수정할 수 있다.
            - [x] [예외] 노선 생성기능의 노선명 유효성검사를 통과해야한다.
        - [x] 노선 색상을 수정할 수 있다.
            - [x] [예외] 노선 색상이 겹치면 예외
    - [x] 노선 삭제 기능

### 테스트

1. End to End테스트를 구현해야 한다.

## 2단계 기능 요구 사항
1. 스프링 JDBC 활용하여 H2 DB에 저장하기
   - [x] Dao 객체가 아닌 DB에서 데이터를 관리하기
   - [x] DB에 접근하기 위한 spring jdbc 라이브러리를 활용하기 (JdbcTemplate 등)
2. H2 DB를 통해 저장된 값 확인하기
   - [x] log, console 등 실제로 DB에 저장이 잘 되었는지 확인할 수 있도록 설정하기
3. 스프링 빈 활용하기
   - [x] 매번 생성하지 않아도 되는 객체와 싱글톤이나 static으로 구현되었던 객체들을 스프링 빈으로 관리해도 좋음

## 3단계 기능 요구 사항
- [ ] 노선 등록 시 상행 종점, 하행 종점, 두 종점간의 거리를 추가
- [x] 구간 기능
  - [x] [예외] 구간의 거리가 음수이면 예외
  - [x] [예외] 상행역과 하행역이 같으면 예외 
  - [x] 해당 역이 구간에 존재하는지 확인하는 기능
  - [x] 다음 구간과 하나의 구간으로 합치는 기능
- [ ] 구간 일급 컬렉션 기능
  - [x] [예외] 사이즈가 1보다 작은 구간을 만들려 하면 예외
  - [x] [예외] 상행종점, 하행종점이 1개가 아니면 예외
  - [x] 구간들을 생성할 때 자동으로 상행종점역 -> 하행종점역 기준으로 정렬하는 기능
  - [x] 하행종점역 -> 상행종점역 기준으로 노선목록을 반환하는 기능
    - [x] 구간을 추가하는 기능
      - [x] [예외] 상행역과 하행역이 이미 노선에 모두 등록되어 있으면 예외
      - [x] [예외] 상행역과 하행역이 모두 노선에 없으면 예외
      - [x] [예외] 역 사이에 새로운 역이 등록할 경우 기존 역 사이보다 크거나 같으면 역을 등록할 수 없음
      - [x] 구간 사이에 역이 추가되면 양옆 구간의 거리가 자동으로 수정된다.
    - [ ] 노선 조회 시 구간을 모두 조회하는 기능
    - [ ] 구간을 제거하는 기능
      - [x] [예외] 제거하려는 역이 노선에 없으면 예외
      - [x] [예외] 구간이 하나밖에 없는 노선은 제거할 수 없음
      - [ ] 구간이 삭제되면 양옆 구간의 거리와 상행역, 하행역이 자동으로 수정된다.