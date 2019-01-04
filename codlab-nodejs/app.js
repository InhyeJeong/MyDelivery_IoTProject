// 익스프레스로 서버 설정 및 구동

// import
const express = require('express');
const app = express();
const bodyParser = require('body-parser');

//  router import
app.use('/users', require('./api/users/index.js'));


// body parser : �̵����
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));




// connect success
app.listen(3000, () => {
  console.log('Example app listening on port 3000!');
});
