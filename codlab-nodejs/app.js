'use strict';
// 익스프레스로 서버 설정 및 구동

// import
const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const date = require('date-utils');
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

  require('./models').sequelize.sync({force: false})  // 개발시에는 true(새로갱신함) // 배포시에는 false
  .then(() => {
    console.log('---------------Database sync');
  });
 
});

//  Supertest사용을 위해 app변수를 외부 '노출'하여 '모듈'로 만들어주기
//  Supertest를 사용하려면 서버 역할을 하는 익스프레스 객체를 가져와함
module.exports = app;








//------------------------------★컨트롤러★-----------------------------------------------
//  import
const models = require('./models');

//  senderQR 랜덤 생성 코드 필요
//   1. 접수직원 / create : post ♥성공
app.post('/users', (req, res)  => {

//  랜덤 QR코드 생성
  var randomQR = '';
  

for (var i = 0; i < 20; i++){
    var rIndex = Math.floor(Math.random() * 3);
    switch (rIndex) {
    case 0:
        // a-z
        randomQR+=String( ( (Math.floor(Math.random() * 26))+ 97) );
        //randomQR.append((char) ((int) (rnd.nextInt(26)) + 97));
        break;
    case 1:
        // A-Z
        randomQR+= String( ( (Math.floor(Math.random() * 26)) + 65) );
        //randomQR.append((char) ((int) (rnd.nextInt(26)) + 65));
        break;
    case 2:
        // 0-9
        randomQR+=(Math.floor(Math.random() * 10));
        //randomQR.append((rnd.nextInt(10)));
        break;
    };
};

  const receiverName = req.body.receiverName || ''; // 없을시 빈문자열 추가
  const receiverAddress = req.body.receiverAddress || '';
  const receiverPhone = req.body.receiverPhone || '';
  const senderPhone = req.body.senderPhone ||'';
  const companyKey = req.body.companyKey || '';
  const senderQR =randomQR;
  const locationCode = req.body.locationCode || '';

  //  에러코드
  if (!receiverName.length) {
   return res.status(400).json({error: 'Incorrenct name'});
  }
  if (!receiverAddress.length) {
   return res.status(400).json({error: 'Incorrenct Address'});
  }
  if (!receiverPhone.length) {
   return res.status(400).json({error: 'Incorrenct receiverPhone'});
  }
  if (!senderPhone.length) {
   return res.status(400).json({error: 'Incorrenct senderPhone'});
  }
  if (!companyKey.length) {
   return res.status(400).json({error: 'Incorrenct companyKey'});
  }
  if (!locationCode.length) {
    return res.status(400).json({error: 'Incorrenct locationCode'});
   }
  //   테이블에 데이터를 추가하는 기능 : 매개변수로 넣은 data를 객체형식으로 넘겨줌.
  models.User.create({
        receiverName: receiverName,
        receiverAddress : receiverAddress,
        locationCode : locationCode,
        receiverPhone : receiverPhone,
        receiverQR : "",
        senderPhone : senderPhone,
        senderQR : senderQR,
        senderOpenTime : null,
        senderCloseTime: null,
        receiverOpenTime : null,
        receiverCloseTime : null,
        state : "0", // registration
        companyKey : companyKey,
        lockerNumber : -1
  }).then((user) => res.status(201).json(user)) //   then함수->콜백함수의 user 파라미터로 테이블에 생선된row가나옴->이것을 요청한 ct에게 그대로 전달해주면됨

});



//  2.택배보관리스트 확인 / index : get/view all ♥성공
app.get('/sender/:senderPhone', (req, res) => {
    //  전체 테이블 불러오기

    const senderPhone = req.params.senderPhone ||'';

  //  특정 컬럼 불러올때는 attributes(select)사용하면 됨!
  models.User.findAll(
     //  phoneNumber 기준으로 수정
    {where: {senderPhone: senderPhone}, attributes: ['receiverName', 'receiverAddress','receiverPhone','senderQR','state' ,'senderOpenTime','senderCloseTime','receiverOpenTime', 'receiverCloseTime','createdAt']}
   
    )
  .then(user => res.json(user));
    
  });




//  3.택배수신리스트 확인 ♥성공
app.get('/receiver/:receiverPhone', (req, res) => {
  const receiverPhone = req.params.receiverPhone ||'';
  //  전체 테이블 불러오기
models.User.findAll(
  {//  receiverNumber 기준으로 수정
    where: {receiverPhone: receiverPhone},
    attributes: ['senderName', 'senderPhone','state','senderCloseTime','receiverCloseTime', 'receiverQR']

  })
.then(user => res.json(user));
  
});




// 4. 발신인 OPEN / update : PUT  ♥성공
app.put('/senderOpen', (req, res) => {
  const locationCode = req.body.locationCode || '';
  const senderQR = req.body.senderQR || '';
  const lockerNumber = req.body.lockerNumber || '';

  //  timezone 수정
  var loadDt = new Date();
  const newDate = new Date(Date.parse(loadDt) + 1000 * 60 * 60 * 9);
  const now_time = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');

  const senderOpenTime = now_time;
  //  에러코드
  if (!locationCode.length) {
    return res.status(400).json({error: 'Incorrect locationCode'});
  }
  if (!senderQR.length) {
    return res.status(400).json({error: 'Incorrect senderQR'});
  }
  if (!lockerNumber.length) {
    return res.status(400).json({error: 'Incorrect lockerNumber'});
  }
  if (!senderOpenTime.length) {
    return res.status(400).json({error: 'Incorrect senderOpenTime'});
  }

  // User.update({ nom: req.body.nom }, { where: {id: user.id} });
  models.User.update({
    lockerNumber : lockerNumber,
    senderOpenTime : senderOpenTime,
  },

    //  senderQR기준으로 수정
    { where: {senderQR : senderQR,
              locationCode : locationCode} }
   
).then((user) => res.status(201).json(user))
  
});



