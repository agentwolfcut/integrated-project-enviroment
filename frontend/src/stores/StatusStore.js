import { createPinia, defineStore } from "pinia";
import { useToast } from "vue-toast-notification";
import "vue-toast-notification/dist/theme-sugar.css";
import router from "@/router";
import { getItems } from "@/libs/fetchUtils";

const toast = useToast();

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
                    `${import.meta.env.VITE_BASE_URL}/boards/${boardID}/statuses`
                )
                this.statuses = statuses
            } catch (error) {
                toast.error('Error fetching statuses');
            }
        }
    }
})