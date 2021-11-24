const db = require("../models/db.js");
const admin = require("../models/firebase.js")


module.exports.sendNotification = (req, res) => {
        
  var sql = "SELECT * FROM tokens WHERE phone = ?"
  db.query(sql,[req.body.phone], (err, result) => {

    if(err) throw err

    if(result.length > 0){

      for(var i=0;i<result.length;i++){

        const message = {
    
          notification: {
            title: req.body.title,
            body: req.body.body
            },
          data: {
            activity: req.body.activity,
            hidden: req.body.hidden
          },
    
          token: result[i].token
    
        }
  
        admin.messaging().send(message)
        .then((response) => {
      
          console.log('Successfully sent message:', response)
          res.status(200).json({serverMsg: 'Successfully sent message:', response})
      
        })
        .catch((error) => {
          console.log('Error sending message:', error)
          res.status(200).json({serverMsg: 'Error sending message:', error})
      
        })




        var sql = "SELECT * FROM notifications WHERE phone = ?"
        db.query(sql, [result[i].phone], (err, result) => {

          if(err) throw err

          if(result.length == 0){

              var sql2 = "INSERT INTO notifications (phone, activity) VALUES (?)"
              var values = [req.result[i].phone, req.body.activity]
              db.query(sql2, [values], (err, result2) => {
              
                if(err) throw err
                
                if(result2.affectedRows > 0){

                  console.log('Notification inserted')

                }
              })
          }
          else{

              var sql2 = "UPDATE notifications SET activity = ? WHERE phone = ?"
              db.query(sql2, [req.result[i].phone, req.body.activity], (err, result2) => {
              
                if(err) throw err

                if(result2.affectedRows > 0){
                  
                  console.log('Notification inserted')

                }
              })
          }

      })

    }
  }
    else{
      console.log('No token found')
      res.status(200).json({serverMsg: 'No token found'})
    }
  })     
}


module.exports.checkNotification = (req, res) => {

  var sql = "SELECT * FROM notifications WHERE phone = ?"
  db.query(sql, [req.body.phone], (err, result) => {

    if(err) throw err

    if(result.length > 0){

      console.log("Notification Found: "+ result[0].activity)
      res.status(200).json({serverMsg: result[0].activity})

      var sql = "DELETE FROM notifications WHERE phone = ?"
      db.query(sql, [req.body.phone], (err, result) => {

        if(err) throw err

        if(result.affectedRows > 0){

          console.log("Notification Deleted!")

        }
    
      })
    }

    else{
      console.log("No Notification Found!")
      res.status(200).json({serverMsg: "No Notifications"})
    }

  })
}

module.exports.deleteNotification = (req, res) => {
  
  var sql = "DELETE FROM notifications WHERE phone = ?"
  db.query(sql, [req.body.phone], (err, result) => {

    if(err) throw err

    if(result.affectedRows > 0){

      console.log("Notification Deleted!")
      res.status(200).json({serverMsg: "Success"})

    }
    else{

      console.log("Not Notification Found!")
      res.status(200).json({serverMsg: "No Notifications"})
    }
  })

}