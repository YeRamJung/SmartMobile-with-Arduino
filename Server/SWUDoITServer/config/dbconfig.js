const mysql = require('promise-mysql');

const dbConfig = {
    host: 'swudoit.ccpmv9cfdrk5.ap-northeast-2.rds.amazonaws.com',
    port: 3306,
    user: 'swudoit',
    password: 'swudoit19',
    database: 'SWUDoIT'
}

module.exports = mysql.createPool(dbConfig);