const db = require("../models/db.js")


module.exports.login = (req,res) => {

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
    
}

module.exports.registration = (req,res) => {

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
}

module.exports.checkUser = (req,res) => {

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
    

}

module.exports.tokenRegister = (req,res) => {


    var sql = "SELECT * FROM tokens WHERE phone = ?"
    db.query(sql, [req.body.phone], (err, rows) => {

        if(err) throw err;

        if(rows.length == 0){

            sql = "INSERT INTO tokens (phone, token) VALUES (?)"
            var values = [req.body.phone, req.body.token]
            db.query(sql, [values], (err, result) => {

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
}