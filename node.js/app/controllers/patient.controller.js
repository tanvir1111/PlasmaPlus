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