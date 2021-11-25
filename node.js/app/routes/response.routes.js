const express = require("express")
const app = express()
const db = require("../models/db.js")
const responseController = require("../controllers/response.controller")

app.use("/responsesFromDonorsAlpha.php", responseController.responsesFromDonorsAlpha)

app.use("/responsesFromDonorsBeta.php", responseController.responsesFromDonorsBeta)

app.use("/responsesFromPatients.php", responseController.responsesFromPatients)

module.exports = app