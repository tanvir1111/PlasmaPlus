const mysql = require("mysql")


var db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "",
  database: "cov19",
  multipleStatements: true

})

module.exports = db