const router = require('express').Router();
const { getAccessToRoute, getTodoOwnerAccess } = require('../middlewares/auth.middleware');

const {
    createTodoList,
    updateTodoList,
    deleteTodoList,
    getTodoLists
} = require('../controllers/todoController');

router.post('/', getAccessToRoute, createTodoList);

// returns IDs of todo lists belongs to a user
router.get('/', getAccessToRoute, getTodoLists);

router.put('/:id', getAccessToRoute, getTodoOwnerAccess, updateTodoList);
router.delete('/:id', getAccessToRoute, getTodoOwnerAccess, deleteTodoList);

module.exports = router;