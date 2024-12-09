import { createPinia, defineStore } from "pinia";
import { addItem, getItems } from "@/libs/fetchUtils";
import { useToast } from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";
import router from "@/router";
import jwtDecode from "vue-jwt-decode";
import VueJwtDecode from "vue-jwt-decode";
import { useUserStore } from "./UserStore";

const toast = useToast();
// interface ขึ้นกับ res ของpostman
export const pinia = createPinia();

export const AuthUserStore = defineStore("AuthUserStore", {
  state: () => ({
    token: null,
    refreshToken: null,
    currentUser: null, // String
    tokenExpiry: null,
    refreshTokenExpiry : null
  }),

  actions: {
    setTokens(accessToken, refreshToken) {
      this.token = accessToken;
      this.refreshToken = refreshToken;
      //acess token lifetime
      this.tokenExpiry = Date.now() + 30 * 60 * 1000;
      // Refresh token lifetime (24 hours)
      this.refreshTokenExpiry = Date.now() + 24 * 60 * 60 * 1000; // 24 hours
      localStorage.setItem("token", accessToken);
      localStorage.setItem("refreshToken", refreshToken);
      localStorage.setItem("tokenExpiry", this.tokenExpiry);
      localStorage.setItem("refreshTokenExpiry", this.refreshTokenExpiry); // Store refresh token expiry

    },

    clearTokens() {
      this.token = null;
      this.refreshToken = null;
      this.tokenExpiry = null;
      this.refreshTokenExpiry = null; // Clear refresh token expiry
      localStorage.removeItem("token");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("tokenExpiry");
      localStorage.removeItem("refreshTokenExpiry"); // Remove refresh token expiry from localStorage
    },

    checkTokenValidity2(token) {
      try {
        const decodedToken = jwtDecode.decode(token);
        const currentTime = Math.floor(Date.now() / 1000);
        const remainingTime = decodedToken.exp - currentTime;
        if (remainingTime > 0) {
          console.log(
            `${tokenType} token will expire in ${remainingTime} seconds`
          );
          return true;
        } else {
          console.log(`${tokenType} token has expired`);
          return false;
        }
      } catch (error) {
        console.error(`Error decoding ${tokenType} token:`, error);
        return false;
      }
    },

    // real use
    async refreshTokens() {
      const toast = useToast();
      const storedRefreshToken = localStorage.getItem("refreshToken")
      const storedRefreshTokenExpiry = localStorage.getItem("refreshTokenExpiry");

      if (!storedRefreshToken) {
        this.clearTokens();
        throw new Error("No refresh token found");
      }

      // Check if the refresh token has expired
      if (Date.now() > storedRefreshTokenExpiry) {
        this.clearTokens();
        toast.error("Refresh token has expired. Please login again.");
        router.push("/login");
        return;
      }

      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token`, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${storedRefreshToken}`,
          },
          body: JSON.stringify({}), // add
        });

        if (res.ok) {
          const data = await res.json();
          // from backend
          this.setTokens(data.access_token, storedRefreshToken);
          this.scheduleTokenRefresh(); // Set the next refresh
          return data.access_token;
        } else if (res.status === 401) {
          toast.error('invalid token')
          router.push('/login')
          this.clearTokens();
        }
      } catch (error) {
        toast.error("Failed to refresh token. Please login again.");
        this.clearTokens();
        router.push("/login");
      }
    },

    scheduleTokenRefresh() {
      const refreshInMs = this.tokenExpiry - Date.now() - 60 * 1000; // 1 minute before access token expiry
      if (refreshInMs > 0) {
        setTimeout(this.refreshTokens, refreshInMs);
      }
      // Optionally, you could also check refresh token expiry here if needed
    },

    // async checkTokenValidity() {
    //   const token = localStorage.getItem("token");
    //   const refreshToken = localStorage.getItem("refreshToken");
    //   if (!token || !refreshToken) {
    //     this.clearTokens();
    //     router.push("/login");
    //     return;
    //   }
    //   try {
    //     // mockup
    //     const res = await fetch(`${import.meta.env.VITE_BASE_URL}/token`, {
    //       headers: { Authorization: `Bearer ${token}` },
    //     });

    //     if (res.status === 200) {
    //       // Token ยัง valid
    //       return;
    //     } else if (res.status === 401) {
    //       // Token หมดอายุหรือไม่ถูกต้อง, ใช้ refresh token
    //       toast.info("use refreshToken");
    //       await this.refreshtoken();
    //     } else {
    //       this.clearTokens();
    //       toast.error("There is a problem. Please try again later.");
    //       router.push("/login");
    //     }
    //   } catch (error) {
    //     console.error("Error checking token validity:", error);
    //     this.clearTokens();
    //     router.push("/login");
    //   }
    // },

    // ยังไม่เช็ค expiry
    async checkAccessToken() {      
      const storedToken = localStorage.getItem("token");
      const storedExpiry = localStorage.getItem("tokenExpiry");

      if (storedToken !== this.token) {
        await this.refreshTokens(); // Try refreshing the token
        
      } else {
        // console.log(`new is ${storedToken} , old is ${this.token}`);
        this.token = storedToken;
        this.tokenExpiry = storedExpiry;
        
      }
    },

    setUser(user) {
      this.currentUser = user;
      localStorage.setItem("currentUser", user);
    },

    async findCurrentUser (username) {
      const useAuthStore = AuthUserStore()
    
      // Ensure the access token is checked and refreshed if necessary
      await useAuthStore.checkAccessToken(); // Wait for the token check to complete
    
      const token = useAuthStore.token; // Get the current token after checking
      if (token) {
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
            this.currentUser  = userFound.oid; // Store the oid of the matched user
            return userFound.oid; // Return the oid
          } else {
            toast.error("User  not found");
            return null;
          }
        } catch (error) {
          console.error("Error fetching users:", error);
          return null;
        }
      } else {
        return null; // If no token, return null
      }
    }
  },
});

