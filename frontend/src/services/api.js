const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";
const SESSION_KEY = "safehaven-session";

function getSession() {
  const raw = localStorage.getItem(SESSION_KEY);
  return raw ? JSON.parse(raw) : null;
}

function saveSession(session) {
  localStorage.setItem(SESSION_KEY, JSON.stringify(session));
}

function clearSession() {
  localStorage.removeItem(SESSION_KEY);
}

async function handleResponse(response) {
  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(data.message || "Something went wrong");
  }

  return data;
}

async function apiFetch(path, options = {}) {
  const session = getSession();
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(session?.token ? { Authorization: `Bearer ${session.token}` } : {}),
      ...(options.headers || {})
    },
    ...options
  });

  return handleResponse(response);
}

export { API_BASE_URL, apiFetch, clearSession, getSession, saveSession };
