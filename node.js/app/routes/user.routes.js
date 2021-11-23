const express = require("express")
const app = express()
const userController = require("../controllers/user.controller")

app.post("/login.php", userController.login)

app.post("/registration.php", userController.registration)

app.post("/checkUser.php", userController.checkUser)

app.post("/tokenRegister.php", userController.tokenRegister)

app.post("/tokenDelete.php", userController.tokenDelete)

app.post("/updateUserInfo.php", userController.updateUser)

app.post("/updateUserPassword.php", userController.updateUserPassword)

app.post("/deleteUserProfile.php", userController.deleteUser)

app.post("/imageDownload.php", userController.downloadImage)

app.post("/imageUpload.php", userController.uploadImage)

app.post("/imageDelete.php", userController.deleteImage)


module.exports = app