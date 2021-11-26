const mysql = require("mysql")
const express = require("express")
const app = express()
var https = require('https')
var http = require('http')
var fs = require('fs')
var path = require('path')
var cron = require("node-cron")
var async = require("async")
var admin = require("./app/models/firebase.js")

var options = {
  key: fs.readFileSync(path.resolve('sslcert/selfsigned.key')),
  cert: fs.readFileSync(path.resolve('sslcert/selfsigned.crt'))
}

var credentials = {key: options.key, cert: options.cert};

var httpServer = http.createServer(app);
var httpsServer = https.createServer(credentials, app);

httpServer.listen(3000, function(){
    console.log("HTTP server running at PORT 3000")
})
httpsServer.listen(8443, function(){
    console.log("HTTPS server running at PORT 8443")
})


const db = require("./app/models/db.js")

db.connect((err) => {
    if(err) throw err;
    console.log("Connected to MySQL database 'cov19'!");

})


app.use(express.json({limit: '50mb'}));
app.use(express.urlencoded({extended: true, limit: '50mb'}));

const appRoutes = require("./app/routes/app.routes")
const dashboardRoutes = require("./app/routes/dashboard.routes")
const donorRoutes = require("./app/routes/donor.routes")
const initdbRoutes = require("./app/routes/initdb.routes")
const userRoutes = require("./app/routes/user.routes")
const patientRoutes = require("./app/routes/patient.routes")
const requestRoutes = require("./app/routes/request.routes")
const responseRoutes = require("./app/routes/response.routes")
const notificationRoutes = require("./app/routes/notification.routes")


app.use("/",appRoutes)
app.use("/",dashboardRoutes)
app.use("/",donorRoutes)
app.use("/",initdbRoutes)
app.use("/",userRoutes)
app.use("/",patientRoutes)
app.use("/",requestRoutes)
app.use("/",responseRoutes)
app.use("/",notificationRoutes)

app.get("/", (req,res) => {

    var date = new Date()
    var dateString = ("0" + date.getDate()).slice(-2)+"-"+("0" + (date.getMonth() + 1)).slice(-2)+"-"+date.getFullYear()
    res.status(200).json({date: dateString, message: "Welcome! You are now connected to Plasma+ API."});
});



setInterval(headerPrint, 10000);
cron.schedule('00 00 * * *', patientDeadlineNotification)
cron.schedule('15 00 * * *', patientAutoDeletion)


function headerPrint(){

    var date = new Date()
    console.log("Auto-script: "+date)
}

function patientDeadlineNotification(){

    

    var date = new Date()
    date.setDate(date.getDate() + 0)
    var dateString = ("0" + date.getDate()).slice(-2)+"-"+("0" + (date.getMonth() + 1)).slice(-2)+"-"+date.getFullYear()
    
    console.log("Patient Deadline Notification: "+dateString)


    var sql = "SELECT * FROM patients WHERE date = ?"
    db.query(sql, dateString, (err, data) => {

        if(err) throw err
        if(data.length > 0){

            console.log("Patients found: "+data.length)

            async.forEachOf(data, (row, i, callback) => {
                
                sendNotification(row.phone, "Patient '"+row.name+"' will expire in 48 hours.","Patient donation deadline is at 12:15 AM on "+row.date+". You can change the deadline from My Patients.","MyPatientsActivity","")

            },
            function(err){
                if(err) throw err
            })

        }
        else{
            console.log("Patients found: "+data.length)

        }
    })
            
}

function patientAutoDeletion(){


    var date = new Date()
    date.setDate(date.getDate() + 2)
    var dateString = ("0" + date.getDate()).slice(-2)+"-"+("0" + (date.getMonth() + 1)).slice(-2)+"-"+date.getFullYear()

    console.log("Patient Auto-Deletion: "+dateString)

    var sql = "SELECT * FROM patients WHERE date = ?"
    db.query(sql, dateString, (err, data) => {

        if(err) throw err
        if(data.length > 0){

            console.log("Patients found: "+data.length)

            var length = data.length

            async.forEachOf(data, (row, i, callback) => {
                
                sendNotification(row.phone, "Patient '"+row.name+"' has expired.","Patient has been deleted from My Patients.","MyPatientsActivity","")
                
                if(i == length - 1){

                    var sql2 = "DELETE FROM patients WHERE date = ?"
                    db.query(sql2, dateString, (err, result) => {

                        if(err) throw err

                        if(result.affectedRows > 0){
                            
                            console.log("Patients deleted: "+result.affectedRows)
                        }
                    })
                }
            },
            function(err){
                if(err) throw err
            })

        }
        else{
            console.log("Patients found: "+data.length)

        }
    })
}



function sendNotification(phone, title, body, activity, hidden){
        
    var sql = "SELECT * FROM tokens WHERE phone = ?"
    db.query(sql,[phone], (err, result) => {
  
      if(err) throw err
  
      if(result.length > 0){
    
        async.forEachOf(result, (result, i, callback) => {
  
          const message = {
      
            notification: {
              title: title,
              body: body
              },
            data: {
              activity: activity,
              hidden: hidden
            },
      
            token: result.token
      
          }
    
          admin.messaging().send(message)
          .then((response) => {
        
            console.log('Successfully sent message:', response)
        
          })
          .catch((error) => {
            console.log('Error sending message:', error)
        
          })
  
  
          var sql2 = "SELECT * FROM notifications WHERE phone = ?"
          db.query(sql2, [phone], (err, result2) => {
  
            if(err) throw err
  
            if(result2.length == 0){
  
                var sql3 = "INSERT INTO notifications (phone, activity) VALUES (?)"
                var values = [phone, activity]
                db.query(sql3, [values], (err, result3) => {
                
                  if(err) throw err
                  
                  if(result3.affectedRows > 0){
  
                    console.log('Notification inserted')
  
                  }
                })
            }
            else{
  
                var sql3 = "UPDATE notifications SET activity = ? WHERE phone = ?"
                db.query(sql3, [phone, activity], (err, result3) => {
                
                  if(err) throw err
  
                  if(result3.affectedRows > 0){
                    
                    console.log('Notification inserted')
  
                  }
                })
            }
            
          
        }, 
        function(err){
          if(err) throw err
        })
  
        
  
  
  
      })
    }
      else{
        console.log('No token found')
      }
    })     
  }