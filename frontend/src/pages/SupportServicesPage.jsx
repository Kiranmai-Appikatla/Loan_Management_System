import { useState } from "react";

const initialRequestForm = {
  counsellorId: "",
  legalAdvisorId: "",
  message: ""
};

export default function SupportServicesPage({
  user,
  loading,
  providers,
  supportRequests,
  onCreateSupportRequest,
  onStatusUpdate,
  onAddLegalAdvice,
  onAddCounsellingNote,
  onNavigate
}) {
  const [requestForm, setRequestForm] = useState(initialRequestForm);
  const [drafts, setDrafts] = useState({});

  const counsellors = providers.filter((provider) => provider.role === "COUNSELLOR");
  const legalAdvisors = providers.filter((provider) => provider.role === "LEGAL_ADVISOR");

  function updateDraft(requestId, field, value) {
    setDrafts((current) => ({
      ...current,
      [requestId]: {
        ...(current[requestId] || {}),
        [field]: value
      }
    }));
  }

  function submitSupportRequest(event) {
    event.preventDefault();
    onCreateSupportRequest({
      counsellorId: Number(requestForm.counsellorId),
      legalAdvisorId: Number(requestForm.legalAdvisorId),
      message: requestForm.message
    });
    setRequestForm(initialRequestForm);
  }

  return (
    <section className="page-grid">
      <article className="panel page-lead">
        <span className="eyebrow">Support services</span>
        <h2>Bring counsellors, legal advisors, and survivors into a shared, secure workflow.</h2>
        <p className="panel-copy">
          SafeHaven keeps provider contacts visible and turns sensitive follow-up into a guided process.
        </p>
      </article>

      {!user && (
        <article className="panel">
          <div className="panel-heading">
            <span className="eyebrow">Sign-in required</span>
            <h3>Create and manage support requests after logging in</h3>
          </div>
          <p className="panel-copy">
            You can still browse the provider list and public resources without an account.
          </p>
          <button type="button" className="primary-button" onClick={() => onNavigate("login")}>
            Go to login
          </button>
        </article>
      )}

      <article className="panel">
        <div className="panel-heading">
          <span className="eyebrow">Provider directory</span>
          <h3>Available specialists</h3>
        </div>
        <div className="bullet-grid">
          {providers.map((provider) => (
            <div key={provider.id} className="bullet-card">
              <strong>{provider.name}</strong>
              <p>{provider.email}</p>
              <span className="status-pill">{provider.role.replaceAll("_", " ")}</span>
            </div>
          ))}
        </div>
      </article>

      {user?.role === "VICTIM_SURVIVOR" && (
        <article className="panel">
          <div className="panel-heading">
            <span className="eyebrow">New request</span>
            <h3>Ask for coordinated support</h3>
          </div>
          <form className="stack-form" onSubmit={submitSupportRequest}>
            <label>
              Counsellor
              <select
                value={requestForm.counsellorId}
                onChange={(event) => setRequestForm((current) => ({ ...current, counsellorId: event.target.value }))}
                required
              >
                <option value="">Select a counsellor</option>
                {counsellors.map((provider) => (
                  <option key={provider.id} value={provider.id}>
                    {provider.name}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Legal advisor
              <select
                value={requestForm.legalAdvisorId}
                onChange={(event) => setRequestForm((current) => ({ ...current, legalAdvisorId: event.target.value }))}
                required
              >
                <option value="">Select a legal advisor</option>
                {legalAdvisors.map((provider) => (
                  <option key={provider.id} value={provider.id}>
                    {provider.name}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Message
              <textarea
                rows="5"
                value={requestForm.message}
                onChange={(event) => setRequestForm((current) => ({ ...current, message: event.target.value }))}
                required
              />
            </label>

            <button type="submit" className="primary-button">
              Submit support request
            </button>
          </form>
        </article>
      )}

      <article className="full-width">
        <div className="panel-heading">
          <span className="eyebrow">{user ? "Active requests" : "Workflow preview"}</span>
          <h3>{user ? "Support requests, notes, and legal guidance" : "Support request details appear after sign-in"}</h3>
        </div>

        {loading ? (
          <p className="muted-text">Loading support activity...</p>
        ) : (
          <div className="request-list">
            {supportRequests.map((request) => {
              const draft = drafts[request.id] || {};
              const canUpdate = user && ["COUNSELLOR", "LEGAL_ADVISOR", "ADMIN"].includes(user.role);
              const canAddLegalAdvice = user && ["LEGAL_ADVISOR", "ADMIN"].includes(user.role);
              const canAddNotes = user && ["COUNSELLOR", "ADMIN"].includes(user.role);

              return (
                <article key={request.id} className="request-card">
                  <div className="request-header">
                    <div>
                      <h3>Request #{request.id}</h3>
                      <p>
                        Survivor: {request.user.name} | Counsellor: {request.counsellor?.name || "Unassigned"} |
                        Legal advisor: {request.legalAdvisor?.name || "Unassigned"}
                      </p>
                    </div>
                    <span className="status-pill accent">{request.status.replaceAll("_", " ")}</span>
                  </div>

                  <p>{request.message}</p>
                  <small className="muted-text">{request.progressSummary || "No progress summary yet."}</small>

                  {canUpdate && (
                    <div className="inline-form">
                      <select
                        value={draft.status || request.status}
                        onChange={(event) => updateDraft(request.id, "status", event.target.value)}
                      >
                        <option value="OPEN">Open</option>
                        <option value="IN_PROGRESS">In Progress</option>
                        <option value="WAITING_FOR_USER">Waiting for User</option>
                        <option value="RESOLVED">Resolved</option>
                      </select>
                      <input
                        type="text"
                        placeholder="Progress summary"
                        value={draft.progressSummary || ""}
                        onChange={(event) => updateDraft(request.id, "progressSummary", event.target.value)}
                      />
                      <button
                        type="button"
                        className="secondary-button"
                        onClick={() =>
                          onStatusUpdate(request.id, {
                            status: draft.status || request.status,
                            progressSummary: draft.progressSummary || request.progressSummary
                          })
                        }
                      >
                        Update status
                      </button>
                    </div>
                  )}

                  <div className="support-columns">
                    <div className="panel inset">
                      <h4>Legal advice</h4>
                      {request.legalAdvice.map((entry) => (
                        <div key={entry.id} className="timeline-entry">
                          <strong>{entry.advisorName}</strong>
                          <p>{entry.legalAdvice}</p>
                        </div>
                      ))}
                      {!request.legalAdvice.length && <p className="muted-text">No legal advice entries yet.</p>}
                      {canAddLegalAdvice && (
                        <div className="stack-form">
                          <textarea
                            rows="3"
                            placeholder="Add legal guidance"
                            value={draft.legalAdvice || ""}
                            onChange={(event) => updateDraft(request.id, "legalAdvice", event.target.value)}
                          />
                          <button
                            type="button"
                            className="secondary-button"
                            onClick={() => onAddLegalAdvice(request.id, draft.legalAdvice || "")}
                          >
                            Save legal advice
                          </button>
                        </div>
                      )}
                    </div>

                    <div className="panel inset">
                      <h4>Counselling notes</h4>
                      {request.counsellingNotes.map((entry) => (
                        <div key={entry.id} className="timeline-entry">
                          <strong>{entry.counsellorName}</strong>
                          <p>{entry.notes}</p>
                        </div>
                      ))}
                      {!request.counsellingNotes.length && <p className="muted-text">No counselling notes yet.</p>}
                      {canAddNotes && (
                        <div className="stack-form">
                          <textarea
                            rows="3"
                            placeholder="Add counselling note"
                            value={draft.notes || ""}
                            onChange={(event) => updateDraft(request.id, "notes", event.target.value)}
                          />
                          <button
                            type="button"
                            className="secondary-button"
                            onClick={() => onAddCounsellingNote(request.id, draft.notes || "")}
                          >
                            Save counselling note
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                </article>
              );
            })}

            {!supportRequests.length && (
              <div className="panel">
                <p className="muted-text">
                  {user ? "No support requests are linked to this account yet." : "Sign in to view case activity."}
                </p>
              </div>
            )}
          </div>
        )}
      </article>
    </section>
  );
}
