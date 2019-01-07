//  test와 dev test 환경 분리하기

const environments = {  // 각 환경 이름에 해당하는 키
    development: {
      mysql: {
        username: 'root',
        password: '',
        database: 'node_api_codelab_dev'
      }
    },
  
    test: {
      mysql: {
        username: 'root',
        password: '',
        database: 'node_api_codelab_test'
      }
    },
  
    production: {
  
    }
  }
  
  //    노드 환경변수 값 할당
  const nodeEnv = process.env.NODE_ENV || 'development';
  //    노드 환경변수에 해당하는 부분의 객체를 반환하는 모듈
  module.exports = environments[nodeEnv];