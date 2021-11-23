const express = require("express")
const app = express()
const db = require("../models/db.js")
const requestController = require("../controllers/request.controller")

module.exports = app