const express = require("express")
const app = express()
const dashboardController = require("../controllers/dashboard.controller")


app.post("/imageDownload.php", dashboardController.imageDownload)

app.post("/dashboardNumbers.php", dashboardController.dashboardNumbers)

app.post("/dashboardNumbers2.php", dashboardController.dashboardNumbers2)

app.post("/donorEligibility.php", dashboardController.donorEligibility)

app.post("/checkNotification.php", dashboardController.checkNotification)


module.exports = app