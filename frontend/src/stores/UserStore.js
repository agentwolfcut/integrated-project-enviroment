import { getItems } from "@/libs/fetchUtils";
import { createPinia, defineStore } from "pinia";
import { useToast } from "vue-toast-notification";

const token = localStorage.getItem("token");
const toast = useToast();

export const pinia = createPinia();
export const useUserStore = defineStore("user", {
  state: () => ({
    currentUser: null,
    users: null,
  }),
  actions: {
    setScheduler(user) {
      this.currentUser = user;
    },
    async findUser() {
      try {
        const data = await getItems(
            // รอ path
          `${import.meta.env.VITE_BASE_URL}/user`,
          token
        );
        if (data) {
          this.users = data;
        } else {
          toast.error("Failed to fetch Users OR You're guest user");
        }
      } catch (error) {
        toast.error(`error from UserStore : ${error}`);
      }
    },
  },
});
