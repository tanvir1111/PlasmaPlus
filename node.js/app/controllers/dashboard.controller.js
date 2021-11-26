const db = require("../models/db.js")


module.exports.dashboardNumbers = (req, res) =>{


    var sql = "SELECT COUNT(*) AS donorCount FROM users WHERE donor != 'na'; SELECT COUNT(*) AS patientCount FROM patients; SELECT COUNT(*) AS myPatientCount FROM patients WHERE phone = "+req.body.phone+"; SELECT COUNT(*) AS requestsFromDonorsCount FROM requests WHERE patientPhone = "+req.body.phone+" AND requestedBy = 'donor';SELECT COUNT(*) AS requestsFromPatientsCount FROM requests WHERE donorPhone = "+req.body.phone+" AND requestedBy = 'patient'; SELECT COUNT(*) AS responsesFromDonorsCount FROM requests WHERE patientPhone = "+req.body.phone+" AND requestedBy = 'patient'; SELECT COUNT(*) AS responsesFromPatientsCount FROM requests WHERE donorPhone = "+req.body.phone+" AND requestedBy = 'donor';"

    db.query(sql, (err, data) => {

        if(err) throw err;
        
        console.log("Dashboard Numbers for Phone found: "+req.body.phone)

        console.log({numberOfDonors: data[0][0].donorCount, 
            numberOfPatients: data[1][0].patientCount, numberOfMyPatients: data[2][0].myPatientCount,
            numberOfRequestsFromDonors: data[3][0].requestsFromDonorsCount, numberOfRequestsFromPatients: data[4][0].requestsFromPatientsCount, 
            numberOfResponsesFromDonors: data[5][0].responsesFromDonorsCount, numberOfResponsesFromPatients: data[6][0].responsesFromPatientsCount})

        res.status(200).json({numberOfDonors: data[0][0].donorCount, 
            numberOfPatients: data[1][0].patientCount, numberOfMyPatients: data[2][0].myPatientCount,
            numberOfRequestsFromDonors: data[3][0].requestsFromDonorsCount, numberOfRequestsFromPatients: data[4][0].requestsFromPatientsCount, 
            numberOfResponsesFromDonors: data[5][0].responsesFromDonorsCount, numberOfResponsesFromPatients: data[6][0].responsesFromPatientsCount})
        
    })

}


module.exports.dashboardNumbers2 = (req, res) => {

    var donorCount = 0, patientCount = 0, myPatientCount = 0;

    if(req.body.phone == null){

        console.log("Phone null")

    }
    else{

        var sql = "SELECT * FROM users WHERE donor != 'na'"
        db.query(sql, (err, data) => {

            if(err) throw err;
            
            donorCount = data.length

            var sql2 = "SELECT * FROM patients"
            db.query(sql2, (err, data) => {
    
                if(err) throw err;
                
                patientCount = data.length

                var sql3 = "SELECT * FROM patients WHERE phone = ?"
                db.query(sql3,[req.body.phone], (err, data) => {
        
                    if(err) throw err;
                    
                    myPatientCount = data.length

                    console.log("Dashboard Numbers for Phone found: "+req.body.phone)

                    console.log({numberOfDonors: donorCount, 
                        numberOfPatients: patientCount, numberOfMyPatients: myPatientCount,
                        numberOfRequestsFromDonors: 0, numberOfRequestsFromPatients: 0, 
                        numberOfResponsesFromDonors: 0, numberOfResponsesFromPatients: 0})
            
                    res.status(200).json({numberOfDonors: donorCount, 
                        numberOfPatients: patientCount, numberOfMyPatients: myPatientCount,
                        numberOfRequestsFromDonors: 0, numberOfRequestsFromPatients: 0, 
                        numberOfResponsesFromDonors: 0, numberOfResponsesFromPatients: 0})
                    
                })
                
            })
            
        })

    }

}
module.exports.donorEligibility = (req, res) => {

    if(req.body.phone == null){

        console.log("Phone null")
    }
    else{

        var sql = "SELECT * FROM users WHERE phone = ?"
        db.query(sql, [req.body.phone], (err, result) => {

            if(result.length > 0){

                if(result[0].eligibility == "eligible"){

                    console.log("Eligibility for Phone found: "+req.body.phone)
                    res.status(200).json({eligibility: "eligible", serverMsg: true})
                }
                else if(result[0].eligibility == "not_eligible"){

                    console.log("Eligibility for Phone found: "+req.body.phone)
                    res.status(200).json({eligibility: "not_eligible", serverMsg: true})
                }
            }
        })

    

    }
}