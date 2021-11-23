const db = require("../models/db.js")



module.exports.receiveRequest = (req, res) => {

    console.log({serverMsg: "called"})

    if(req.body.operation == "getStatus"){

        var sql = "SELECT * FROM requests WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND requestedBy = ?"
    
        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGrp, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.length > 0){

                    console.log({serverMsg: result[0].status})
                    res.status(200).json({serverMsg: result[0].status})
                }

                else{

                    console.log({serverMsg: "no requests"})
                    res.status(200).json({serverMsg: "no requests"})
                }

        })
    }
    

}
module.exports.sendRequest = (req, res) => {


    var sql = "SELECT * FROM requests WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientPhone = ?"
    
    db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
        req.body.patientBloodGrp, req.body.patientPhone], (err, result) => {

            if(err) throw err

            if(result.length > 0){

                console.log({serverMsg: "This Request Already exists! Wait for Response"})
                res.status(200).json({serverMsg: "This Request Already exists! Wait for Response"})
            }

            else{
                var sql2 = "INSERT INTO requests (donorPhone, patientName, patientAge, patientBloodGrp, patientDate, patientPhone, patientNeed, requestedBy, status) VALUES (?)"

                var values2 = [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
                    req.body.patientBloodGrp, req.body.patientDate, req.body.patientPhone, 
                    req.body.patientNeed, req.body.requestedBy, req.body.status]
            
                db.query(sql2,[values2], (err, result2) => {
            
                    if(err) throw err
            
                    if(result2.affectedRows == 1){
            
                        console.log({serverMsg: "Request Inserted"})
                        res.status(200).json({serverMsg: "Success"})
            
                    }
                    else{
                        console.log({serverMsg: "Request Inserting Failed"})
                        res.status(200).json({serverMsg: "Failure"})
            
                    }
                })
            }
        })
   
}