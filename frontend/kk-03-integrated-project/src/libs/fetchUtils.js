const API_URL = "http://localhost:5000"
// async task must wait , inside are promise
async function getItems(url) { //ส่งurl
    try {
      const data = await fetch(url) //GET METHOD อย่างเดียว default + await รอ promise ของ fetch
      const items = await data.json() // converse json => js object
      return items
    } catch (error) {
      console.log(`error: ${error}`)
    }
  }


  async function getItemById(url, id) {
    try {
      const data = await fetch(`${url}/${id}`)
      const item = await data.json()
      return item
    } catch (error) {
      console.log(`error: ${error}`)
    }
  }
  
  async function deleteItemById(url, id) {
    console.log(`${url}/${id}`)
  
    try {
      const res = await fetch(`${url}/${id}`, {
        method: 'DELETE'
      })
      return res.status // number of network
    } catch (error) {
      console.log(`error: ${error}`)
    }
  }
  
  async function addItem(url, newItem) {
    try {
      const res = await fetch(url, {
        method: 'POST', // add
        headers: {
          'content-type': 'application/json' // add contents
        },
        body: JSON.stringify({
          ...newItem // sent add data . destructuring object
        }) // js to  json
      })
      const addedItem = await res.json()
      return addedItem
    } catch (error) {
      console.log(`error: ${error}`)
    }
  }
  
  // update with replace entire items
  // but PATCH modify some field
  async function editItem(url, id, editItem) {
    try {
      const res = await fetch(`${url}/${id}`, {
        method: 'PUT',
        headers: {
          'content-type': 'application/json'
        },
        body: JSON.stringify({
          ...editItem
        })
      })
      const editedItem = await res.json()
      return editedItem
    } catch (error) {
      console.log(`error: ${error}`)
    }
  }
  
  // destructuring
  export { getItems, getItemById, deleteItemById, addItem, editItem , API_URL }