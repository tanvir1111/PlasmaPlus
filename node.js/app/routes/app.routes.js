const express = require("express")
const app = express()
const appController = require("../controllers/app.controller")

app.get("/appVer.php", appController.version)

module.exports = app