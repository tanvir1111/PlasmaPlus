const db = require("../models/db.js")
const admin = require("../models/firebase.js")
var async = require('async')

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
            req.body.patientBloodGroup, req.body.patientDate, req.body.patientPhone, 
            req.body.patientNeed, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.affectedRows > 0){

                    console.log({serverMsg: "Accepted"})
                    res.status(200).json({serverMsg: "Accepted"})

                    var sql2 = "UPDATE users SET eligibility = 'not_eligible' WHERE phone = ?"
                    db.query(sql2, [req.body.donorPhone], (err, result2) => {
            
                        if(err) throw err
                        if(result2.affectedRows > 0){
            
                            console.log({serverMsg: "Donor "+req.body.donorPhone+" is now set as not_eligible"})
                            

                            var sql3 = "SELECT * FROM tokens WHERE phone IN (SELECT patientPhone AS phone FROM requests WHERE donorPhone = ? AND patientPhone != ? AND status = 'Pending')"
                            db.query(sql3,[req.body.donorPhone, req.body.patientPhone], (err, result3) => {
                            
                                if(err) throw err
                            
                                if(result3.length > 0){
                        
                                var length = result3.length
                        
                                async.forEachOf(result3, (result, i, callback) => {
                                    var message;
                                    if(req.body.requestedBy = "patient"){
                                        message = {
                                
                                            notification: {
                                                title: "Request declined from donor!",
                                                body: "Your Request has been declined by donor."
                                                },
                                            data: {
                                                activity: "DonorResponseActivity",
                                                hidden: ""
                                            },
                                        
                                            token: result.token
                                        
                                            }
                                    }
                                    else if(req.body.requestedBy = "donor"){
                                        message = {
                                
                                            notification: {
                                                title: "Request declined from patient!",
                                                body: "Your Request has been declined by patient."
                                                },
                                            data: {
                                                activity: "PatientResponseActivity",
                                                hidden: ""
                                            },
                                        
                                            token: result.token
                                        
                                            }
                                    }
                                    
                                    admin.messaging().send(message)
                                    .then((response) => {
                                
                                    console.log('Successfully sent message:', response)
                                
                                    if(i == length-1){
                        
                                        var sql4 = "UPDATE requests SET status = 'Declined' WHERE donorPhone = ? AND status = 'Pending'"
                        
                        
                                        db.query(sql4, [req.body.donorPhone], (err, result) => {
                                    
                                                if(err) throw err
                                                if(result.affectedRows > 0){
                                
                                                    console.log({serverMsg: "Declined"})
                        
                                                }
                                
                                                else{
                                
                                                    console.log({serverMsg: "Failed to decline request"})
                                                }
                                
                                        })                    
                        
                                    }
                                    })
                                    .catch((error) => {
                                    console.log('Error sending message:', error)
                                
                                    })
                        
                                }, 
                                function(err){
                                    if(err) throw err
                                })
                                }
                        
                                else{
                                console.log('No token found')
                                }
                        
                        
                            })
                        
                            
        
            
                        }
                        else{
                            console.log({servermsg: "Donor eligibility change failed"})
                        }
                    })

                   

                      

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
            req.body.patientBloodGroup, req.body.patientDate, req.body.patientPhone, 
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

    else if(req.body.operation == "cancel"){
        
        var sql = "UPDATE requests SET status = 'Canceled' WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientDate = ? AND patientPhone = ? AND patientNeed = ? AND requestedBy = ?"

        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGroup, req.body.patientDate, req.body.patientPhone, 
            req.body.patientNeed, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.affectedRows > 0){

                    console.log({serverMsg: "Canceled"})
                    res.status(200).json({serverMsg: "Canceled"})

                    var sql2 = "UPDATE users SET eligibility = 'eligible' WHERE phone = ?"
                    db.query(sql2, [req.body.donorPhone], (err, results) => {

                        if(err) throw err
                        if(result.affectedRows > 0){
                            console.log({serverMsg: "Donor "+req.body.donorPhone+" is now set as eligible"})
                        }
                        else{
                            console.log({servermsg: "Donor eligibility change failed"})
                        }
                    })
                }

                else{

                    console.log({serverMsg: "Failed to cancel request"})
                    res.status(200).json({serverMsg: "Accepted"})
                
                }

        })
    }

    else if(req.body.operation == "claim"){
        
        var sql = "UPDATE requests SET status = 'Claimed' WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientDate = ? AND patientPhone = ? AND patientNeed = ? AND requestedBy = ?"
    
        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGroup, req.body.patientDate, req.body.patientPhone, 
            req.body.patientNeed, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.affectedRows > 0){

                    console.log({serverMsg: "Claimed"})
                    res.status(200).json({serverMsg: "Claimed"})

                    var date = new Date()

                    if(req.body.patientNeed == "Blood" || req.body.patientNeed == "Blood and Plasma"){

                        date.setDate(date.getDate() + 90)
                    }
                    else if(req.body.patientNeed == "Plasma"){

                        date.setDate(date.getDate() + 7)
                    }
            

                    var dateString = ("0" + date.getDate()).slice(-2)+"-"+("0" + (date.getMonth() + 1)).slice(-2)+"-"+date.getFullYear()
    

                    var sql2 = "UPDATE users SET eligibility = 'not_available' , eligible_date = ? WHERE phone = ?"
                    db.query(sql2, [dateString, req.body.donorPhone], (err, results) => {

                        if(err) throw err
                        if(result.affectedRows > 0){
                            console.log({serverMsg: "Donor "+req.body.donorPhone+" is now set as not_available"})
                        }
                        else{
                            console.log({servermsg: "Donor eligibility change failed"})
                        }
                    })
                }

                else{

                    console.log({serverMsg: "Failed to claim request"})
                    res.status(200).json({serverMsg: "Accepted"})
                }

        })
    }

    else if(req.body.operation == "not_donate"){
        
        var sql = "UPDATE requests SET status = 'Not_Donated' WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientDate = ? AND patientPhone = ? AND patientNeed = ? AND requestedBy = ?"
    
        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGroup, req.body.patientDate, req.body.patientPhone, 
            req.body.patientNeed, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.affectedRows > 0){

                    console.log({serverMsg: "Not_Donated"})
                    res.status(200).json({serverMsg: "Not_Donated"})

                    var sql2 = "UPDATE users SET eligibility = 'eligible' WHERE phone = ?"
                    db.query(sql2, [req.body.donorPhone], (err, results) => {

                        if(err) throw err
                        if(result.affectedRows > 0){
                            console.log({serverMsg: "Donor "+req.body.donorPhone+" is now set as eligible"})
                        }
                        else{
                            console.log({servermsg: "Donor eligibility change failed"})
                        }
                    })
                }

                else{

                    console.log({serverMsg: "Failed to not-donate request"})
                    res.status(200).json({serverMsg: "Accepted"})
                }

        })
    }


    else if(req.body.operation == "confirm"){
        
        var sql = "UPDATE requests SET status = 'Donated' WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientDate = ? AND patientPhone = ? AND patientNeed = ? AND requestedBy = ?"
    
        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGroup, req.body.patientDate, req.body.patientPhone, 
            req.body.patientNeed, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.affectedRows > 0){

                    console.log({serverMsg: "Donated"})
                    res.status(200).json({serverMsg: "Donated"})

                }

                else{

                    console.log({serverMsg: "Failed to confirm request"})
                    res.status(200).json({serverMsg: "Claimed"})
                }

        })
    }

    else if(req.body.operation == "not_confirm"){

        var sql = "UPDATE requests SET status = 'Not_Donated' WHERE donorPhone = ? AND patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientDate = ? AND patientPhone = ? AND patientNeed = ? AND requestedBy = ?"
    
        db.query(sql, [req.body.donorPhone, req.body.patientName, req.body.patientAge, 
            req.body.patientBloodGroup, req.body.patientDate, req.body.patientPhone, 
            req.body.patientNeed, req.body.requestedBy], (err, result) => {
    
                if(err) throw err
                if(result.affectedRows > 0){

                    console.log({serverMsg: "Not_Donated"})
                    res.status(200).json({serverMsg: "Not_Donated"})

                    var sql2 = "UPDATE users SET eligibility = 'eligible' WHERE phone = ?"
                    db.query(sql2, [req.body.donorPhone], (err, results) => {

                        if(err) throw err
                        if(result.affectedRows > 0){
                            console.log({serverMsg: "Donor "+req.body.donorPhone+" is now set as eligible"})
                        }
                        else{
                            console.log({servermsg: "Donor eligibility change failed"})
                        }
                    })
                }

                else{

                    console.log({serverMsg: "Failed to not-confirm request"})
                    res.status(200).json({serverMsg: "Claimed"})
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

            var length = result.length

            
            async.forEachOf(result, (result, i, callback) => {


                var sql2 = "SELECT * FROM patients WHERE name = '" + result.patientName + "' AND age = '" + result.patientAge + "' AND blood_group = '" + result.patientBloodGrp + "' AND date = '"+ result.patientDate +"' AND phone = '" + result.patientPhone + "' AND need = '"+ result.patientNeed+"'"
                var serverMsg = result.status

                db.query(sql2, [result.patientPhone], (err, result2) => {
            
                    if(err) throw err
        
                    if(result2.length > 0){
        
                        result2[0].serverMsg = serverMsg
                        responseValues.push(result2[0])
                        
                        if(i == length-1){

                            console.log(responseValues)
                            res.status(200).json(responseValues)
                        }
                    }
                    else{
                        console.log({serverMsg: "No Record"})
                        res.status(200).json([{serverMsg: "No Record"}])

                    }
                })

            }, 
            
            function(err){
                if(err){
                    throw err;
                }
            })

            
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

            var length = result.length

            async.forEachOf(result, (result, i, callback) => {

                var sql2 = "SELECT * FROM users WHERE phone = ?"
                var serverMsg = result.status
    
                db.query(sql2, [result.donorPhone], (err, result2) => {
            
                    if(err) throw err
        
                    if(result2.length > 0){
        
                        result2[0].serverMsg = serverMsg
                        responseValues.push(result2[0])
                        
                        if(i == length-1){

                            console.log(responseValues)
                            res.status(200).json(responseValues)
                        }
                    }
                    else{
                        console.log({serverMsg: "No Record"})
                        res.status(200).json([])
                    }
                })

            }, function(err){

                if(err) throw err
            })

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
            var length = result.length

            
            async.forEachOf(result, (result, i, callback) => {


                var sql2 = "SELECT * FROM patients WHERE name = '" + result.patientName + "' AND age = '" + result.patientAge + "' AND blood_group = '" + result.patientBloodGrp + "' AND date = '"+ result.patientDate +"' AND phone = '" + result.patientPhone + "' AND need = '"+ result.patientNeed+"'"
                var serverMsg = result.status

                db.query(sql2, [result.patientPhone], (err, result2) => {
            
                    if(err) throw err
        
                    if(result2.length > 0){
        
                        result2[0].serverMsg = serverMsg
                        responseValues.push(result2[0])
                        
                        if(i == length-1){

                            console.log(responseValues)
                            res.status(200).json(responseValues)
                        }
                    }
                    else{
                        console.log({serverMsg: "No Record"})
                        res.status(200).json([{serverMsg: "No Record"}])

                    }
                })

            }, 
            
            function(err){
                if(err){
                    throw err;
                }
            })
        

            
        }
        else{

            console.log({serverMsg: "No Record"})
            res.status(200).json([{serverMsg: "No Record"}])

        }
    
    })

    
}