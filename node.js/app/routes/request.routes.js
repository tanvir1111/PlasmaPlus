const express = require("express")
const app = express()
const db = require("../models/db.js")
const requestController = require("../controllers/request.controller")

app.use("/lookForRequests.php", requestController.receiveRequest)

app.use("/sendRequest.php", requestController.sendRequest)

app.use("/requestsFromDonorsAlpha.php", requestController.requestsFromDonorsAlpha)

app.use("/requestsFromDonorsBeta.php", requestController.requestsFromDonorsBeta)

app.use("/requestsFromPatients.php", requestController.requestsFromPatients)

module.exports = app