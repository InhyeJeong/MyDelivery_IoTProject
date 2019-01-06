//  test 파일 (specification)


//  1. describe(): 테스트 수트
//  2. it(): 테스트


//  import
//const assert = require('assert');
const should = require('should');
const request = require('supertest');   //  상수에 슈퍼테스트 모듈 할당
const app = require('../../app');   //  supertest사용을 위해 app을 외부로 노출시켜서 사용함

//  첫번째 파라미터 : test suite의 설명을 서술형 문자열로 입력
//  두번째 파라미터 : 함수입력, 비동기 로직의 콜백 형식으로 (이 안에 it함수로 실제 테스트 코드 작성) 
describe('GET /users', () => {  
    //  it()함수를 이요애 실제 test code 작성
    it('should return 200 status code', (done) => {
      //    console.log('test 1');

      //    검증 로직 시작
        // assert module : 매개변수 두 값이 같으면 pass, 아니면 error를 던짐
        // assert.equal(true, false);

       //   should module : 서술식의 검증을 코드로 작성할 수 있게 해줌
       //(true).should.be.equal(true);

       //   Supertest 시작 !!!!
       request(app) //  익스프레서 서버인 app을 슈퍼테스트로 테스트하겠다는 의미
                .get('/users/1')  // get함수로 API요청보냄
                .send({
                  name: 'foo'
                })
                .expect(200)    // expect함수로 응답코드 설정
                
                //  end함수의매개변수를 받는데
                //  요청 실패하면 err객체 활성화
                //  성공하면 res.body를 통해 응답 바디에 접근
                //  but 이 코드는 API가 상태코드 200을 리턴하는가만 check함
                .end((err, res) => {    
         if (err) throw err;

        //   it함수의 두번째 매개변수인 콜백함수의 매개변수.
        //   슈퍼테스트가 HTTP요청을 하는 비동기 로직->모카측에서 it함수가 종료되는 시점을 알기 위해 사용되는 함수
         done();
       });
    });

       //   바디 점검하는 code
       it('should return array', (done) => {
        request(app)
            .get('/users')
            .expect(200)
            .end((err, res) => {
              if (err) throw err;
              //    응답 바디의 타입이 배열인지 check
              res.body.should.be.an.instanceof(Array)
                                    //  배열의 길이가 3인지 check
                                    .and.have.length(3);
              //    배열 체크 후 배열메소드인 map을 이용해 배열 각요소 점검                      
              res.body.map(user => {
                user.should.have.properties('id', 'name');  //  프로퍼티
                user.id.should.be.a.Number();   //  숫자
                user.name.should.be.a.String(); //  문자
             });
             done();
           });
        });


        
  });