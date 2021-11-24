const db = require("../models/db.js")


module.exports.patientData = (req, res) => {

    var sql = "SELECT * FROM patients WHERE name = ? && age = ? && gender = ? && blood_group = ? && phone = ?"
    db.query(sql,[req.body.name, req.body.age, req.body.gender, req.body.blood_group, req.body.phone], (err, result) => {

        if(err) throw err;
        if(result.length > 0){

            console.log("Patient Exists")
            res.status(200).json({serverMsg: "exists"})
        }
        else{
            var sql = "INSERT INTO patients (name, age, gender, blood_group, hospital, division, district, date, need, phone, amountOfBloodNeeded) VALUES (?)"
            var values = [req.body.name, req.body.age, req.body.gender, req.body.blood_group, req.body.hospital,
                req.body.division, req.body.district, req.body.date, req.body.need, req.body.phone, req.body.amountOfBloodNeeded]
            db.query(sql, [values], 
                (err, result) => {
        
                if(err) throw err;
        
                if(result.affectedRows > 0){
        
                    console.log("Patient Added")
                    res.status(200).json({serverMsg: "success"})
                }
            })
        }
    })
   
}


module.exports.ownPatients = (req, res) => {

    var sql = "SELECT * FROM patients WHERE phone = ?"
    db.query(sql,[req.body.phone], (err, data) => {

        if(err) throw err;

        console.log({"My Patients": data})
        res.status(200).json(data)
      
    })
}

module.exports.searchPatients = (req, res) => {

    if(req.body.blood_group == "Any"){

        if(req.body.district=="any")
            var sql = "SELECT * FROM patients WHERE phone != '"+req.body.phone+"'"
        else
            var sql = "SELECT * FROM patients WHERE phone != '"+req.body.phone+"' AND district LIKE '%"+req.body.district+"%'"

    }

    else {

        if(req.body.district=="any")
            var sql = "SELECT * FROM patients WHERE phone != '"+req.body.phone+"' AND blood_group = '"+req.body.blood_group+"'"
        else
            var sql = "SELECT * FROM patients WHERE phone != '"+req.body.phone+"' AND blood_group = '"+req.body.blood_group+"' AND district LIKE '%"+req.body.district+"%'"
    }
   
    db.query(sql, (err, data) => {

        if(err) throw err;

        console.log({"Search Patients": data, "blood_group":req.body.blood_group, "district":req.body.district})
        res.status(200).json(data)
      
    })

}

module.exports.updatePatient = (req, res) => {

    var sql = "DELETE FROM requests WHERE patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientPhone = ?"
    db.query(sql, [req.body.name, req.body.age, req.body.blood_group, req.body.phone], (err, result) => {
    
    
        var sql2 = "UPDATE patients SET name = ?, age = ?, gender = ?, hospital = ?, division = ?, district = ?, date = ?, amountOfBloodNeeded = ? WHERE name = ? AND age = ? AND blood_group = ? AND phone = ?"

        db.query(sql2, [req.body.newname, req.body.newage, req.body.newgender, req.body.newhospital,
        req.body.newdivision, req.body.newdistrict, req.body.newdate, req.body.newAmountOfBloodNeeded,
        req.body.name, req.body.age, req.body.blood_group, req.body.phone], (err, result2) => {
    
            if(err) throw err
            
            if(result2.affectedRows > 0){
    
                console.log("Update Patient Success: "+req.body.phone)
                res.status(200).json({serverMsg: "Success"})
            }
    
            else{
                console.log("Update Patient Failure: "+req.body.phone)
                res.status(200).json({serverMsg: "Failure"})
            }
        })
    })

   
}

module.exports.deletePatient = (req, res) => {


    var sql = "DELETE FROM requests WHERE patientName = ? AND patientAge = ? AND patientBloodGrp = ? AND patientPhone = ?"
    db.query(sql, [req.body.name, req.body.age, req.body.blood_group, req.body.phone], (err, result) => {


        var sql2 = "DELETE FROM patients WHERE name = ? AND age = ? AND blood_group = ? AND phone = ?"
            db.query(sql2, [req.body.name, req.body.age, req.body.blood_group, req.body.phone], (err, result2) => {
        
                if(result2.affectedRows > 0){
        
                    console.log("Delete Patient Success: "+req.body.phone)
                    res.status(200).json({serverMsg: "Success"})
                }
                else{
                    console.log("Delete Patient Failed: "+req.body.phone)
                    res.status(200).json({serverMsg: "Failed"})
                }
            })

    })
   
}