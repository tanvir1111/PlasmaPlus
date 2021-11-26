const db = require("../models/db.js");
const admin = require("../models/firebase.js")
const async = require("async")

module.exports.sendNotification = (req, res) => {
        
  var sql = "SELECT * FROM tokens WHERE phone = ?"
  db.query(sql,[req.body.phone], (err, result) => {

    if(err) throw err

    if(result.length > 0){

      var length = result.length

      async.forEachOf(result, (result, i, callback) => {

        const message = {
    
          notification: {
            title: req.body.title,
            body: req.body.body
            },
          data: {
            activity: req.body.activity,
            hidden: req.body.hidden
          },
    
          token: result.token
    
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


        var sql2 = "SELECT * FROM notifications WHERE phone = ?"
        db.query(sql2, [req.body.phone], (err, result2) => {

          if(err) throw err

          if(result2.length == 0){

              var sql3 = "INSERT INTO notifications (phone, activity) VALUES (?)"
              var values = [req.body.phone, req.body.activity]
              db.query(sql3, [values], (err, result3) => {
              
                if(err) throw err
                
                if(result3.affectedRows > 0){

                  console.log('Notification inserted')

                }
              })
          }
          else{

              var sql3 = "UPDATE notifications SET activity = ? WHERE phone = ?"
              db.query(sql3, [req.body.phone, req.body.activity], (err, result3) => {
              
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
      console.log("Check Notification: No Notification Found!")
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

      console.log("Delete Notification: No Notification Found!")
      res.status(200).json({serverMsg: "No Notifications"})
    }
  })

}