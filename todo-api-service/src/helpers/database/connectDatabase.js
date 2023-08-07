const mongoose = require('mongoose');

const connectDatabase = () => {
    mongoose.connect(process.env.MONGODB_URI, {
        useNewUrlParser: true,
        useUnifiedTopology: true,
    })
        .then(() => {
            console.log("MongoDB Connection Succesful");
        })
        .catch((err) => {
            console.error('MongoDB connection error:', err.message);
        });
}

module.exports = connectDatabase;