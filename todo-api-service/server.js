const express = require('express');
const morgan = require('morgan');
const dotenv = require('dotenv');
const todoRouter = require('./src/routes/todoRouter');
const eurekaHelper = require('./src/helpers/eureka/eureka-helper');
const connectDatabase = require('./src/helpers/database/connectDatabase');

dotenv.config({ path: "./src/config/.env" });

const app = express();
const PORT = process.env.PORT || 3000;

connectDatabase();

// Middleware
app.use(express.json());
app.use(morgan('dev'));

app.use('/api/v1/todo', todoRouter);

app.listen(PORT, () => {
  console.log(`App is up & running on port ${PORT}`);
});

eurekaHelper.registerWithEureka('todo-service', PORT);