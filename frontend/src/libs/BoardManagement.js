class BoardManagement {
    constructor(previousBoard = []) {
      this.Boards = previousBoard;
    }
  
    addBoards(newBoards) {
      newBoards.forEach((newBoard) =>
        this.addBoard(
          newBoard.id,
          newBoard.name,
          newBoard.ownerId
        )
      );
    }
  
    addBoard(id, name, ownerId) {
      this.Boards.push({
        id: id,
        name: name,
        ownerId: ownerId
      });
    }
  
    updateBoard(newBoard) {
      this.Boards = this.Boards.map((Board) => {
        return Board.id === newBoard.id
          ? {
              ...Board,
              name: newBoard.name,
              ownerId: newBoard.ownerId
            }
          : Board;
      });
    }
  
    findBoard(searchID) {
      return this.Boards.find((Board) => Board.id === searchID);
    }
  
    findIndexBoard(searchID) {
      return this.Boards.findIndex((Board) => Board.id === searchID);
    }
  
    removeBoard(removeID) {
      this.Boards.splice(
        this.Boards.findIndex((Board) => Board.id === removeID),
        1
      );
    }
  
    getBoards() {
      return this.Boards;
    }
  }
  
  export { BoardManagement };  