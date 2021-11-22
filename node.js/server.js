const mysql = require("mysql");
const express = require("express");
const { DB } = require("./app/config/db.config");
const app = express();


var db = mysql.createConnection({
    host: "localhost",
    user: "root",
    password: "",
    database: "cov19"
})

db.connect((err) => {
    if(err) throw err;
    console.log("Connected to MySQL database 'cov19'!");

})

app.use(express.json());
app.use(express.urlencoded({extended: true}));


app.listen(3000, () => {
    console.log("Connected to Port 3000!")

});

app.get("/", (req,res) => {
    res.json({message: "Welcome! You are now connected to Plasma+ API."});
});


app.get("/createUserTable", (req,res) => {

    var sql = "CREATE TABLE IF NOT EXISTS users (id INT(12) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), phone VARCHAR(255), gender VARCHAR(255), blood_group VARCHAR(255), division VARCHAR(255), district VARCHAR(255), thana VARCHAR(255), age VARCHAR(255), donor VARCHAR(255), password VARCHAR(255))"
    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'users' created successfully!");
        res.status(200).send("TABLE 'users' created successfully!")
        
    });


})

app.get("/createTokenTable", (req, res) => {
    var sql = "CREATE TABLE IF NOT EXISTS tokens (phone VARCHAR(255), token VARCHAR(255))"

    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'tokens' created successfully!");
        res.status(200).send("TABLE 'tokens' created successfully!")
        
    });
})

app.get("/dropTables", (req,res) => {

    var sql = "DROP TABLE users";    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log(res);
        res.status(200).send("TABLE 'users' removed successfully!")
        
    });

})

app.get("/appVer.php", (req,res) => {

    res.status(200).json({serverMsg: "1.14"})
    
})

app.post("/checkUser.php", (req,res) => {

    if(req.body.phone == null){

        console.log({serverMsg: "Phone cannot be empty"})
        res.status(400).json({serverMsg: "Phone cannot be empty"})

    }
    else{

        var sql = "SELECT * FROM users WHERE phone=?"
        db.query(sql,[req.body.phone], (err, rows, fields) => {

            if(err) throw err;

            if(rows.length == 0){
                console.log({serverMsg: "record doesn't exist"})
                res.status(200).json({serverMsg: "record doesn't exist"})
            }
            else{
                console.log({serverMsg: "record exists"})
                res.status(200).json({serverMsg: "record exists"})

            }
        })
    }
    

})

app.post("/login.php", (req,res) => {

    if(req.body.phone == null || req.body.password == null){

        console.log({serverMsg: "Empty field"})
        res.status(200).json({serverMsg: "Wrong phone or password"})
    }
    else{
        var sql = "SELECT * FROM users WHERE phone = ? AND password = ?"
        db.query(sql, [req.body.phone, req.body.password], (err, rows) => {
    
            if (err) throw err;
            if(rows.length == 1){
                
                rows[0].serverMsg = "success"
                console.log(rows[0])
                res.status(200).json(rows[0])
        
            }
            else if(rows.length == 0){
    
                console.log({serverMsg: "Wrong phone or password"})
                res.status(200).json({serverMsg: "Wrong phone or password"})
        
            }
        })
    }
    
})

app.post("/registration.php", (req,res) => {

    var sql = "INSERT INTO users (name, phone, gender, blood_group, division, district, thana, age, donor, password) VALUES (?)"
    var values = 
    [req.body.name, 
        req.body.phone, 
        req.body.gender, 
        req.body.blood_group, 
        req.body.division, 
        req.body.district, 
        req.body.thana, 
        req.body.age, 
        req.body.donor, 
        req.body.password
    ]
    db.query(sql, [values], (err, data, fields) => {

        if(err) throw err;

        if(data.affectedRows == 1){
            res.status(200).json({serverMsg: "success"})

        }
        else{
            res.status(400).json({serverMsg: "failure"})

        }
        console.log({data: data, fields: fields})

    })
});


app.post("/tokenRegister.php", (req,result) => {


    var sql = "SELECT * FROM tokens WHERE phone = ?"
    db.query(sql, [req.body.phone], (err, rows) => {

        if(err) throw err;

        if(rows.length == 0){

            sql = "INSERT INTO tokens (phone, token) VALUES (?)"
            db.query(sql, [req.body.phone, req.body.token], (err, result) => {

                if(err) throw err;
                console.log({serverMsg: "Token Inserted"})
            
            })
            
         
        }
        else{

            sql = "UPDATE tokens SET token = ? WHERE phone = ?"
            db.query(sql, [req.body.token, req.body.phone], (err, result) => {

                console.log({serverMsg: "Token Updated"})

            })
        }

    })
})

app.post("imageDownload.php", (req, res) => {

    if(req.body.phone == null){

        console.log("Phone null")
        res.status(200).json({serverMsg: "false"})

    }
    else{
        console.log("Image for Phone found: "+req.body.phone)
        res.status(200).json({serverMsg: "false"})

    }


})

app.post("/dashboardNumbers.php", (req, res) => {

    if(req.body.phone == null){

        console.log("Phone null")

    }
    else{
        console.log("Dashboard Numbers for Phone found: "+req.body.phone)
        res.status(200).json({numberOfDonors: 0, numberOfPatients: 0, numberOfMyPatients: 0,
        numberOfRequestsFromDonors: 0, numberOfRequestsFromPatients: 0, 
        numberOfResponseFromDonors: 0, numberOfResponseFromPatients: 0})

    }

})

app.post("/donorEligibility.php", (req, res) => {

    if(req.body.phone == null){

        console.log("Phone null")
    }
    else{

        console.log("Eligibility for Phone found: "+req.body.phone)
        res.status(200).json({eligibility: "eligible", serverMsg: true})

    }
})

app.post("/checkNotification.php", (req, res) => {

    if(req.body.phone == null){

        console.log("Phone null")
    }
    else{

        console.log("Notification for Phone found: "+req.body.phone)
        res.status(200).json({serverMsg: "No Notifications"})

    }
})