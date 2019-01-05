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

//  test 예시
let users = [
  {
    id: 1,
    name: 'alice'
  },
  {
    id: 2,
    name: 'bek'
  },
  {
    id: 3,
    name: 'chris'
  }
]


