import { createPinia , defineStore } from "pinia";
import {get} from '@/libs/Utils'
import {addItem , getItems} from '@/libs/fetchUtils'
import {useToast} from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-sugar.css';

const toast = useToast();
const token = localStorage.getItem('token');

export const pinia = createPinia()
export const AuthUserStore = defineStore('AuthUserStore' , {
    state: () => ({
      token: null,
      currentUser: null
    }),
    actions: {
      setToken(token) {
        this.token = token
      },
      setUser(user) {
        this.currentUser = user
      },
      clearToken() {
        this.token = null;
        this.currentUser = null;
      }
    }
});

export const BoardStore = defineStore('BoardStore', {
  state: () => ({
    board: [], // Array ของบอร์ดทั้งหมด
    currentBoard: {}, // บอร์ดปัจจุบันที่กำลังจัดการ
    boardID: null, // ตัวแปรสำหรับเก็บ boardID
    tasks: []
  }),
  getters: {
    getBoards: (state) => state.board,
    getCurrentBoard: (state) => state.currentBoard,
    getBoardId: (state) => (id) => {
      return state.board.find((board) => board.boardID === id)
    },
    getTasks: (state) => state.tasks // Getter สำหรับดึง tasks ของบอร์ด

  },
  actions: {
    async getBoard() {
      try {
        const data = await getItems(`${import.meta.env.VITE_BASE_URL}/boards`, token);
        if (data && Array.isArray(data)) {
          this.board = data; // อัปเดต array ของบอร์ด
        } else {
          toast.error('Failed to fetch Board or invalid data received.');
        }
      } catch (error) {
        console.error('Error fetching boards:', error);
        toast.error('An error occurred while fetching boards.');
      }
    },

    async addBoard(name) {
      try {
        const data = await addItem(`${import.meta.env.VITE_BASE_URL}/boards`, { "boardName": name });
        if (data.status < 200 || data.status >= 300) {
          toast.error('Failed to add Board');
        } else {
          toast.success('Board added successfully');
          
          // เก็บ boardID จากการตอบกลับ
          this.currentBoard = data; // บันทึกบอร์ดที่เพิ่มใหม่ใน currentBoard
          this.boardID = data.boardID; // เก็บค่า boardID แยกไว้          
          this.board.push(data); // เพิ่มบอร์ดใน array
        }
      } catch (error) {
        console.error('Error adding board:', error);
        toast.error('An error occurred while adding the board');
      }
    },
    async addTask(taskData, boardID) {
      try {
        const data = await addItem(`${import.meta.env.VITE_BASE_URL}/boards/${boardID}/tasks`, taskData, token);
        if (data.status < 200 || data.status >= 300) {
          toast.error('Failed to add Task');
        } else {
          toast.success('Task added successfully');
          // อัปเดต tasks ใน store ด้วย task ใหม่
          this.tasks.push(data); // เพิ่ม task ใน array tasks
        }
      } catch (error) {
        console.error('Error adding task:', error);
        toast.error('An error occurred while adding the task');
      }
    }
  }
});
