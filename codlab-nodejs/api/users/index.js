//  index.js는 모듈

// import
const express = require('express');

// router
const router = express.Router();
module.exports = router;


// controller사용
const controller = require('./users.controller');

router.get('/', controller.index);

router.get('/:id', controller.show);

router.delete('/:id', controller.destroy);

router.post('/', controller.create);

router.put('/:id', controller.update);

//  test 예시
let users = [
  {
    id: 1,
    receiverName: 'alice',
    receiverAddress : 'Seoul',
    locationCode : 1,
    receiverPhone : '010-1234-5678',
    senderPhone : '010-7777-7777',
    companyKey : 1
  },
  {
    id: 2,
    receiverName: 'bek',
    receiverAddress : 'Busan',
    locationCode : 2,
    receiverPhone : '010-2341-5678',
    senderPhone : '010-7777-7777',
    companyKey : 2
  },
  {
    id: 3,
    receiverName: 'chris',
    receiverAddress : 'Daegu',
    locationCode : 3,
    receiverPhone : '010-3124-5678',
    senderPhone : '010-7777-7777',
    companyKey : 3
  }
]


