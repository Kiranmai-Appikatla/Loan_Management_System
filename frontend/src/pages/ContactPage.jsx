import { useState } from "react";

export default function ContactPage({ onSubmit }) {
  const [form, setForm] = useState({
    name: "",
    email: "",
    concernType: "Emergency safety planning",
    message: ""
  });

  function handleChange(event) {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  }

  function handleSubmit(event) {
    event.preventDefault();
    onSubmit(form);
    setForm({
      name: "",
      email: "",
      concernType: "Emergency safety planning",
      message: ""
    });
  }

  return (
    <section className="split-layout">
      <article className="panel">
        <div className="panel-heading">
          <span className="eyebrow">Need help?</span>
          <h2>Reach the SafeHaven team</h2>
        </div>
        <p className="panel-copy">
          This form is for non-immediate support and follow-up requests. If someone is in immediate
          danger, contact local emergency services or a local crisis line right away.
        </p>
        <div className="bullet-grid">
          <div className="bullet-card">
            <strong>Safety planning</strong>
            <p>Request help building an immediate and practical safety plan.</p>
          </div>
          <div className="bullet-card">
            <strong>Legal guidance</strong>
            <p>Ask where to begin with protection orders and documentation.</p>
          </div>
          <div className="bullet-card">
            <strong>Wellbeing support</strong>
            <p>Get directed toward counselling and emotional support resources.</p>
          </div>
        </div>
      </article>

      <article className="form-panel">
        <div className="panel-heading">
          <span className="eyebrow">Contact form</span>
          <h3>Send a message</h3>
        </div>

        <form className="stack-form" onSubmit={handleSubmit}>
          <label>
            Name
            <input name="name" value={form.name} onChange={handleChange} required />
          </label>

          <label>
            Email
            <input name="email" type="email" value={form.email} onChange={handleChange} required />
          </label>

          <label>
            Concern type
            <select name="concernType" value={form.concernType} onChange={handleChange}>
              <option>Emergency safety planning</option>
              <option>Legal rights information</option>
              <option>Counselling and wellbeing</option>
              <option>Platform support</option>
            </select>
          </label>

          <label>
            Message
            <textarea name="message" rows="6" value={form.message} onChange={handleChange} required />
          </label>

          <button type="submit" className="primary-button">
            Submit help request
          </button>
        </form>
      </article>
    </section>
  );
}
