const express = require("express")
const app = express()
const initdbController = require("../controllers/initdb.controller")

app.get("createTables", initdbController.createTables)

app.get("/createUserTable", initdbController.createUserTable)

app.get("/createPatientTable", initdbController.createPatientTable)

app.get("/createTokenTable", initdbController.createTokenTable)

app.get("/createRequestTable", initdbController.createRequestTable)

app.get("/createNotificationTable", initdbController.createNotificationTable)

app.get("/dropTables", initdbController.dropTables)

module.exports = app