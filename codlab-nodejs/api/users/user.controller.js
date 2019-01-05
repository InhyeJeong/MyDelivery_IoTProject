


// index : get/view all
exports.index = (req, res) => {
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
  const name = req.body.name || '';
  if (!name.length) {
   return res.status(400).json({error: 'Incorrenct name'});
  }
  // data를 축적함. reduce()함수
   const id = users.reduce((maxId, user) => {
    return user.id > maxId ? user.id : maxId
   }, 0) + 1;	// 새로운 유저가 추가되었으니 뒤에 +1

  // 
  const newUser = {
   id: id,
   name: name
  };

  users.push(newUser); // 새로운 유저 생성
  // 201 Created : 성공 !
  return res.status(201).json(newUser);
};