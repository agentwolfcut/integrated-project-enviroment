class StatusManagement {
    constructor(previousStatus = []) {
      this.statuses = previousStatus;
    }
  
    addStatuses(newStatuses) {
      newStatuses.forEach((newStatus) =>
        this.addStatus(
          newStatus.id,
          newStatus.name,
          newStatus.description
        )
      );
    }
  
    addStatus(id, name, description) {
      this.statuses.push({
        id: id,
        name: name,
        description: description
      });
    }
  
    updateStatus(id, name, description) {
      this.statuses = this.statuses.map((status) => {
        return status.id === id
          ? {
              ...status,
              name: name,
              description: description
            }
          : status;
      });
    }
  
    findStatus(searchID) {
      return this.statuses.find((status) => status.id === searchID);
    }
  
    findIndexStatus(searchID) {
      return this.statuses.findIndex((status) => status.id === searchID);
    }
  
    removeStatus(removeID) {
      this.statuses.splice(
        this.statuses.findIndex((status) => status.id === removeID),
        1
      );
    }
  
    getStatuses() {
      return this.statuses;
    }
  }
  
  export { StatusManagement };  