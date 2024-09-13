// utils.js
export async function fetchData(url, method = 'GET', body = null, headers = {}) {
    try {
      const options = {
        method,
        headers: {
          'Content-Type': 'application/json',
          ...headers,
        },
      };
  
      if (body) {
        options.body = JSON.stringify(body);
      }
  
      const response = await fetch(url, options);
  
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
  
      return await response.json();
    } catch (error) {
      console.error('Fetch error:', error);
      throw error;
    }
  }
  
  export function get(url, headers = {}) {
    return fetchData(url, 'GET', null, headers);
  }
  
  export function post(url, body, headers = {}) {
    return fetchData(url, 'POST', body, headers);
  }
  
  export function put(url, body, headers = {}) {
    return fetchData(url, 'PUT', body, headers);
  }
  
  export function del(url, headers = {}) {
    return fetchData(url, 'DELETE', null, headers);
  }