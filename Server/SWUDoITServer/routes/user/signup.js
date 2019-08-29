var express = require('express');
var router = express.Router();

const utils=require('../utils/utils');
const responseMessage=require('../utils/responseMessage');
const statusCode=require('../utils/statusCode');
const db=require('../../module/pool');

const pool = require('../../config/dbconfig');

router.post('/', async(req, res)=>{
    console.log(789);
if(!req.body.id){
    res.status(200).send(utils.successFalse(statusCode.BAD_REQUEST),responseMessage.NULL_VALUE);
}
else{
    const userInfo={
        id:req.body.id,
        password: req.body.password
    }

    //const userSelectQuery='SELECT * FROM user WHERE id=?'; //user 중복 여부 확인
    const userInsertQuery='INSERT INTO user(id, password) VALUES (?, ?)';
    const userInsertResult=await db.queryParam_Parse(userInsertQuery, [userInfo.id, userInfo.password])

    //const userSelectResult=await db.queryParam_Parse(userSelectQuery, req.body.id);

    // if(!(userSelectResult[0]==undefined)){
    //     res.status(400).send(utils.successFalse(statusCode.BAD_REQUEST, responseMessage.ALREADY_USER));
    // } else{
    //     const userInsertResult=await db.queryParam_Parse(userInsertQuery,[userInfo.id,userInfo.password])
    //     if (!userInsertResult) {
    //         res.status(200).send(utils.successFalse(statusCode.INTERNAL_SERVER_ERROR, responseMessage.OUT_OF_VALUE));
    //     } else {
    //         res.status(200).send(utils.successTrue(statusCode.OK, responseMessage.CREATED_USER));
    // }
    // }

    if (!userInsertResult) {
        res.status(200).send(utils.successFalse(statusCode.INTERNAL_SERVER_ERROR, responseMessage.CREATED_USER));
    } else {
        res.status(200).send(utils.successTrue(statusCode.OK, responseMessage.OUT_OF_VALUE));
    }
}
});

module.exports = router;
