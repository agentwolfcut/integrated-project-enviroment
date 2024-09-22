import { createPinia , defineStore } from "pinia";
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