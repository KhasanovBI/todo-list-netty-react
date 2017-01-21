/*jshint quotmark:false */
/*jshint white:false */
/*jshint trailing:false */
/*jshint newcap:false */
var app = app || {};

(function () {
    'use strict';
    var Utils = app.Utils;
    var URL = '/api/todo_item/';
    app.TodoModel = function () {
        this.todos = [];
        this.getListTodo();
        this.onChanges = [];
    };

    app.TodoModel.prototype.subscribe = function (onChange) {
        this.onChanges.push(onChange);
    };

    app.TodoModel.prototype.inform = function () {
        this.onChanges.forEach(function (cb) {
            cb();
        });
    };

    app.TodoModel.prototype.create = function (title) {
        fetch(URL, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: title,
                completed: false
            })
        })
            .then(function (response) {
                return response.json();
            })
            .then(function (responseJSON) {
                this.todos.push(responseJSON);
                this.inform();
            }.bind(this))
            .catch(function (error) {
                console.error(error);
            });
    };

    app.TodoModel.prototype.update = function (todoItem, newParams) {
        var newTodoItem = Utils.extend({}, todoItem, newParams);
        fetch(URL + todoItem.id, {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newTodoItem)
        })
            .then(function (response) {
                return response.json();
            })
            .then(function (responseJSON) {
                this.todos = this.todos.map(function (todo) {
                    return todo !== todoItem ? todo : Utils.extend(todo, newParams);
                });
                this.inform();
            }.bind(this))
            .catch(function (error) {
                console.error(error);
            });
    };

    app.TodoModel.prototype.destroy = function (todoId) {
        return fetch(URL + todoId, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(function () {
                this.getListTodo();
            }.bind(this))
            .catch(function (error) {
                console.log(error);
            });
    };

    app.TodoModel.prototype.getListTodo = function () {
        return fetch(URL, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(function (response) {
                return response.json();
            })
            .then(function (responseJSON) {
                this.todos = responseJSON;
                this.inform();
            }.bind(this))
            .catch(function (error) {
                console.log(error);
            });
    };


    app.TodoModel.prototype.toggleAll = function (checked) {
        return fetch(URL + 'toggle', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({completed: checked})
        })
            .then(function (response) {
                return response.json();
            })
            .then(function (responseJSON) {
                this.todos = responseJSON;
                this.inform();
            }.bind(this))
            .catch(function (error) {
                console.log(error);
            });
    };

    app.TodoModel.prototype.toggle = function (todoToToggle) {
        this.update(todoToToggle, {completed: !todoToToggle.completed});
    };

    app.TodoModel.prototype.save = function (todoToSave, text) {
        this.update(todoToSave, {title: text});
    };

    app.TodoModel.prototype.clearCompleted = function () {
        return fetch(URL + 'clear_completed', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(function (response) {
                return response.json();
            })
            .then(function (responseJSON) {
                this.todos = responseJSON;
                this.inform();
            }.bind(this))
            .catch(function (error) {
                console.log(error);
            });
    };
})();
