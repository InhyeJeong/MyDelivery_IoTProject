//  모델
//  import
const Sequelize = require('sequelize');
const config = require('./config/environment.js');
//  객체 생성(db name, 접속 계정명, pw)
const sequelize = new Sequelize(
    config.mysql.database,
    config.mysql.username,
    config.mysql.password, {
    host : 'localhost',
    dialect : 'mysql',
    pool: {
        max: 5,
        min: 0,
        acquire: 30000,
        idle: 10000
        },
        
        // http://docs.sequelizejs.com...
        operatorsAliases: false      
});


//  모델 생성( db에 만들어질 테이블 이름, 테이블 세부사항을 객체형식으로 정의)
const User = sequelize.define('delivery_test', {
    receiverName: Sequelize.STRING  //  name값이 문자열임을 정의 (id는 기본으로 만들어줌)
    // receiverAddress : Sequelize.STRING,
    // locationCode : Sequelize.STRING,
    // receiverPhone : Sequelize.STRING,
    // receiverQR : Sequelize.STRING,
    // senderPhone : Sequelize.STRING,
    // senderQR : Sequelize.STRING,
    // regTime : Sequelize.DATE,
    // senderOpenTime : Sequelize.DATE,
    // senderCloseTime : Sequelize.DATE,
    // receiverOpenTime : Sequelize.DATE,
    // receiverCloseTime : Sequelize.DATE,
    // state : Sequelize.STRING,
    // companyKey : Sequelize.STRING,
    // lockerNumber : Sequelize.INTEGER
  });

//  모듈로 만들기(외부에서 사용하기 위해서)
module.exports = {
    sequelize: sequelize,
    User: User
  }