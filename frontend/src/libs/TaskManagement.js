class TaskManagement {
  constructor(previoustask = []) {
    this.tasks = previoustask
  }
  addtasks(newtasks) {
    newtasks.forEach((newtask) =>
      this.addtask(
        newtask
      )
    )
  }
  // addtasks(newtasks) {
  //   newtasks.forEach((newtask) =>
  //     this.addtask(
  //       newtask.id,
  //       newtask.title,
  //       newtask.description,
  //       newtask.assignees,
  //       newtask.status,
  //       newtask.createdOn,
  //       newtask.updatedOn
  //     )
  //   )
  // }
  // push
  addtask(newTask) {
    this.tasks.push({
      id: newTask.id,
      title: newTask.title,
      description: newTask.description,
      assignees: newTask.assignees,
      status: newTask.status
      // status: status.split('_').map(words=> words.charAt(0).toUpperCase()+words.slice(1).toLowerCase()).join(' '),
    })
  }

  // map
  updatetask(newTask) {
    this.tasks = this.tasks.map((task) => {
      return task.id === newTask.id // if id correct
        ? // destructuring and replace with new
        {
          ...task,
          title: newTask.title,
          description: newTask.description,
          assignees: newTask.assignees,
          status: newTask.status
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
