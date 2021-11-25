const db = require("../models/db.js")



module.exports.receiveRequest = (req, res) => {

    console.log(req.body.donorPhone+req.body.patientName+req.body.patientAge+ 
        req.body.patientBloodGroup+req.body.requestedBy)

    if(req.body.operation == "getStatus"){

        var sql = "SELECT * FROM requests WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND requestedBy = ?"
    
        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGroup, req.body.requestedBy], (err, result) => {
    
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

    
    else if(req.body.operation == "accept"){

        var sql = "UPDATE requests SET status = 'Accepted' WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientDate = ? AND patientPhone = ? AND patientNeed = ? AND requestedBy = ?"
    
        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGrp, req.body.patientDate, req.body.patientPhone. 
            req.body.patientNeed, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.affectedRows > 0){

                    console.log({serverMsg: "Accepted"})
                    res.status(200).json({serverMsg: "Accepted"})
                }

                else{

                    console.log({serverMsg: "Failed to accept request"})
                    res.status(200).json({serverMsg: "Pending"})
                }

        })
        
    }

    else if(req.body.operation == "decline"){
        
        var sql = "UPDATE requests SET status = 'Declined' WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientDate = ? AND patientPhone = ? AND patientNeed = ? AND requestedBy = ?"
    
        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGrp, req.body.patientDate, req.body.patientPhone. 
            req.body.patientNeed, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.affectedRows > 0){

                    console.log({serverMsg: "Declined"})
                    res.status(200).json({serverMsg: "Declined"})
                }

                else{

                    console.log({serverMsg: "Failed to decline request"})
                    res.status(200).json({serverMsg: "Pending"})
                }

        })
    }

    else if(req.body.operation == "confirm"){
        
    }

    else if(req.body.operation == "not_confirm"){
        
    }

    else if(req.body.operation == "claim"){
        
    }

    else if(req.body.operation == "not_donate"){
        
    }

    else if(req.body.operation == "cancel"){
        
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
                    req.body.patientNeed, req.body.requestedBy, req.body.operation]
            
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

module.exports.requestsFromDonorsAlpha = (req, res) => {


    var sql = "SELECT * FROM requests"

    if(req.body.status == "any"){
        sql = "SELECT * FROM requests WHERE patientPhone = '"+req.body.phone+"' AND requestedBy = 'donor'"

    }
    else if(req.body.status == "Pending"){
        sql = "SELECT * FROM requests WHERE patientPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Pending'"

    }
    else if(req.body.status == "Successful"){
        sql = "SELECT * FROM requests WHERE patientPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Accepted' OR status = 'Claimed' OR status = 'Confirmed'"

    }
    else if(req.body.status == "Failed"){
        sql = "SELECT * FROM requests WHERE patientPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Declined' OR status = 'Canceled' OR status = 'Not_Confirmed'"

    }

    db.query(sql, (err, result) => {

        if(err) throw err

        if(result.length > 0){

            var responseValues = []

            for(var i=0;i<result.length;i++){

                var sql2 = "SELECT * FROM patients WHERE phone = ?"
    
                db.query(sql2, [result[i].patientPhone], (err, result2) => {
            
                    if(err) throw err
        
                    if(result2.length > 0){
        
                        responseValues.push(result2[0])
                        
                        if(i == result.length){

                            console.log(responseValues)
                            res.status(200).json(responseValues)
                        }
                    }
                    else{
                        console.log({serverMsg: "No Record"})
                        res.status(200).json([])

                    }
                })
            }

            
        }
        else{

            console.log({serverMsg: "No Record"})
            res.status(200).json([])

        }
    
    })
    
}

module.exports.requestsFromDonorsBeta = (req, res) => {

    var sql = "SELECT * FROM requests"

    if(req.body.status == "any"){
        sql = "SELECT * FROM requests WHERE patientName = '" + req.body.name + "' AND patientAge = '" + req.body.age + "' AND patientBloodGrp = '" + req.body.bloodGroup + "' AND patientPhone = '"+req.body.phone+"' AND requestedBy = 'donor'"

    }
    else if(req.body.status == "Pending"){
        sql = "SELECT * FROM requests WHERE patientName = '" + req.body.name + "' AND patientAge = '" + req.body.age + "' AND patientBloodGrp = '" + req.body.bloodGroup + "' AND patientPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Pending'"

    }
    else if(req.body.status == "Successful"){
        sql = "SELECT * FROM requests WHERE patientName = '" + req.body.name + "' AND patientAge = '" + req.body.age + "' AND patientBloodGrp = '" + req.body.bloodGroup + "' AND patientPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Accepted' OR status = 'Claimed' OR status = 'Confirmed'"

    }
    else if(req.body.status == "Failed"){
        sql = "SELECT * FROM requests WHERE patientName = '" + req.body.name + "' AND patientAge = '" + req.body.age + "' AND patientBloodGrp = '" + req.body.bloodGroup + "' AND patientPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Declined' OR status = 'Canceled' OR status = 'Not_Confirmed'"

    }

    db.query(sql, (err, result) => {

        if(err) throw err

        if(result.length > 0){

            var responseValues = []

            for(var i=0;i<result.length;i++){

                var sql2 = "SELECT * FROM users WHERE phone = ?"
                var serverMsg = result[i].status
    
                db.query(sql2, [result[i].donorPhone], (err, result2) => {
            
                    if(err) throw err
        
                    if(result2.length > 0){
        
                        result2[0].serverMsg = serverMsg
                        responseValues.push(result2[0])
                        
                        if(i == result.length){

                            console.log(responseValues)
                            res.status(200).json(responseValues)
                        }
                    }
                    else{
                        console.log({serverMsg: "No Record"})
                        res.status(200).json([])
                    }
                })
            }

        }

        else{

            console.log({serverMsg: "No Record"})
            res.status(200).json([])

        }
    })

    
}

module.exports.requestsFromPatients = (req, res) => {

    var sql = "SELECT * FROM requests"

    if(req.body.status == "any"){
        sql = "SELECT * FROM requests WHERE donorPhone = '"+req.body.phone+"' AND requestedBy = 'patient'"

    }
    else if(req.body.status == "Pending"){
        sql = "SELECT * FROM requests WHERE donorPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Pending'"

    }
    else if(req.body.status == "Successful"){
        sql = "SELECT * FROM requests WHERE donorPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Accepted' OR status = 'Claimed' OR status = 'Confirmed'"

    }
    else if(req.body.status == "Failed"){
        sql = "SELECT * FROM requests WHERE donorPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Declined' OR status = 'Canceled' OR status = 'Not_Confirmed'"

    }

    db.query(sql, (err, result) => {

        if(err) throw err

        if(result.length > 0){

            var responseValues = []

            for(var i=0;i<result.length;i++){

                var sql2 = "SELECT * FROM patients WHERE phone = ?"
                var serverMsg = result[i].status

                db.query(sql2, [result[i].patientPhone], (err, result2) => {
            
                    if(err) throw err
        
                    if(result2.length > 0){
        
                        result2[0].serverMsg = serverMsg
                        responseValues.push(result2[0])
                        
                        if(i == result.length){

                            console.log(responseValues)
                            res.status(200).json(responseValues)
                        }
                    }
                    else{
                        console.log({serverMsg: "No Record"})
                        res.status(200).json([{serverMsg: "No Record"}])

                    }
                })
            }

            
        }
        else{

            console.log({serverMsg: "No Record"})
            res.status(200).json([{serverMsg: "No Record"}])

        }
    
    })

    
}