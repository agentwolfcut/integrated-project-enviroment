import { useToast } from "vue-toast-notification";
import router from "@/router";
import { AuthUserStore } from "@/stores/store";
const toast = useToast();

// async task must wait , inside are promise
async function getItems(url) {
  try {
    const useAuthStore = AuthUserStore();
    useAuthStore.checkAccessToken();
    const token = useAuthStore.token;
    let headers = {};
    if (token) {
      headers = {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      };
    } else {
      console.log(token);

      headers = {
        "Content-Type": "application/json",
      };
    }
    const response = await fetch(url, {
      method: "GET",
      headers: headers,
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status getitems from : ${response.status}`);
    }
    const items = await response.json();
    return items;
  } catch (error) {
    console.log(`error: ${error}`);
    throw error; // Re-throw the error to handle it in the calling function
  }
}

async function getItemById(url) {
  const useAuthStore = AuthUserStore();
  useAuthStore.checkAccessToken();
  const token = useAuthStore.token;
  let headers = {};
  if (token) {
    headers = {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    };
  } else {
    headers = {
      "Content-Type": "application/json",
    };
  }
  try {
    const data = await fetch(`${url}`, {
      method: "GET",
      headers: headers,
    });
    const item = await data.json();
    return item;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function deleteItemById(url, id, token = null) {
  try {
    const headers = {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    };
    const res = await fetch(`${url}/${id}`, {
      method: "DELETE",
      headers: headers,
    });
    return res.status; // number of network
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function transferTasksAndDeleteStatus(url, id, desId, token = null) {
  try {
    const headers = {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    };
    const res = await fetch(`${url}/${id}/${desId}`, {
      method: "DELETE",
      headers: headers,
    });
    return res.status;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function addItem(url, newItem) {
  const token = useAuthStore.token;
  try {
    const res = await fetch(url, {
      method: "POST", // add
      headers: {
        "content-type": "application/json", // add contents
        Authorization: "Bearer " + token,
      },
      body: JSON.stringify({
        ...newItem, // sent add data . destructuring object
      }), // js to  json
    });
    const addedItem = await res.json();
    return addedItem;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function editItem(url, id, editItem) {
  const token = useAuthStore.token;
  try {
    const res = await fetch(`${url}/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
      body: JSON.stringify({
        ...editItem,
      }),
    });
    const editedItem = await res.json();
    return editedItem;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function getTasksByStatus(url, statusId) {
  // เพิ่ม statusId เป็นพารามิเตอร์
  try {
    const res = await fetch(`${url}/${statusId}`);
    const allTasks = await res.json(); // เพิ่ม await

    // Filter tasks ที่มี statusId ตรงกับที่ต้องการ
    const filteredTasks = allTasks.filter((task) => task.statusId === statusId);
    return filteredTasks;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function patchItem(url, patchData) {
  const useAuthStore = AuthUserStore();
  useAuthStore.checkAccessToken();
  const token = useAuthStore.token;

  try {
    const headers = {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    };

    const res = await fetch(url, {
      method: "PATCH", // PATCH request
      headers: headers,
      body: JSON.stringify(patchData), // Send the data directly
    });

    if (res.ok) {
      toast.success(`updated visibility successfully`);
    } else if (res.status === 401) {
      localStorage.removeItem("token");
      router.push("/login");
    } else if (res.status === 403) {
      toast.error("You do not have permission to change board visibility mode");
    }
    // Only try to parse JSON if there is a response body
    const text = await res.text();
    return text ? JSON.parse(text) : null; // If empty, return null or handle accordingly
  } catch (error) {
    console.error(`Error in patchItem: ${error}`);
    throw error; // Re-throw to handle in calling function
  }
}

// destructuring
export {
  getItems,
  getItemById,
  deleteItemById,
  addItem,
  editItem,
  getTasksByStatus,
  transferTasksAndDeleteStatus,
  patchItem,
};
