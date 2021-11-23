const express = require("express")
const app = express()
const loginController = require("../controllers/login.controller")

app.post("/login.php", loginController.login)

app.post("/registration.php", loginController.registration)

app.post("/checkUser.php", loginController.checkUser)

app.post("/tokenRegister.php", loginController.tokenRegister)

module.exports = app