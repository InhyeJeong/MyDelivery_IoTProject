//  test 파일 (specification)


//  1. describe(): 테스트 수트
//  2. it(): 테스트


//  import
//const assert = require('assert');
const should = require('should');
const request = require('supertest');   //  상수에 슈퍼테스트 모듈 할당
const app = require('../../app');   //  supertest사용을 위해 app을 외부로 노출시켜서 사용함
const syncDatabase = require('../../bin/sync-database');
const models = require('../../models');


//  첫번째 파라미터 : test suite의 설명을 서술형 문자열로 입력
//  두번째 파라미터 : 함수입력, 비동기 로직의 콜백 형식으로 (이 안에 it함수로 실제 테스트 코드 작성) 
describe('GET /users', () => {
  before('sync database', (done) => {
    // sync data base ...
    // sync({force: true}); //db초기화
    syncDatabase().then(() =>
      done());
    
  });// before

  const users = [
    {
      receiverName: 'alice',
      receiverAddress : 'Seoul',
      locationCode : 1,
      receiverPhone : '010-1234-5678',
      senderPhone : '010-1777-7777',
      companyKey : 1
    },
    {
      receiverName: 'bek',
      receiverAddress : 'Busan',
      locationCode : 2,
      receiverPhone : '010-2341-5678',
      senderPhone : '010-2777-7777',
      companyKey : 2
    },
    {
      receiverName: 'chris',
      receiverAddress : 'Daegu',
      locationCode : 3,
      receiverPhone : '010-3124-5678',
      senderPhone : '010-3777-7777',
      companyKey : 3
    }
  ];
  //   db에 users테이블에 있는 유저를 추가하는 역할
  before('insert 3 users into database', (done) => {
    // bulkCreate()함수 여러개 data를 배열로 받아 여러개 로우 생성하는 함수
    models.User.bulkCreate(users).then(() => done());
  });
    //  it()함수를 이용해 실제 test code 작성
    it('should return 200 status code', (done) => {
      //    console.log('test 1');

      //    검증 로직 시작



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
        //  after : db초기화
        // after('clear up database', (done) => {
        //   syncDatabase().then(() => done());
        // });

        
  });
})