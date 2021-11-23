const db = require("../models/db.js")


module.exports.version = (req,res) => {

    res.status(200).json({serverMsg: "1.14"})
    
}

