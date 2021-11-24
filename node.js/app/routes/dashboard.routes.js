const express = require("express")
const app = express()
const dashboardController = require("../controllers/dashboard.controller")


app.post("/dashboardNumbers.php", dashboardController.dashboardNumbers)

app.post("/dashboardNumbers2.php", dashboardController.dashboardNumbers2)

app.post("/donorEligibility.php", dashboardController.donorEligibility)


module.exports = app