export const BoardStoreOld = defineStore("BoardStore", {
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
      const useAuthStore = AuthUserStore();
      useAuthStore.checkAccessToken();
      const token = localStorage.getItem("token");
      try {
        const data = await getItems(
          `${import.meta.env.VITE_BASE_URL}/boards`,
          token
        );
        if (data && Array.isArray(data)) {
          this.board = data;
        } else {
          console.log('Failed to fetch Board or invalid data received.')
          // toast.error("Failed to fetch Board or invalid data received.");
        }
      } catch (error) {
        console.error("Error fetching boards:", error);
      }
    },

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
      const useAuthStore = AuthUserStore();
      const token = useAuthStore.token;
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

export const BoardStore = defineStore("BoardStore", {
  state: () => ({
    ownedBoards: [], // Boards owned by the user
    collabBoards: [], // Boards where the user is a collaborator
    currentBoard: {}, // The board currently being managed
    currentBoardId: null,
    tasks: [],
    visibility: "private",
  }),
  getters: {
    getOwnedBoards: (state) => state.ownedBoards,
    getCollabBoards: (state) => state.collabBoards,
    getCurrentBoard: (state) => state.currentBoard,
    getBoardById: (state) => (id) => {
      // Look for the board in both ownedBoards and collabBoards
      return (
        state.ownedBoards.find((board) => board.id === id) ||
        state.collabBoards.find((board) => board.id === id)
      );
    },
  },
  actions: {
    setCurrentBoardID(id) {
      this.currentBoardId = id;
    },
    async getBoards() {
      const authStore = AuthUserStore();
      authStore.checkAccessToken();
      const token = localStorage.getItem("token");

      try {
        const data = await getItems(`${import.meta.env.VITE_BASE_URL}/boards`, token);

        if (data) {
          // Store the boards in the correct state properties
          this.ownedBoards = data.ownedBoards || [];
          this.collabBoards = data.collabBoards || [];
        } else {
          console.error("Failed to fetch boards or invalid data received.");
        }
      } catch (error) {
        console.error("Error fetching boards:", error);
      }
    },
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
          // Add the new board to ownedBoards
          this.ownedBoards.push(data);
          this.currentBoard = data;
          this.currentBoardId = data.id;
          router.push(`board/${this.currentBoardId}`);
        }
      } catch (error) {
        console.error("Error adding board:", error);
        toast.error("An error occurred while adding the board");
      }
    },
  },
});
