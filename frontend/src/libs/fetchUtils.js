// async task must wait , inside are promise
async function getItems(url, token = null) {
  try {
    const headers = {
      'Content-Type': 'application/json',
    };
    if (token) {
      headers['Authorization'] = `${token}`;
    }
    const response = await fetch(url, {
      method: 'GET',
      headers: headers,
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const items = await response.json();
    return items;
  } catch (error) {
    console.log(`error: ${error}`);
    throw error; // Re-throw the error to handle it in the calling function
  }
}

async function getItemById(url, id) {
  try {
    const data = await fetch(`${url}/${id}`);
    const item = await data.json();
    return item;
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function deleteItemById(url, id ,token = null) {
  try {
    const headers = {
      'Content-Type': 'application/json',
    };
    if (token) {
      headers['Authorization'] = `${token}`;
    }
    const res = await fetch(`${url}/${id}`, {
      method: "DELETE",
      headers: headers,
    });
    return res.status; // number of network
  } catch (error) {
    console.log(`error: ${error}`);
  }
}

async function transferTasksAndDeleteStatus(url, id, desId , token = null) {
  try {
    const headers = {
      'Content-Type': 'application/json',
    };
    if (token) {
      headers['Authorization'] = `${token}`;
    }
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
  try {
    const res = await fetch(url, {
      method: "POST", // add
      headers: {
        "content-type": "application/json", // add contents
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

// update with replace entire items
// but PATCH modify some field
async function editItem(url, id, editItem) {
  try {
    const res = await fetch(`${url}/${id}`, {
      method: "PUT",
      headers: {
        "content-type": "application/json",
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
async function getTasksByStatus(url, id) {
  try {
    const res = await fetch(`${url}/${id}`);
    const allTasks = res.json();
    // filter
    const filteredTasks = allTasks.filter((task) => task.statusId === statusId);
    return filteredTasks;
  } catch (error) {
    console.log(`error: ${error}`);
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
};
