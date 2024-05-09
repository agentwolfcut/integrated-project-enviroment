class StatusManagement {
    constructor(previousStatus = []) {
      this.statuses = previousStatus;
    }
  
    addStatuses(newStatuses) {
      newStatuses.forEach((newStatus) =>
        this.addStatus(
          newStatus.statusID,
          newStatus.statusName,
          newStatus.statusDescription
        )
      );
    }
  
    addStatus(statusID, statusName, statusDescription) {
      this.statuses.push({
        statusID: statusID,
        statusName: statusName,
        statusDescription: statusDescription
      });
    }
  
    updateStatus(statusID, statusName, statusDescription) {
      this.statuses = this.statuses.map((status) => {
        return status.statusID === statusID
          ? {
              ...status,
              statusName: statusName,
              statusDescription: statusDescription
            }
          : status;
      });
    }
  
    findStatus(searchID) {
      return this.statuses.find((status) => status.statusID === searchID);
    }
  
    findIndexStatus(searchID) {
      return this.statuses.findIndex((status) => status.statusID === searchID);
    }
  
    removeStatus(removeID) {
      this.statuses.splice(
        this.statuses.findIndex((status) => status.statusID === removeID),
        1
      );
    }
  
    getStatuses() {
      return this.statuses;
    }
  }
  
  export { StatusManagement };  