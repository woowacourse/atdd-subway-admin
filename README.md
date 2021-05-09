# 1, 2단계 기능 목록

## 지하철 역 관리 API

- [x] 역 생성
  - [x] 같은 이름 지하철역 생성 불가
- [x] 역 목록 불러오기
- [x] 역 삭제
  - [x] 존재하지 않는 id의 경우 예외 처리
    
## 지하철 노선 관리 API

- [x] 노선 생성
    - [x] 같은 이름 노선 생성 불가
    - [x] 같은 색깔 노선 생성 불가
    - 존재하지 않는 색깔인 경우 예외 처리(optional)
- [x] 노선 목록 조회
- [x] 노선 조회
    - [x] 존재하지 않는 id의 경우 예외 처리
- [x] 노선 수정
    - [x] 존재하지 않는 id의 경우 예외 처리
    - [x] 같은 이름 노선으로 변경 불가
    - [x] 같은 색깔 노선으로 변경 불가
    - 존재하지 않는 색깔인 경우 예외 처리(optional)
- [x] 노선 삭제
    - [x] 존재하지 않는 id의 경우 예외 처리
    
## End-to-End Test

- [x] 노선 기능에 대한 E2E 테스트를 작성
- [x] StationAcceptanceTest 클래스를 참고

## DAO 를 JDBC 사용하도록 수정
- [x] Station
- [x] Line

## Spring Bean
- [x] Spring Bean을 사용하여 생명주기 관리

# 3단계 기능 목록

## 노선 추가 API
- [x] 상행종점역, 하행종점역, 노선 길이(구간 길이) 추가
- [x] 상행종점역이나 하행종점역의 ID가 존재하지 않을 경우 예외처리
- [x] 상행종점역과 하행종점역이 같은 경우 예외처리
- [x] 노선 길이가 자연수가 아닌 경우 예외처리

## 노선 조회 API
- [x] 노선에 포함된 구간 정보를 상행 종점부터 하행 종점까지 반환
- [x] 존재하지 않는 ID의 노선일 경우 예외처리

## 구간 추가 API
- [x] 노선에 구간을 추가한다.
- [x] 상행역이나 하행역 둘 중 하나만 노선에 이미 존재해야 됨(해당 하지 않는 경우 예외처리)
- [x] 상행 종점역이나 하행 종점역에 구간을 추가할 수 있음
- [x] 하나의 구간 사이에 새로운 역이 등록 할 경우 기존 구간의 길이를 분할
- [x] 역 사이에 새로운 역을 등록 할 경우 기존 역 사이 길이 이상일 경우 예외 처리
- [x] 존재하지 않는 ID의 노선일 경우 예외처리
- [x] 상행역이나 하행역의 ID가 존재하지 않을 경우 예외처리
- [x] 구간 길이가 자연수가 아닌 경우 예외처리

## 구간 제거 API
- [ ] 종점이 제거 될 경우 다음 역이 종점이 됨
- [ ] 중간역이 제거될 경우 두 개의 구간을 하나의 구간으로 합침
- [ ] 존재하지 않는 ID의 노선인 경우 예외 처리
- [ ] 노선에 존재하지 않는 ID의 역인 경우 예외 처리
- [ ] 노선에 하나의 구간이 남은 경우 예외 처리
