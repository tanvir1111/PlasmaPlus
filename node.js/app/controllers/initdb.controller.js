const db = require("../models/db.js")


module.exports.createUserTable = (req,res) => {

    var sql = "CREATE TABLE IF NOT EXISTS users (id INT(12) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), phone VARCHAR(255), gender VARCHAR(255), blood_group VARCHAR(255), division VARCHAR(255), district VARCHAR(255), thana VARCHAR(255), age VARCHAR(255), donor VARCHAR(255), password VARCHAR(255), image VARCHAR(16384))"
    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'users' created successfully!");
        res.status(200).send("TABLE 'users' created successfully!")
        
    });


}

module.exports.createPatientTable = (req,res) => {

    var sql = "CREATE TABLE IF NOT EXISTS patients (id INT(12) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age VARCHAR(255), gender VARCHAR(255), blood_group VARCHAR(255), hospital VARCHAR(255), division VARCHAR(255), district VARCHAR(255), date VARCHAR(255), need VARCHAR(255), phone VARCHAR(255), amountOfBloodNeeded VARCHAR(255))"
    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'patients' created successfully!");
        res.status(200).send("TABLE 'patients' created successfully!")
        
    });


}

module.exports.createTokenTable = (req, res) => {
    var sql = "CREATE TABLE IF NOT EXISTS tokens (phone VARCHAR(255), token VARCHAR(255))"

    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'tokens' created successfully!");
        res.status(200).send("TABLE 'tokens' created successfully!")
        
    });
}


module.exports.dropTables = (req,res) => {

    var sql = "DROP TABLE users";    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log(res);
        res.status(200).send("TABLE 'users' removed successfully!")
        
    });

}