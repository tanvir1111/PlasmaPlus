const express = require("express")
const app = express()
const db = require("../models/db.js")
const donorController = require("../controllers/donor.controller")

app.post("/searchDonor.php", donorController.searchDonor)

module.exports = app