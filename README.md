# KurlyPretest
## 테스트 환경
* minimum sdk version > 24
* maximum sdk version > 35
* test sdk version > android 10 (API Level 29)
* 테스트 기기 > 갤럭시 노트 9
## 기능 구현
### 간단 설명
* clean architecture
* UI : compose
* database : room
* DI : hilt
### 메인
* [x] 섹션 노출
  * [x] 상단에 타이틀 노출
  * [x] 섹션에 따라 상품 리스트를 노출
  * [x] pull to refresh를 사용해 메인 화면을 refresh
* [x] 상품 노출
  * [x] 섹션 타입에 따라 상품 제목, 가격 노출
  * [x] 찜하기 버튼 표시
  * [x] 찜은 room을 사용해 내부에 저장
* [x] 네트워크
  * [x] 전달받은 mock 서버를 사용
  * [x] section과 product api를 요청을 만들고 사용
* [x] 테스트 코드
  * [x] 메인, 라이브러리들 UI, 테스트 코드 추가
## 참고
* [NowInAndroid](https://github.com/android/nowinandroid)를 Best Practice로 생각하고 많은 부분 참고