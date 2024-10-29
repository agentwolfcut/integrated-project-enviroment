import { createPinia, defineStore } from "pinia";
import { get } from "@/libs/Utils";
import { addItem, getItemById, getItems } from "@/libs/fetchUtils";
import { useToast } from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";
import router from "@/router";
import { useVisibilityStore } from "./VisibilityStore";

const toast = useToast();
const token = localStorage.getItem("token");

// interface ขึ้นกับ res ของpostman
export const pinia = createPinia();
export const AuthUserStore = defineStore("AuthUserStore", {
  state: () => ({
    // token: null,
    token: null,
    refreshToken: null,
    currentUser: null, // String
    tokenExpiry: null,
  }),

  actions: {
    setTokens(accessToken, refreshToken) {
      this.token = accessToken;
      this.refreshToken = refreshToken;
      localStorage.setItem("token", accessToken);
      localStorage.setItem("refreshToken", refreshToken);

      // Set the expiry for 30 minutes (access token lifetime)
      this.tokenExpiry = Date.now() + 30 * 60 * 1000;
      localStorage.setItem("tokenExpiry", this.tokenExpiry);
    },

    scheduleTokenRefresh() {
      const refreshInMs = this.tokenExpiry - Date.now() - 60 * 1000; // 1 minute before expiry
      if (refreshInMs > 0) {
        setTimeout(this.refreshToken, refreshInMs);
      }
    },

    setUser(user) {
      this.currentUser = user;
      localStorage.setItem("currentUser", user);
    },

    clearTokens() {
      this.token = null;
      this.refreshToken = null;
      this.tokenExpiry = null;
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('tokenExpiry');
    },

    async checkTokenValidity() {
      const token = localStorage.getItem("token");
      const refreshToken = localStorage.getItem("refreshToken");
      if (!token || !refreshToken) {
        this.clearTokens();
        router.push("/login");
        return;
      }

      try {
        // mockup
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (res.status === 200) {
          // Token ยัง valid
          return;
        } else if (res.status === 401) {
          // Token หมดอายุหรือไม่ถูกต้อง, ใช้ refresh token
          toast.info('use refreshToken')
          await this.refreshtoken();
        } else {
          this.clearTokens()
          toast.error("There is a problem. Please try again later.")
          router.push("/login");
        }
      } catch (error) {
        console.error("Error checking token validity:", error);
        this.clearTokens();
        router.push("/login");
      }
    },

    async refreshTokens() {
      const toast = useToast();
      const storedRefreshToken = localStorage.getItem('refreshToken');
      console.log(storedRefreshToken)
      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${storedRefreshToken}`,
          },
        });

        if (res.ok) {
          const data = await res.json();
          this.setTokens(data.accessToken, storedRefreshToken);
          this.scheduleTokenRefresh();  // Set the next refresh
        } else if (res.status === 401) {
          this.clearTokens();
          router.push('/login');
        }
      } catch (error) {
        toast.error('Failed to refresh token. Please login again.');
        this.clearTokens();
        router.push('/login');
      }
    },

    async checkAccessToken() {
      const storedAccessToken = localStorage.getItem('accessToken');
      const storedExpiry = localStorage.getItem('tokenExpiry');

      if (!storedAccessToken || !storedExpiry || Date.now() > storedExpiry) {
        await refreshTokens() // Try refreshing the token
      } else {
        this.accessToken = storedAccessToken;
        this.tokenExpiry = storedExpiry;
        this.scheduleTokenRefresh();
      }
    },

    async findCurrentUser(username) {
      try {
        const users = await getItems(`${import.meta.env.VITE_BASE_URL}/users`);
        const normalizedUsername = username
          .toLowerCase()
          .trim()
          .replace(/\s+/g, ".");
        const userFound = users.find(
          (item) =>
            item.username.toLowerCase().trim().replace(/\s+/g, ".") ===
            normalizedUsername
        );

        if (userFound) {
          this.currentUser = userFound.oid; // Store the oid of the matched user
          return userFound.oid; // Return the oid
        } else {
          toast.error("user not found");
          return null;
        }
      } catch (error) {
        console.error("Error fetching users:", error);
        return null;
      }
    },
  },
});

export const BoardStore = defineStore("BoardStore", {
  state: () => ({
    board: [], // Array ของบอร์ดทั้งหมด
    currentBoard: {}, // บอร์ดปัจจุบันที่กำลังจัดการ
    id: null, // ตัวแปรสำหรับเก็บ id
    tasks: [],
    currentBoardId: null,
    visibility: "private",
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
    getVisibility: (state) => state.visibility,
  },
  actions: {
    setCurrentBoardID(id) {
      this.currentBoardId = id;
    },
    async getBoard() {
      try {
        const data = await getItems(
          `${import.meta.env.VITE_BASE_URL}/boards`,
          token
        );
        if (data && Array.isArray(data)) {
          this.board = data; // อัปเดต array ของบอร์ด
          // this.visibility = data.visibility
          // this.visibility = data[0].visibility
        } else {
          toast.error("Failed to fetch Board or invalid data received.");
        }
      } catch (error) {
        console.error("Error fetching boards:", error);
        toast.error("An error occurred while fetching boards.");
      }
    },

    // async fetchBoardById(id) {
    //   try {
    //     const data = await getItems(
    //       `${import.meta.env.VITE_BASE_URL}/boards/${id}`,
    //       token
    //     )
    //     if (data) {
    //       return data
    //     } else {
    //       toast.error("Failed to fetch Board or invalid data received.")
    //       return null
    //     }
    //   } catch (error) {
    //     console.error("Error fetching board:", error)
    //     toast.error("An error occurred while fetching the board")
    //     return null
    //   }
    // }, // Action สำหรับดึงบอร์ดโดย ID
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
          this.board.push(data);
          router.push(`board/${this.id}`);
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
