//  test 파일 (specification)


//  1. describe(): 테스트 수트
//  2. it(): 테스트


//  import
const assert = require('assert');
//  첫번째 파라미터 : test suite의 설명을 서술형 문자열로 입력
//  두번째 파라미터 : 함수입력, 비동기 로직의 콜백 형식으로 (이 안에 it함수로 실제 테스트 코드 작성) 
describe('GET /users', () => {  
    //  it()함수를 이요애 실제 test code 작성
    it('should return 200 status code', () => {
      console.log('test 1');
    });
  });