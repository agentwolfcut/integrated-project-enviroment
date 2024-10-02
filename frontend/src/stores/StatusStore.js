import { createPinia, defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";
import router from "@/router";
import { getItems } from "@/libs/fetchUtils";

const toast = useToast();
const token = localStorage.getItem("token");

export const pinia = createPinia();
export const useStatusStore = defineStore('status' , {
    state: () => ({
        statuses : [],
    }),
    getters:{
        getStatuses : (state) => state.statuses,
        getStatusById : (state) => (id) => state.statuses.find((status) => status.id === id)
    },
    actions : {
        async fetchStatus(boardID) {
            try {
                const statuses = await getItems (
                    `${import.meta.env.VITE_BASE_URL}/boards/${boardID}/tasks`
                )
                this.statuses = statuses
            } catch (error) {
                console.log('Error fetching statuses: ' , error);
                toast.error('Error fetching statuses');
            }
        }
    }
})