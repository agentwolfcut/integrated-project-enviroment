class TaskManagement {
  constructor(previoustask = []) {
    this.tasks = previoustask
  }
  addtasks(newtasks) {
    newtasks.forEach((newtask) =>
      this.addtask(newtask.id, newtask.title, newtask.description,
        newtask.assignees, newtask.status, newtask.createdOn, newtask.updatedOn)
    )
  }
  // push
  addtask(id, title, description, assignees, status, createdOn, updatedOn) {
    this.tasks.push({
      id: id,
      title: title,
      description: description,
      assignees: assignees,
      status: status,
      createdOn: createdOn,
      updatedOn: updatedOn
    })
  }

  // map
  updatetask(id, title, description, assignees, status, createdOn, updatedOn) {
    this.tasks = this.tasks.map((task) => {
      return task.id === id // if id correct
        // destructuring and replace with new
        ? {
          ...task, title: title, description: description,
          assignees: assignees, status: status, createdOn: createdOn, updatedOn: updatedOn
        }
        : task
    })
  }
  findtask(searchId) {
    return this.tasks.find((task) => task.id === searchId)
  }
  findIndextask(searchId) {
    return this.tasks.findIndex((task) => task.id === searchId)
  }
  removetask(removeId) {
    this.tasks.splice(
      this.tasks.findIndex((task) => task.id === removeId),
      1
    )
  }
  gettasks() {
    return this.tasks
  }
}
export { TaskManagement }