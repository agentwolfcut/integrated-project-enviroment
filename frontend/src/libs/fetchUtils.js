// async task must wait , inside are promise
async function getItems(url) {
  try {
    const headers = {
      'Content-Type': 'application/json',
      'Authorization' : 'Bearer ' + localStorage.getItem('token')
    };
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

async function getItemById(url) {
  const headers = {
    "Content-Type": "application/json",
    'Authorization' : 'Bearer ' + localStorage.getItem('token')
  }
  try {
    const data = await fetch(`${url}` , {
      method: 'GET',
      headers: headers,
    })
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
      'Authorization' : 'Bearer ' + token
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
      'Authorization' : 'Bearer ' + token
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
  try {
    const res = await fetch(url, {
      method: "POST", // add
      headers: {
        "content-type": "application/json", // add contents
        'Authorization' : 'Bearer ' + localStorage.getItem('token')
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
  const token = localStorage.getItem('token'); // เพิ่มการประกาศ token
  try {
    const res = await fetch(`${url}/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        'Authorization': 'Bearer ' + token
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

async function getTasksByStatus(url, statusId) { // เพิ่ม statusId เป็นพารามิเตอร์
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
  try {
    const headers = {
      "Content-Type": "application/json",
      'Authorization': 'Bearer ' + localStorage.getItem('token')
    };

    const res = await fetch(url, {
      method: 'PATCH', // PATCH request
      headers: headers,
      body: JSON.stringify(patchData), // Send the data directly
    });
    
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }

    // Only try to parse JSON if there is a response body
    const text = await res.text();
    return text ? JSON.parse(text) : null; // If empty, return null or handle accordingly
  } catch (error) {
    console.error(`Error in patchItem: ${error}`);
    throw error; // Re-throw to handle in calling function
  }
}
// async function patchItem(url, patchData) {
//   try {
//     const headers = {
//       "Content-Type": "application/json",
//       'Authorization': 'Bearer ' + localStorage.getItem('token')
//     };

//     const res = await fetch(url, {
//       method: 'PATCH', // PATCH request
//       headers: headers,
//       body: JSON.stringify(patchData), // Send the data directly
//     });
    
//     if (!res.ok) {
//       throw new Error(`HTTP error! status: ${res.status}`);
//     }
//     // Parse and return the response
//     return await res.json();
//   } catch (error) {
//     console.error(`Error in patchItem: ${error}`);
//     throw error; // Re-throw to handle in calling function
//   }
// }

// destructuring
export {
  getItems,
  getItemById,
  deleteItemById,
  addItem,
  editItem,
  getTasksByStatus,
  transferTasksAndDeleteStatus,
  patchItem
};