const express = require("express")
const app = express()
const db = require("../models/db.js")
const requestController = require("../controllers/request.controller")

app.use("/lookForRequests.php", requestController.receiveRequest)
app.use("/sendRequest.php", requestController.sendRequest)

module.exports = app