const db = require("../models/db.js")


module.exports.dashboardNumbers = (req, res) =>{


    var sql = "SELECT COUNT(*) AS donorCount FROM users WHERE donor != 'na'; SELECT COUNT(*) AS patientCount FROM patients; SELECT COUNT(*) AS myPatientCount FROM patients WHERE phone = "+req.body.phone+";"

    db.query(sql, (err, data) => {

        if(err) throw err;
        
        console.log("Dashboard Numbers for Phone found: "+req.body.phone)

        console.log({numberOfDonors: data[0][0].donorCount, 
            numberOfPatients: data[1][0].patientCount, numberOfMyPatients: data[2][0].myPatientCount,
            numberOfRequestsFromDonors: 0, numberOfRequestsFromPatients: 0, 
            numberOfResponsesFromDonors: 0, numberOfResponsesFromPatients: 0})

        res.status(200).json({numberOfDonors: data[0][0].donorCount, 
            numberOfPatients: data[1][0].patientCount, numberOfMyPatients: data[2][0].myPatientCount,
            numberOfRequestsFromDonors: 0, numberOfRequestsFromPatients: 0, 
            numberOfResponsesFromDonors: 0, numberOfResponsesFromPatients: 0})
        
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

        console.log("Eligibility for Phone found: "+req.body.phone)
        res.status(200).json({eligibility: "eligible", serverMsg: true})

    }
}


module.exports.checkNotification = (req, res) => {

    if(req.body.phone == null){

        console.log("Phone null")
    }
    else{

        console.log("Notification for Phone found: "+req.body.phone)
        res.status(200).json({serverMsg: "No Notifications"})

    }
}