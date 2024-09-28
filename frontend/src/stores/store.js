import { createPinia , defineStore } from "pinia";
import {get , post} from '@/libs/Utils'
import {useToast} from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-sugar.css';

const toast = useToast();
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

export const BoardStore = defineStore('BoardStore' , {
  state: () => ({
    board: [],
    currentBoard : {}
  }),
  getters : {
    getBoards : (state) => state.board,
    getCurrentBoard : (state) => state.currentBoard,
    getBoardId : (state) => (id) => {
      return state.board.find((board) => board.id === id)
    }
  },
  actions : {
    async getBoard() {
      try {
        const data = await get(`${import.meta.env.VITE_BASE_URL}/boards`,token)

        if (data.status < 200 & data.status > 299) {
          toast.error('Failed to fetch Board')
        } else {
          this.board = data
        }
      } catch(error) {
          console.log(error);
          toast.error(error)
      }
    }

   
  }
})