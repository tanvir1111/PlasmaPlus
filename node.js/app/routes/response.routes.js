const express = require("express")
const app = express()
const db = require("../models/db.js")
const responseController = require("../controllers/response.controller")

module.exports = app