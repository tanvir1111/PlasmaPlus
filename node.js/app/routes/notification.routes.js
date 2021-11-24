const express = require("express")
const app = express()
const notificationController = require("../controllers/notification.controller")

app.post("/pushNotification.php", notificationController.sendNotification)

app.post("/checkNotification.php", notificationController.checkNotification)

app.post("/deleteNotification.php", notificationController.deleteNotification)

module.exports = app