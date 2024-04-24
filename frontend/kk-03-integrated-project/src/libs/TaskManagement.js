class TaskManagement {
    constructor(previoustask = []) {
      this.tasks = previoustask
    }
    addtasks(newtasks) {
      newtasks.forEach((newtask) =>
        this.addtask(newtask.id, newtask.category, newtask.description)
      )
    }
    // push
    addtask(id, category, desc) {
      this.tasks.push({
        id: id,
        category: category,
        description: desc
      })
    }
  
    // map
    updatetask(id, category, description) {
      this.tasks = this.tasks.map((task) => {
        return task.id === id // if id correct
        // destructuring and replace with new
          ? { ...task, category: category, description: description }
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