const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const TodoListSchema = new Schema({
    metadata: {
        listHeader: {
            type: String,
            required: [true, 'Header is required'],
            default: " "
        },
        lastUpdated: {
            type: Date,
            required: true,
            default: Date.now(),
        },
        itemCount: {
            type: Number,
            required: true,
            default: 0
        },
        userId: {
            type: String,
            required: true
        }
    },
    items: [{
        type: String
    }]
})

TodoListSchema.pre('save', function (next) {
    // update the lastUpdated field of the metadata every time the list is updated
    this.metadata.lastUpdated = Date.now();
    this.metadata.itemCount = this.items.length;
    next();
});

module.exports = mongoose.model("Todo", TodoListSchema);