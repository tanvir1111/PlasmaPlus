const admin = require("firebase-admin");
const firebaseApp = require("firebase/app");
const firebaseMessaging = require("firebase/messaging");

var serviceAccount = require("../../ece-covid-app-firebase-adminsdk-ganop-19f4bbf36e.json");

var firebaseConfig = {
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://ece-covid-app.firebaseio.com"
  };

  var firebaseConfig2 = {
    apiKey: "AIzaSyBDgJBbyYlSqKL6aJTUFoRy4rsC2GajQZs",
    authDomain: "ece-covid-app.firebaseapp.com",
    databaseURL: "https://ece-covid-app.firebaseio.com",
    projectId: "ece-covid-app",
    storageBucket: "ece-covid-app.appspot.com",
    messagingSenderId: "747629944283",
    appId: "1:747629944283:android:bc2986db7206307c2edbfe"
  };

admin.initializeApp(firebaseConfig)

module.exports = admin