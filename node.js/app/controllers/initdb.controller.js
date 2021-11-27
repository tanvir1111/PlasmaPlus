const db = require("../models/db.js")


module.exports.createTables = (req, res) => {

    var sql = "CREATE TABLE IF NOT EXISTS users (id INT(12) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), phone VARCHAR(255), gender VARCHAR(255), blood_group VARCHAR(255), division VARCHAR(255), district VARCHAR(255), thana VARCHAR(255), age VARCHAR(255), donor VARCHAR(255), eligibility VARCHAR(255) NOT NULL DEFAULT 'eligible', eligible_date VARCHAR(255) password VARCHAR(255), image LONGTEXT"
    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'users' created successfully!");
        res.status(200).send("TABLE 'users' created successfully!")

        var sql = "CREATE TABLE IF NOT EXISTS patients (id INT(12) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age VARCHAR(255), gender VARCHAR(255), blood_group VARCHAR(255), hospital VARCHAR(255), division VARCHAR(255), district VARCHAR(255), date VARCHAR(255), need VARCHAR(255), phone VARCHAR(255), amountOfBloodNeeded VARCHAR(255))"
    
        db.query(sql, (err, result) => {
            
            if(err) throw err;
            console.log("TABLE 'patients' created successfully!");
            res.status(200).send("TABLE 'patients' created successfully!")

            var sql = "CREATE TABLE IF NOT EXISTS requests (id  INT(12) NOT NULL AUTO_INCREMENT, datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, donorPhone VARCHAR(255), patientName VARCHAR(255), patientAge VARCHAR(255), patientBloodGrp VARCHAR(255), patientDate VARCHAR(255), patientPhone VARCHAR(255), patientNeed VARCHAR(255), requestedBy VARCHAR(255), status VARCHAR(255), PRIMARY KEY (id))"

            db.query(sql, (err, result) => {
        
                if(err) throw err
                console.log("TABLE 'requests' created successfully!")
                res.status(200).send("TABLE 'requests' created successfully!")

                var sql = "CREATE TABLE IF NOT EXISTS tokens (phone VARCHAR(255), token VARCHAR(255))"

                db.query(sql, (err, result) => {
                    
                    if(err) throw err;
                    console.log("TABLE 'tokens' created successfully!");
                    res.status(200).send("TABLE 'tokens' created successfully!")
                    
                    var sql = "CREATE TABLE IF NOT EXISTS notifications (phone VARCHAR(255), activity VARCHAR(255), PRIMARY KEY (phone))"

                    db.query(sql, (err, result) => {
                
                        if(err) throw err
                        console.log("TABLE 'notifications' created successfully!")
                        res.status(200).send("TABLE 'notifications' created successfully!")
                
                    })                    
                });
            })
            
        });

    });
}

module.exports.createUserTable = (req,res) => {

    var sql = "CREATE TABLE IF NOT EXISTS users (id INT(12) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), phone VARCHAR(255), gender VARCHAR(255), blood_group VARCHAR(255), division VARCHAR(255), district VARCHAR(255), thana VARCHAR(255), age VARCHAR(255), donor VARCHAR(255), eligibility VARCHAR(255) NOT NULL DEFAULT, eligible_date VARCHAR(255) password VARCHAR(255), image LONGTEXT"
    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'users' created successfully!");
        res.status(200).send("TABLE 'users' created successfully!")
        
    });


}

module.exports.createPatientTable = (req,res) => {

    var sql = "CREATE TABLE IF NOT EXISTS patients (id INT(12) AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age VARCHAR(255), gender VARCHAR(255), blood_group VARCHAR(255), hospital VARCHAR(255), division VARCHAR(255), district VARCHAR(255), date VARCHAR(255), need VARCHAR(255), phone VARCHAR(255), amountOfBloodNeeded VARCHAR(255))"
    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'patients' created successfully!");
        res.status(200).send("TABLE 'patients' created successfully!")
        
    });


}

module.exports.createRequestTable = (req, res) => {

    var sql = "CREATE TABLE IF NOT EXISTS requests (id INT (12) NOT NULL AUTO_INCREMENT, datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, donorPhone VARCHAR(255), patientName VARCHAR(255), patientAge VARCHAR(255), patientBloodGrp VARCHAR(255), patientDate VARCHAR(255), patientPhone VARCHAR(255), patientNeed VARCHAR(255), requestedBy VARCHAR(255), status VARCHAR(255), PRIMARY KEY (id))"

    db.query(sql, (err, result) => {

        if(err) throw err
        console.log("TABLE 'requests' created successfully!")
        res.status(200).send("TABLE 'requests' created successfully!")
    })
}

module.exports.createTokenTable = (req, res) => {
    var sql = "CREATE TABLE IF NOT EXISTS tokens (phone VARCHAR(255), token VARCHAR(255))"

    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log("TABLE 'tokens' created successfully!");
        res.status(200).send("TABLE 'tokens' created successfully!")
        
    });
}

module.exports.createNotificationTable = (req, res) => {

    var sql = "CREATE TABLE IF NOT EXISTS notifications (phone VARCHAR(255), activity VARCHAR(255), PRIMARY KEY (phone))"

    db.query(sql, (err, result) => {

        if(err) throw err
        console.log("TABLE 'notifications' created successfully!")
        res.status(200).send("TABLE 'notifications' created successfully!")

    })
}

module.exports.createHistoryTable = (req, res) => {

    var sql = "CREATE TABLE IF NOT EXISTS history (id INT(12) NOT NULL AUTO_INCREMENT, datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, donorPhone VARCHAR(255), patientName VARCHAR(255), patientAge VARCHAR(255), patientBloodGrp VARCHAR(255), patientDate VARCHAR(255), patientPhone VARCHAR(255), patientNeed VARCHAR(255), requestedBy VARCHAR(255), status VARCHAR(255), PRIMARY KEY (id))"

    db.query(sql, (err, result) => {

        if(err) throw err
        console.log("TABLE 'history' created successfully!")
        res.status(200).send("TABLE 'history' created successfully!")

    })
}
   

module.exports.dropTables = (req,res) => {

    var sql = "DROP TABLE users";    
    db.query(sql, (err, result) => {
        
        if(err) throw err;
        console.log(res);
        res.status(200).send("TABLE 'users' removed successfully!")

        var sql = "DROP TABLE patients";    
        db.query(sql, (err, result) => {
            
            if(err) throw err;
            console.log(res);
            res.status(200).send("TABLE 'patients' removed successfully!")

            
            var sql = "DROP TABLE requests";    
            db.query(sql, (err, result) => {
                
                if(err) throw err;
                console.log(res);
                res.status(200).send("TABLE 'requests' removed successfully!")
    
                
                var sql = "DROP TABLE tokens";    
                db.query(sql, (err, result) => {
                    
                    if(err) throw err;
                    console.log(res);
                    res.status(200).send("TABLE 'tokens' removed successfully!")
        
                    
                    var sql = "DROP TABLE notifications";    
                    db.query(sql, (err, result) => {
                        
                        if(err) throw err;
                        console.log(res);
                        res.status(200).send("TABLE 'notifications' removed successfully!")
            
                        
                        var sql = "DROP TABLE history";    
                        db.query(sql, (err, result) => {
                            
                            if(err) throw err;
                            console.log(res);
                            res.status(200).send("TABLE 'history' removed successfully!")
                
                            
                            
                        });       
                    });                       
                });                   
            });            
        });
        
    });

}