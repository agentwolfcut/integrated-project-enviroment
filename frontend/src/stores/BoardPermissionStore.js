import { defineStore } from "pinia"
import { useToast } from "vue-toast-notification"
import router from "@/router"
import { AuthUserStore } from "@/stores/Store.js"

const token = localStorage.getItem("token")
const toast = useToast()

export const useBoardPermissionStore = defineStore("boardPermission", {
  state: () => ({
    boardDetails: null,
    isOwner: false,
    hasAccess: false,
  }),

  actions: {
    async fetchBoardById(path, method) {
      const token = localStorage.getItem("token")
      const toast = useToast()
      const authStore = AuthUserStore() 

      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL + path}`, {
          method: method,
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })

        if (res.ok) {
          const boardData = await res.json()
          this.boardDetails = boardData
          const currentUser = localStorage.getItem("currentUser")
          const currentUserId = await authStore.findCurrentUser(currentUser)
          this.isOwner = boardData.ownerID === currentUserId
          this.hasAccess = this.isOwner || boardData.visibility === "PUBLIC"
          this.visibility = boardData.visibility
          
        } else if (res.status === 401) {
          router.push("/login")
          // real BE
        } else if (res.status === 403) {
          toast.error("You do not have permission to access this board.")
          router.push("/test")
        } else {
          toast.error("Failed to fetch board details.")
        }
      } catch (error) {
        console.error("Error fetching board details:", error)
        toast.error("An error occurred while fetching board details.")
      }
    },

    async updateVisibility(boardID, newVisibility) {
      const token = localStorage.getItem("token")
      const toast = useToast()

      try {
        const res = await fetch(
          `${import.meta.env.VITE_BASE_URL}/boards/${boardID}`,
          {
            method: "PATCH",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: `"visibility" : "${newVisibility}"`,
          }
        )
        if (res.ok) {
          this.visibility = newVisibility
          toast.success(`Visibility updated to ${newVisibility}`)
        } else if (res.status === 401) {
          router.push("/login")
        } else if (res.status === 403) {
          toast.error("You do not have permission to change visibility.")
        } else {
          toast.error("There was a problem. Please try again later.")
        }
      } catch (error) {
        console.error("Error changing visibility:", error)
        toast.error("An error occurred while changing visibility.")
      }
    },
  },
})
