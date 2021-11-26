const db = require("../models/db.js")
const async = require("async")

module.exports.responsesFromDonorsAlpha = (req, res) => {


    var sql = "SELECT * FROM requests"

    if(req.body.status == "any"){
        sql = "SELECT * FROM requests WHERE patientPhone = '"+req.body.phone+"' AND requestedBy = 'patient'"

    }
    else if(req.body.status == "Pending"){
        sql = "SELECT * FROM requests WHERE patientPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Pending'"

    }
    else if(req.body.status == "Successful"){
        sql = "SELECT * FROM requests WHERE patientPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Accepted' OR status = 'Claimed' OR status = 'Confirmed'"

    }
    else if(req.body.status == "Failed"){
        sql = "SELECT * FROM requests WHERE patientPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Declined' OR status = 'Canceled' OR status = 'Not_Confirmed'"

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

module.exports.responsesFromDonorsBeta = (req, res) => {

    var sql = "SELECT * FROM requests"

    if(req.body.status == "any"){
        sql = "SELECT * FROM requests WHERE patientName = '" + req.body.name + "' AND patientAge = '" + req.body.age + "' AND patientBloodGrp = '" + req.body.bloodGroup + "' AND patientPhone = '"+req.body.phone+"' AND requestedBy = 'patient'"

    }
    else if(req.body.status == "Pending"){
        sql = "SELECT * FROM requests WHERE patientName = '" + req.body.name + "' AND patientAge = '" + req.body.age + "' AND patientBloodGrp = '" + req.body.bloodGroup + "' AND patientPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Pending'"

    }
    else if(req.body.status == "Successful"){
        sql = "SELECT * FROM requests WHERE patientName = '" + req.body.name + "' AND patientAge = '" + req.body.age + "' AND patientBloodGrp = '" + req.body.bloodGroup + "' AND patientPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Accepted' OR status = 'Claimed' OR status = 'Confirmed'"

    }
    else if(req.body.status == "Failed"){
        sql = "SELECT * FROM requests WHERE patientName = '" + req.body.name + "' AND patientAge = '" + req.body.age + "' AND patientBloodGrp = '" + req.body.bloodGroup + "' AND patientPhone = '"+req.body.phone+"' AND requestedBy = 'patient' AND status = 'Declined' OR status = 'Canceled' OR status = 'Not_Confirmed'"

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


module.exports.responsesFromPatients = (req, res) => {

    var sql = "SELECT * FROM requests"

    if(req.body.status == "any"){
        sql = "SELECT * FROM requests WHERE donorPhone = '"+req.body.phone+"' AND requestedBy = 'donor'"

    }
    else if(req.body.status == "Pending"){
        sql = "SELECT * FROM requests WHERE donorPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Pending'"

    }
    else if(req.body.status == "Successful"){
        sql = "SELECT * FROM requests WHERE donorPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Accepted' OR status = 'Claimed' OR status = 'Confirmed'"

    }
    else if(req.body.status == "Failed"){
        sql = "SELECT * FROM requests WHERE donorPhone = '"+req.body.phone+"' AND requestedBy = 'donor' AND status = 'Declined' OR status = 'Canceled' OR status = 'Not_Confirmed'"

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

            }, function(err){

                if(err) throw err
            })

            
        }
        else{

            console.log({serverMsg: "No Record"})
            res.status(200).json([{serverMsg: "No Record"}])

        }
    
    })

    
}