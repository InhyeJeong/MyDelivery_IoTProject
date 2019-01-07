//  실제 로직이 들어간 컨트롤러

//  import
const models = require('../../models');

// index : get/view all
exports.index = (req, res) => {
  //  전체 테이블 불러오기
  models.User.findAll()
      .then(users => res.json(users));

  console.log("catch");
  //  test예시 주석처리 return res.json({"msg":"hihi"}); 
  return res.json(users);
};

// show : get/findById
exports.show = (req, res) => {
   const id = parseInt(req.params.id, 10);
  if (!id) {
    return res.status(400).json({error: 'Incorrect id'});
  }

  let user = users.filter(user => user.id === id)[0] 

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
};



// destroy : delete
exports.destroy = (req, res) => {
  const id = parseInt(req.params.id, 10);
  if (!id) {
    return res.status(400).json({error: 'Incorrect id'});
  }

  // const userIdx = users.findIndex(user => user.id === id);
  // if (userIdx === -1) {
  //   return res.status(404).json({error: 'Unknown user'});
  // }

  // users.splice(userIdx, 1);
 
  models.User.destroy({
    where: {// id기준으로 삭제 
      id: id
    }
  }).then(() => res.status(204).send());	// no content = 204
};


// create : post
exports.create = (req, res) => {
  const name = req.body.name || ''; // 없을시 빈문자열 추가
  //console.log("네임 : "+name);
  if (!name.length) {
   return res.status(400).json({error: 'Incorrenct name'});
  }
  //   테이블에 데이터를 추가하는 기능 : 매개변수로 넣은 data를 객체형식으로 넘겨줌.
  models.User.create({
    name: name  //  상수값
  }).then((user) => res.status(201).json(user)) //   then함수->콜백함수의 user 파라미터로 테이블에 생선된row가나옴->이것을 요청한 ct에게 그대로 전달해주면됨
  
  // data를 축적하는 reduce() 함수
   const id = users.reduce((maxId, user) => {
    return user.id > maxId ? user.id : maxId
   }, 0) + 1;	// 새로운 유저 생성이니 뒤에 추가, +1

  
  const newUser = {
   id: id,
   name: name
  };
   
  users.push(newUser); // 새로운 유저 생성
  // 201 Created : 성공 !
  return res.status(201).json(newUser);
};

//  update : PUT
exports.update = (req, res) => {
  //  요청이 들어오면 send() 함수를 이용해 200 상태코드만 응답
  res.send(); 
}