//  5. 발신인 CLOSE / update : PUT ♥성공
//  receiverQR 랜덤 생성 코드 필요
app.put('/senderClose', (req, res) => {
  //  랜덤 QR코드 생성
  var randomQR = '';
  

for (var i = 0; i < 20; i++){
    var rIndex = Math.floor(Math.random() * 3);
    switch (rIndex) {
    case 0:
        // a-z
        randomQR+=String( ( (Math.floor(Math.random() * 26))+ 97) );
        //randomQR.append((char) ((int) (rnd.nextInt(26)) + 97));
        break;
    case 1:
        // A-Z
        randomQR+= String( ( (Math.floor(Math.random() * 26)) + 65) );
        //randomQR.append((char) ((int) (rnd.nextInt(26)) + 65));
        break;
    case 2:
        // 0-9
        randomQR+=(Math.floor(Math.random() * 10));
        //randomQR.append((rnd.nextInt(10)));
        break;
    };
};


  const locationCode = req.body.locationCode || '';
  const receiverQR = randomQR;
  const senderQR = req.body.senderQR || '';
 // const state = req.body.state || '';
  var loadDt = new Date();
  const newDate = new Date(Date.parse(loadDt) + 1000 * 60 * 60 * 9);
  const now_time = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');

  const senderCloseTime = now_time;

  //  에러코드
  if (!receiverQR.length) {
    return res.status(400).json({error: 'Incorrect receiverQR'});
  }
  if (!locationCode.length) {
    return res.status(400).json({error: 'Incorrect locationCode'});
  }
  if (!senderQR.length) {
    return res.status(400).json({error: 'Incorrect senderQR'});
  }
  // if (!state.length) {
  //   return res.status(400).json({error: 'Incorrect state'});
  // }
  if (!senderCloseTime.length) {
    return res.status(400).json({error: 'Incorrect senderCloseTime'});
  }

  // User.update({ nom: req.body.nom }, { where: {id: user.id} });
  models.User.update({
    receiverQR : receiverQR,
    senderCloseTime : senderCloseTime,
    state : "1",  //  locked
    senderQR : "" //  발신인이 CLOSE했을 때, 발신인 QR코드 없앰
    },
    
    //  senderQR기준으로 수정
    { where: {senderQR : senderQR,
      locationCode : locationCode} }
   
).then((user) => res.status(201).json(user))
  
});

//  6. 수신인 OPEN / update : PUT ♥성공
app.put('/receiverOpen', (req, res) => {

  const receiverQR = req.body.receiverQR || '';
  const locationCode =  req.body.locationCode || '';
  const lockerNumber = req.body.lockerNumber || '';

  var loadDt = new Date();
  const newDate = new Date(Date.parse(loadDt) + 1000 * 60 * 60 * 9);
  const now_time = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');

  const receiverOpenTime = now_time;

  //  에러코드
  if (!receiverQR.length) {
    return res.status(400).json({error: 'Incorrect receiverQR'});
  }
  if (!locationCode.length) {
    return res.status(400).json({error: 'Incorrect locationCode'});
  }
  if (!lockerNumber.length) {
    return res.status(400).json({error: 'Incorrect lockerNumber'});
  }
  if (!receiverOpenTime.length) {
    return res.status(400).json({error: 'Incorrect receiverOpenTime'});
  }

  models.User.update({
    receiverOpenTime : receiverOpenTime//현재시간
    },
    
    //  receiverQR기준으로 수정
    { where: {receiverQR : receiverQR,
    locationCode : locationCode} }
   
).then((user) => res.status(201).json(user))
  
});



//  7. 수신인 CLOSE / update : PUT ♥성공
app.put('/receiverClose', (req, res) => {

  const receiverQR = req.body.receiverQR || '';
  const locationCode =  req.body.locationCode || '';

  var loadDt = new Date();
  const newDate = new Date(Date.parse(loadDt) + 1000 * 60 * 60 * 9);
  const now_time = newDate.toFormat('YYYY-MM-DD HH24:MI:SS');

  const receiverCloseTime = now_time;

  //  에러코드
  if (!receiverQR) {
    return res.status(400).json({error: 'Incorrect receiverQR'});
  }
  if (!locationCode) {
    return res.status(400).json({error: 'Incorrect locationCode'});
  }
  if (!receiverCloseTime) {
    return res.status(400).json({error: 'Incorrect receiverCloseTime'});
  }

  // User.update({ nom: req.body.nom }, { where: {id: user.id} });
  models.User.update({
    receiverCloseTime : receiverCloseTime,//현재시간
    state : "2",  //  received
    receverQR : ""  //  수신인이 CLOSE했을때 수신인 QR코드 없앰
    },
    
    //  receiverQR기준으로 수정
    { where: {receiverQR : receiverQR,
    locationCode : locationCode} }
   
).then((user) => res.status(201).json(user))
  
});





//------------------------아래는 원본 CRUD함수-------------------//


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
    
    {receiverName: receiverName,
     receiverAddress : receiverAddress,
    locationCode : locationCode,
    receiverPhone : receiverPhone,
    receiverQR : receiverQR,
    senderPhone : senderPhone,
    senderQR : senderQR,
    senderOpenTime : senderOpenTime,
    senderCloseTime: senderCloseTime,
    receiverOpenTime : receiverOpenTime,
    receiverCloseTime :receiverCloseTime,
    state : state,
    companyKey : companyKey,
    lockerNumber : Sequelize.INTEGER
    },
    // id기준으로 수정
    { where: {id: id} }
   
).then((user) => res.status(201).json(user))
  
});

