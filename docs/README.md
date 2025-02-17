# Yale User Guide

![Ui](Ui.png)

Yale is a chatbot that can help you manage your tasks.\
You can add and delete tasks, and track their completion.

# Command summary
| Action       | Format                                 |
|--------------|----------------------------------------|
| Help         | `help`                                 |
| Exit         | `bye`                                  |
| List         | `list`                                 |
| Add todo     | `todo [name]`                          |
| Add deadline | `deadline [name] /by [date]`           |
| Add event    | `event [name] /from [start] /to [end]` |
| Mark         | `mark [id]`                            |
| Unmark       | `unmark [id]`                          |
| Delete       | `delete [id]`                          |
| Find         | `find [keyword]`                       |

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

## Adding deadlines: `deadline`

Adds a new task with a due date to the task list.

Format: `deadline [name] /by [date]`

## Adding events: `event`

Adds a new task with a start and end date to the task list.

Format: `event [name] /from [start] /to [end]`

## Marking tasks: `mark`

Marks the task at index `[id]` as completed.

Format: `mark [id]`

## Unmarking tasks: `unmark`

Marks the task at index `[id]` as incomplete.

Format: `unmark [id]`

## Deleting tasks: `delete`

Deletes the task at index [id].

Format: `delete [id]`

## Finding tasks: `find`

Lists out all the tasks that contain the keywords.

Format: `find [keyword]`

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```