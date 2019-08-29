var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.status(200).send('respond with a resource Youna');
});

router.use('/user', require('./user'));
router.use('/user', require('./user'));

module.exports = router;
