// 익스프레스로 서버 설정 및 구동

// import
const express = require('express');
const app = express();
const bodyParser = require('body-parser');

//  router import
app.use('/users', require('./api/users/index.js'));


// body parser : POST 내용이 담길 body
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


//  Supertest사용을 위해 app변수를 외부 '노출'하여 '모듈'로 만들어주기
//  Supertest를 사용하려면 서버 역할을 하는 익스프레스 객체를 가져와함
module.exports = app;


// connect success
app.listen(3000, () => {
  console.log('Example app listening on port 3000!');
  //  모델 정의하는 sync()메서드
  // 함수 실행->sequelize객체에 연결된db에 정의한 model을 테이블로 생성하는기능
  require('./models').sequelize.sync({force: true}) // 배포시에는 반드시 false로 바꾸기 !!!
      .then(() => {
        console.log('Databases sync');
      });
});