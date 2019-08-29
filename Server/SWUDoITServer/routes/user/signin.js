var express = require('express');
var router = express.Router();
const async = require('async');
const crypto = require('crypto-promise');

const utils = require('../utils/utils');
const statusCode = require('../utils/statusCode');
const responseMessage = require('../utils/responseMessage');
const db = require('../../module/pool');

router.post('/', async (req, res)=>{
    if (!req.body.id || !req.body.password) {
        res.status(200).send(utils.successFalse(statusCode.BAD_REQUEST),responseMessage.NULL_VALUE);
    } else {
        const userInfo = {
            id:req.body.id,
            password:req.body.password
        }
        const selectUserQuery='SELECT id, password FROM user WHERE id=?';
        const selectUserResult=await db.queryParam_Parse(selectUserQuery,req.body.id);
        if(!selectUserResult){
            res.status(400).send(utils.successFalse(statusCode.INTERNAL_SERVER_ERROR,responseMessage.LOGIN_FAIL));
        }
        else{
            userInfo.password=selectUserResult[0].password;
            const returnData={
                studentIdx:selectUserResult[0].studentIdx
            }

            if(userInfo.password!=req.body.password){ //비밀번호 DB값과 다른 경우
                res.status(500).send(utils.successTrue(statusCode.INTERNAL_SERVER_ERROR, responseMessage.MISS_MATCH_PW));
            }else{ //비밀번호 DB값과 일치하는 경우
                res.status(200).send(utils.successTrue(statusCode.OK, responseMessage.LOGIN_SUCCESS,returnData));
            }
            
    
        }


    }
    
});
module.exports = router;