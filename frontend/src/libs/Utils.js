// import { useRoute } from "vue-router";
// const router = useRoute()
// export async function fetchData(
//   url,
//   method = "GET",
//   body = null,
//   headers = {}
// ) {
//   try {
//     const options = {
//       method,
//       headers: {
//         "Content-Type": "application/json",
//         ...headers,
//       },
//     };
//     if (body) {
//       options.body = JSON.stringify(body);
//     }
//     const response = await fetch(url, options);
//     if (response.status === 401) {
//         // reset state
//         localStorage.removeItem('token')
//         // redirect to login
//         router.push('/login')
//         throw new Error('Unauthorized. Redirecting to login page.');
//     }
//     return response.json();
//   } catch (error) {
//     throw error;
//   }
// }

async function get(url, token) {
  try {
    const data = await fetch(url, {
      headers: {
        'content-type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      }
    })
    const items = await data.json().then((response) => {
      return response
    })
    return items
  } catch (error) {
    console.log(`error: ${error}`)
  }
}

async function del(url, id) {
  try {
    const res = await fetch(`${url}/${id}`, {
      method: 'DELETE',
      headers: {
        'content-type': 'application/json',
        'authorization': `Bearer ${localStorage.getItem('token')}`,
      }
    })

    return res.status
  } catch (error) {
    console.log(`Error: ${error}`)
  }
}

async function post(url, newItem) {
  try {
    const res = await fetch(url, {
      method: 'POST', // add
      headers: {
        'content-type': 'application/json',
        'authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify({
        ...newItem, // sent add data . destructuring object
      }), // js to  json
    })
    const addedItem = await res.json()
    return addedItem
  } catch (error) {
    console.log(`error: ${error}`)
  }
}

async function put(url, id, editItem) {
  try {
    const res = await fetch(`${url}/${id}`, {
      method: 'PUT',
      headers: {
        'content-type': 'application/json',
        'authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: JSON.stringify({
        ...editItem,
      }),
    })
    const editedItem = await res.json()
    return editedItem
  } catch (error) {
    console.log(`error: ${error}`)
  }
}
export {
  post , put , del , get
}