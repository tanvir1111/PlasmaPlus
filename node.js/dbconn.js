var mysql = require("mysql")

var connection = mysql.createConnection({

    host: "localhost",
    user: "root",
    password: "",
    
})

connection.connect(function(err){

    if(err) throw err;
    console.log("CONNECTED!");
    var sql = "CREATE DATABASE cov19";
    connection.query(sql, function(err, result){

        if(err) throw err;
        console.log({message: "SUCCESSFUL"});
    })
})