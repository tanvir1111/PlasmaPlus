const db = require("../models/db.js")

module.exports.searchDonor = (req, res) => {

    if(req.body.blood_group == "Any"){

        if(req.body.district=="any")
            var sql = "SELECT * FROM users WHERE donor != 'na' AND phone != '"+req.body.phone+"'"
        else
            var sql = "SELECT * FROM users WHERE donor != 'na' AND phone != '"+req.body.phone+"' AND district LIKE '%"+req.body.district+"%'"

    }

    else {

        if(req.body.district=="any")
            var sql = "SELECT * FROM users WHERE donor != 'na' AND phone != '"+req.body.phone+"' AND blood_group = '"+req.body.blood_group+"'"
        else
            var sql = "SELECT * FROM users WHERE donor != 'na' AND phone != '"+req.body.phone+"' AND blood_group = '"+req.body.blood_group+"' AND district LIKE '%"+req.body.district+"%'"
    }
   
    db.query(sql, (err, data) => {

        if(err) throw err;

        console.log({"Search Donors": data, "blood_group":req.body.blood_group, "district":req.body.district})
        res.status(200).json(data)
      
    })
}