import { useState } from "react";

const roleOptions = [
  { value: "VICTIM_SURVIVOR", label: "Victim / Survivor" },
  { value: "COUNSELLOR", label: "Counsellor" },
  { value: "LEGAL_ADVISOR", label: "Legal Advisor" },
  { value: "ADMIN", label: "Admin" }
];

export default function AuthPage({ mode, onSubmit, onSwitch }) {
  const isRegister = mode === "register";
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    role: "VICTIM_SURVIVOR"
  });

  function handleChange(event) {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  }

  function handleSubmit(event) {
    event.preventDefault();
    onSubmit(
      isRegister
        ? form
        : {
            email: form.email,
            password: form.password
          }
    );
  }

  return (
    <section className="split-layout">
      <article className="hero-panel">
        <span className="eyebrow">Secure support portal</span>
        <h2>Private access to trusted guidance, case updates, and survivor-centered coordination.</h2>
        <p>
          SafeHaven brings resources, counselling support, legal advice, and admin oversight into
          one calm and accessible web experience.
        </p>
        <div className="feature-grid">
          <div className="feature-card">
            <strong>Protected accounts</strong>
            <span>JWT-based sign-in keeps private workflows separated by role.</span>
          </div>
          <div className="feature-card">
            <strong>Case progress tracking</strong>
            <span>Follow updates, status changes, and provider notes in one place.</span>
          </div>
          <div className="feature-card">
            <strong>Resource-first design</strong>
            <span>Important safety and legal information stays easy to find and read.</span>
          </div>
        </div>
      </article>

      <article className="form-panel">
        <div className="panel-heading">
          <span className="eyebrow">{isRegister ? "Create account" : "Welcome back"}</span>
          <h3>{isRegister ? "Register for SafeHaven" : "Sign in to SafeHaven"}</h3>
        </div>

        <form className="stack-form" onSubmit={handleSubmit}>
          {isRegister && (
            <label>
              Full name
              <input name="name" value={form.name} onChange={handleChange} required />
            </label>
          )}

          <label>
            Email address
            <input name="email" type="email" value={form.email} onChange={handleChange} required />
          </label>

          <label>
            Password
            <input
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              minLength={8}
              required
            />
          </label>

          {isRegister && (
            <label>
              Role
              <select name="role" value={form.role} onChange={handleChange}>
                {roleOptions.map((role) => (
                  <option key={role.value} value={role.value}>
                    {role.label}
                  </option>
                ))}
              </select>
            </label>
          )}

          <button type="submit" className="primary-button">
            {isRegister ? "Create account" : "Sign in"}
          </button>
        </form>

        <button type="button" className="text-button" onClick={onSwitch}>
          {isRegister ? "Already have an account? Sign in" : "Need an account? Register"}
        </button>
      </article>
    </section>
  );
}
