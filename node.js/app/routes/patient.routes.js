const express = require("express")
const app = express()
const patientController = require("../controllers/patient.controller")


app.post("/patientData.php", patientController.patientData)

app.post("/ownPatients.php", patientController.ownPatients)

app.post("/searchPatients.php", patientController.searchPatients)

app.post("/updatePatientProfile.php", patientController.updatePatient)

app.post("/deletePatientProfile.php", patientController.deletePatient)

module.exports = app