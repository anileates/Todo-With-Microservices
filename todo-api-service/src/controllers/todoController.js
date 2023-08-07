const asyncHandler = require('express-async-handler');
const TodoList = require('../models/TodoList');

const createTodoList = asyncHandler(async (req, res, next) => {
    const userId = req.loggedUser.id;

    if (!req.body.listHeader) return res.status(400).send("Please provide a list title");

    const todoList = await TodoList.create({
        metadata: {
            listHeader: req.body.listHeader,
            userId
        }
    });

    return res.status(200).json({
        success: true,
        data: todoList,
    })
})

const updateTodoList = asyncHandler(async (req, res, next) => {
    if (!req.body.listHeader || !req.body.items) {
        return res.status(400).json({
            success: false,
            message: "Please provide a list header and items"
        })
    }

    const todoList = await TodoList.findById(req.params.id);
    if (!todoList) {
        return res.status(404).json({
            success: false,
            message: "Todo list not found",
        })
    }

    todoList.metadata.listHeader = req.body.listHeader;
    todoList.items = req.body.items;

    let newTodo = await todoList.save();
    return res.status(200).json({
        success: true,
        data: newTodo,
    })
})

const deleteTodoList = asyncHandler(async (req, res, next) => {
    await TodoList.findByIdAndDelete(req.params.id);

    return res.sendStatus(200);
})

const getTodoLists = asyncHandler(async (req, res, next) => {
    const todoLists = await TodoList.find({ "metadata.userId": req.loggedUser.id }); 

    return res.status(200).json({
        success: true,
        data: todoLists,
    })
})

module.exports = { createTodoList, updateTodoList, deleteTodoList, getTodoLists };