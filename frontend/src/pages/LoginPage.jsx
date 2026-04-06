import { useState } from "react";
import AuthForm from "../components/AuthForm";
import { apiFetch } from "../services/api";

const initialState = {
  email: "",
  password: ""
};

export default function LoginPage({ onLogin, onSwitch }) {
  const [formData, setFormData] = useState(initialState);

  const handleChange = (event) => {
    setFormData((current) => ({
      ...current,
      [event.target.name]: event.target.value
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const user = await apiFetch("/auth/login", {
        method: "POST",
        body: JSON.stringify(formData)
      });

      localStorage.setItem("loggedInUser", JSON.stringify(user));
      alert("Login successful");
      onLogin(user);
    } catch (error) {
      alert(error.message);
    }
  };

  return (
    <AuthForm
      title="Login"
      description="Access your dashboard to manage loans, requests, and payments."
      fields={[
        { name: "email", label: "Email", type: "email", required: true, placeholder: "Enter your email" },
        { name: "password", label: "Password", type: "password", required: true, placeholder: "Enter your password" }
      ]}
      formData={formData}
      onChange={handleChange}
      onSubmit={handleSubmit}
      submitLabel="Login"
      footer={
        <>
          Don't have an account?{" "}
          <button type="button" className="link-btn" onClick={onSwitch}>
            Register here
          </button>
        </>
      }
    />
  );
}
