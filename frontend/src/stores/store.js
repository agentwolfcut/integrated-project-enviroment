import { createPinia, defineStore } from "pinia";
import { get } from "@/libs/Utils";
import { addItem, getItemById, getItems } from "@/libs/fetchUtils";
import { useToast } from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";
import router from "@/router";

const toast = useToast();
const token = localStorage.getItem("token");

// interface ขึ้นกับ res ของpostman

export const pinia = createPinia();
export const AuthUserStore = defineStore("AuthUserStore", {
  state: () => ({
    token: null,
    currentUser: null,
  }),
  actions: {
    setToken(token) {
      this.token = token;
    },
    setUser(user) {
      this.currentUser = user;
    },
    clearToken() {
      this.token = null;
      this.currentUser = null;
    }
  },
});

export const BoardStore = defineStore("BoardStore", {
  state: () => ({
    board: [], // Array ของบอร์ดทั้งหมด
    currentBoard: {}, // บอร์ดปัจจุบันที่กำลังจัดการ
    id: null, // ตัวแปรสำหรับเก็บ id
    tasks: [],
    currentBoardId: null,
  }),
  getters: {
    getBoards: (state) => state.board,
    getCurrentBoard: (state) => state.currentBoard,
    getid: (state) => (id) => {
      return state.board.find((board) => board.id === id);
    },
    getTasks: (state) => state.tasks, // Getter สำหรับดึง tasks ของบอร์ด
    getBoardById: (state) => (id) => {
      return state.board.find((board) => board.id === id);
    },
  },
  actions: {
    setCurrentBoardID(id) {
      this.currentBoardId = id;
    } ,
    async getBoard() {
      try {
        const data = await getItems(
          `${import.meta.env.VITE_BASE_URL}/boards`,
          token
        );
        if (data && Array.isArray(data)) {
          this.board = data; // อัปเดต array ของบอร์ด
        } else {
          toast.error("Failed to fetch Board or invalid data received.");
        }
      } catch (error) {
        console.error("Error fetching boards:", error);
        toast.error("An error occurred while fetching boards.");
      }
    },
    async fetchBoardById(id) {
      try {
        const data = await getItems(
          `${import.meta.env.VITE_BASE_URL}/boards/${id}`,
          token
        );
        if (data) {
          return data;
        } else {
          toast.error("Failed to fetch Board or invalid data received.");
          return null;
        }
      } catch (error) {
        console.error("Error fetching board:", error);
        toast.error("An error occurred while fetching the board");
        return null;
      }
    }, // Action สำหรับดึงบอร์ดโดย ID
    async addBoard(name) {
      try {
        const data = await addItem(`${import.meta.env.VITE_BASE_URL}/boards`, {
          name: name,
        });
        if (data.status < 200 || data.status >= 300) {
          if (data.status === 401) {
            router.push("/login");
          } else {
            toast.error("Failed to add Board");
          }
        } else {
          toast.success("Board added successfully");
          // เก็บ id จากการตอบกลับ
          this.currentBoard = data; // บันทึกบอร์ดที่เพิ่มใหม่ใน currentBoard
          this.id = data.id; // เก็บค่า id แยกไว้
          this.board.push(data); // เพิ่มบอร์ดใน array
          // router.push(`board/${this.id}`)
        }
      } catch (error) {
        console.error("Error adding board:", error);
        toast.error("An error occurred while adding the board");
      }
    },
    async addTask(taskData, id) {
      try {
        const data = await addItem(
          `${import.meta.env.VITE_BASE_URL}/boards/${id}/tasks`,
          taskData,
          token
        );
        if (data.status < 200 || data.status >= 300) {
          toast.error("Failed to add Task");
        } else {
          toast.success("Task added successfully");
          // อัปเดต tasks ใน store ด้วย task ใหม่
          this.tasks.push(data); // เพิ่ม task ใน array tasks
        }
      } catch (error) {
        console.error("Error adding task:", error);
        toast.error("An error occurred while adding the task");
      }
    },
  },
});
