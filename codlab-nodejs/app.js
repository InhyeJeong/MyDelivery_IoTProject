'use strict';
// 익스프레스로 서버 설정 및 구동

// import
const express = require('express');
const app = express();
const bodyParser = require('body-parser');

// router
const router = express.Router();
module.exports = router;

// body parser : POST 내용이 담길 body
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

//  서버 구동하는 모듈

const port = 3000;
const syncDatabase = require('./bin/sync-database');

app.listen(port, () => {
  console.log('---------------Example app listening on port 3000');

  require('./models').sequelize.sync({force: false})
  .then(() => {
    console.log('---------------Database sync');
  });
 
});

//  Supertest사용을 위해 app변수를 외부 '노출'하여 '모듈'로 만들어주기
//  Supertest를 사용하려면 서버 역할을 하는 익스프레스 객체를 가져와함
module.exports = app;




//  실제 로직이 들어간 컨트롤러

//  import
const models = require('./models');


//컨트롤러

// index : get/view all
app.get('/users', (req, res) => {
    //  전체 테이블 불러오기
  models.User.findAll()
  .then(user => res.json(user));
    
  });



// show : get/findById

app.get('/users/:id', (req, res) => {
    const id = parseInt(req.params.id, 10);
    if (!id) {
      return res.status(400).json({error: 'Incorrect id'});
    }
    
    models.User.findOne({
        where: {//  where : id 컬럼 조건값 설정
          id: id
        }
      }).then(user => {
        if (!user) {//  없으면 404 응답
          return res.status(404).json({error: 'No User'});
        }
        //  성공하면 json()로 응답
        return res.json(user);
      });
  });





// destroy : delete
app.delete('/users/:id', (req, res) => {
  const id = parseInt(req.params.id, 10);
  if (!id) {
    return res.status(400).json({error: 'Incorrect id'});
  }
 
  models.User.destroy({
    where: {// id기준으로 삭제 
      id: id
    }
  }).then(() => res.status(204).send());	// no content = 204
});








// create : post
//   접수직원이 발신인, 수신인 정보 기입하여 배달접수
app.post('/users', (req, res)  => {
  const receiverName = req.body.receiverName || ''; // 없을시 빈문자열 추가
  //console.log("네임 : "+name);
  if (!receiverName.length) {
   return res.status(400).json({error: 'Incorrenct name'});
  }
  //   테이블에 데이터를 추가하는 기능 : 매개변수로 넣은 data를 객체형식으로 넘겨줌.
  models.User.create({
        receiverName: receiverName
        // receiverAddress : receiverAddress,
        // locationCode : locationCode,
        // receiverPhone : receiverPhone,
        // receiverQR : receiverQR,
        // senderPhone : senderPhone,
        // senderQR : senderQR,
        // regTime : regTime,
        // senderOpenTime : senderOpenTime,
        // senderCloseTime: senderCloseTime,
        // receiverOpenTime : receiverOpenTime,
        // receiverCloseTime :receiverCloseTime,
        // state : state,
        // companyKey : companyKey,
        // lockerNumber : Sequelize.INTEGER
  }).then((user) => res.status(201).json(user)) //   then함수->콜백함수의 user 파라미터로 테이블에 생선된row가나옴->이것을 요청한 ct에게 그대로 전달해주면됨

});





//  update : PUT
app.put('/users', (req, res) => {
  // id 받아옴
  const id_string = req.body.id || ''; 
  const id = parseInt(id_string, 10);
  if (!id) {
    return res.status(400).json({error: 'Incorrect id'});
  }



  //  receiveName 받아옴
  const receiverName = req.body.receiverName || ''; // 없을시 빈문자열 추가
  
  if (!receiverName.length) {
   return res.status(400).json({error: 'Incorrenct name'});
  }

  console.log(id);
  console.log(receiverName);


  // User.update({ nom: req.body.nom }, { where: {id: user.id} });
  models.User.update(
    
    {receiverName: receiverName},
    // id기준으로 수정
    { where: {id: id} }
    // receiverAddress : receiverAddress,
    // locationCode : locationCode,
    // receiverPhone : receiverPhone,
    // receiverQR : receiverQR,
    // senderPhone : senderPhone,
    // senderQR : senderQR,
    // regTime : regTime,
    // senderOpenTime : senderOpenTime,
    // senderCloseTime: senderCloseTime,
    // receiverOpenTime : receiverOpenTime,
    // receiverCloseTime :receiverCloseTime,
    // state : state,
    // companyKey : companyKey,
    // lockerNumber : Sequelize.INTEGER
).then((user) => res.status(201).json(user))
  
});

