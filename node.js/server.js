const mysql = require("mysql");
const express = require("express");
const app = express();


const db = require("./app/models/db.js")

db.connect((err) => {
    if(err) throw err;
    console.log("Connected to MySQL database 'cov19'!");

})

app.use(express.json({limit: '50mb'}));
app.use(express.urlencoded({extended: true, limit: '50mb'}));

const appRoutes = require("./app/routes/app.routes")
const dashboardRoutes = require("./app/routes/dashboard.routes")
const donorRoutes = require("./app/routes/donor.routes")
const initdbRoutes = require("./app/routes/initdb.routes")
const userRoutes = require("./app/routes/user.routes")
const patientRoutes = require("./app/routes/patient.routes")
const requestRoutes = require("./app/routes/request.routes")
const responseRoutes = require("./app/routes/response.routes")


app.use("/",appRoutes)
app.use("/",dashboardRoutes)
app.use("/",donorRoutes)
app.use("/",initdbRoutes)
app.use("/",userRoutes)
app.use("/",patientRoutes)
app.use("/",requestRoutes)
app.use("/",responseRoutes)

app.listen(3000, () => {
    console.log("Connected to Port 3000!")

});

app.get("/", (req,res) => {
    res.json({message: "Welcome! You are now connected to Plasma+ API."});
});