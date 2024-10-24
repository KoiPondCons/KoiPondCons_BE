/** @type {CodeceptJS.MainConfig} */
require('./heal')
require('dotenv').config()
exports.config = {
  tests: './*_test.js',
  output: './output',
  helpers: {
    REST: {
      // defaultHeaders: {
      //   // use Bearer Authorization
      //   'Authorization': `Bearer ${token}`,
      //   'Content-Type': 'application/json',
      //   'Accept': 'application/json',
      // },
      endpoint: 'http://localhost:8080/api'
    },
    Playwright: {
      browser: 'chromium',
      url: 'http://localhost:8080',
      show: true
    },
    AI:{},
    JSONResponse: {}
  },
  include: {
    I: './steps_file.js'
  },
  name: 'koipondcons',
  ai: {
    request: async (messages) => {
      const Groq = require('groq-sdk');
      const groq = new Groq({
        apiKey: process.env.GROQ_API_KEY
      });
      const chatCompletion = await groq.chat.completions.create({
        messages,
        model: "mixtral-8x7b-32768",
      });
      return chatCompletion.choices[0]?.message?.content || "";
    }
  },
  plugins: {
    heal: {
      enabled: true
    }
  }
}