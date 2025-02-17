# Yale User Guide

![Ui](Ui.png)

Yale is a chatbot that can help you manage your tasks.\
You can add and delete tasks, and track their completion.

## Viewing help: `help`

Lists out all the commands and their details.

Format: `help`

## Exiting the program: `bye`

Exits the application.

Format: 'bye'

## Listing all tasks: `list`

Lists out all the tasks in order.

Format: `list`

## Adding todos: `todo`

Adds a new task with no due date to the task list.

Format: `todo [name]`

Example: `todo read a book` adds the task `read a book`.

## Adding deadlines: `deadline`

Adds a new task with a due date to the task list.

Format: `deadline [name] /by [date]`

Example: `deadline submit iP /by Friday`
adds the task `submit iP` which is due by `Friday`.

## Adding events: `event`

Adds a new task with a start and end date to the task list.

Format: `event [name] /from [start] /to [end]`

Example: `event recess week /from 23 Feb /to 1 Mar`
adds the task `recess week` which lasts from
`23 Feb` to `1 Mar`.

## Marking tasks: `mark`

Marks the task at index `[id]` as completed.

Note that `[id]` needs to be a positive number, from 1
to however many tasks you have in the list.

Format: `mark [id]`

Example: `mark 2` marks the second task as complete.

## Unmarking tasks: `unmark`

Marks the task at index `[id]` as incomplete.

Format: `unmark [id]`

Example: `mark 3` marks the third task as incomplete.

## Deleting tasks: `delete`

Deletes the task at index [id].

Format: `delete [id]`

Example: `delete 1` deletes the first task in the list.

## Finding tasks: `find`

Lists out all the tasks that contain the keywords.

Format: `find [keyword]`

Example: If the task `read a book` is in the list,
the command `find read` will list it out as one of the tasks.