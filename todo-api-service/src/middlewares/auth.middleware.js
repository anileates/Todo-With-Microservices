const jwt = require('jsonwebtoken');
const TodoList = require('../models/TodoList');
const asyncHandler = require('express-async-handler');

const getAccessToRoute = (req, res, next) => {
    const { JWT_SECRET_KEY } = process.env;

    if (!isTokenIncluded(req)) {
        return res.status(401).json({ message: "Token is not included"})
    }

    const accessToken = getAccessTokenFromHeader(req);

    jwt.verify(accessToken, JWT_SECRET_KEY, async (err, decodedUserInfos) => {
        if (err) {
            return res.status(401).json({ message: "Token is not valid"})
        }

        req.loggedUser = {
            id: decodedUserInfos.sub,
        };

        next();
    })
};

const getTodoOwnerAccess = asyncHandler(async (req, res, next) => {
    const userId = req.loggedUser.id;
    const todo = await TodoList.findById(req.params.id);

    if (todo.metadata.userId != userId) {
        return res.status(401).json({ message: "You are not authorized to access this route"})
    }

    next();
});

const getAccessTokenFromHeader = req => {
    const authorization = req.headers.authorization;

    const access_token = authorization.split(" ")[1];
    return access_token;
}

const isTokenIncluded = req => {
    return req.headers.authorization && req.headers.authorization.startsWith("Bearer")
};

module.exports = {
    getAccessToRoute,
    getTodoOwnerAccess
}