// utils.js
import { useRoute } from "vue-router";
const router = useRoute()
export async function fetchData(
  url,
  method = "GET",
  body = null,
  headers = {}
) {
  try {
    const options = {
      method,
      headers: {
        "Content-Type": "application/json",
        ...headers,
      },
    };
    if (body) {
      options.body = JSON.stringify(body);
    }
    const response = await fetch(url, options);
    if (response.status === 401) {
        // reset state
        localStorage.removeItem('token')
        // redirect to login
        router.push('/login')
        throw new Error('Unauthorized. Redirecting to login page.');
    }
    return response.json();
  } catch (error) {
    throw error;
  }
}

export function get(url, token = null, headers = {}) {
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  return fetchData(url, "GET", null, headers);
}

export function post(url, body, token, headers = {}) {
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  return fetchData(url, "POST", body, headers);
}

export function put(url, body, token = null, headers = {}) {
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  return fetchData(url, "PUT", body, headers);
}

export function del(url, token = null, headers = {}) {
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  return fetchData(url, "DELETE", null, headers);
}