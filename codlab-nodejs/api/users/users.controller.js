//  실제 로직이 들어간 컨트롤러


// index : get/view all
exports.index = (req, res) => {
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
  if (!user) {
    return res.status(404).json({error: 'Unknown user'});
  }
  
  return res.json(user);
};



// destroy : delete
exports.destroy = (req, res) => {
  const id = parseInt(req.params.id, 10);
  if (!id) {
    return res.status(400).json({error: 'Incorrect id'});
  }

  const userIdx = users.findIndex(user => user.id === id);
  if (userIdx === -1) {
    return res.status(404).json({error: 'Unknown user'});
  }

  users.splice(userIdx, 1);
  res.status(204).send();	// no content = 204
};


// create : post
exports.create = (req, res) => {
  const name = req.body.name || ''; // 없을시 빈문자열 추가
  //console.log("네임 : "+name);
  if (!name.length) {
   return res.status(400).json({error: 'Incorrenct name'});
  }
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