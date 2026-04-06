const roleText = {
  VICTIM_SURVIVOR: "Track support requests, view progress, and reach providers safely.",
  COUNSELLOR: "Review assigned cases, document counselling notes, and guide next steps.",
  LEGAL_ADVISOR: "Provide legal rights guidance and coordinate follow-up actions.",
  ADMIN: "Manage users, content, and platform operations from one secure dashboard."
};

export default function HomePage({
  user,
  loading,
  resources,
  supportRequests,
  adminUsers,
  adminOverview,
  onNavigate,
  onRoleUpdate
}) {
  return (
    <section className="page-grid">
      <article className="spotlight-card">
        <div className="spotlight-copy">
          <span className="eyebrow">Support portal</span>
          <h2>SafeHaven helps people reach safety, guidance, and coordinated care with less friction.</h2>
          <p className="panel-copy">
            The platform combines legal rights information, health-risk education, provider access,
            and secure case management in one responsive interface.
          </p>
          <div className="hero-actions">
            <button type="button" className="primary-button" onClick={() => onNavigate("support")}>
              Open support services
            </button>
            <button type="button" className="secondary-button" onClick={() => onNavigate("resources")}>
              Browse resources
            </button>
          </div>
        </div>

        <div className="spotlight-side">
          <div className="metric-card">
            <span>Resource records</span>
            <strong>{resources.length}</strong>
          </div>
          <div className="metric-card">
            <span>{user ? "Assigned or submitted cases" : "Support pathways"}</span>
            <strong>{user ? supportRequests.length : 4}</strong>
          </div>
        </div>
      </article>

      <article className="panel">
        <div className="panel-heading">
          <span className="eyebrow">{user ? "Your role" : "Guest access"}</span>
          <h3>{user ? `Signed in as ${user.name}` : "Explore the portal before signing in"}</h3>
        </div>
        <p className="panel-copy">
          {user
            ? roleText[user.role]
            : "Guests can review the public resource library, provider directory, and help form. Sign in to manage support requests or admin content."}
        </p>
        {user && (
          <div className="pill-row">
            <span className="status-pill accent">{user.role.replaceAll("_", " ")}</span>
            <span className="status-pill">Authenticated session</span>
          </div>
        )}
      </article>

      <article className="panel">
        <div className="panel-heading">
          <span className="eyebrow">Recent activity</span>
          <h3>{user ? "Latest support request updates" : "Portal capabilities"}</h3>
        </div>
        {loading ? (
          <p className="muted-text">Loading current information...</p>
        ) : user ? (
          <div className="request-list">
            {supportRequests.slice(0, 4).map((request) => (
              <div key={request.id} className="request-card compact">
                <div className="request-header">
                  <strong>Request #{request.id}</strong>
                  <span className="status-pill">{request.status.replaceAll("_", " ")}</span>
                </div>
                <p>{request.message}</p>
                <small>{request.progressSummary || "Awaiting updates"}</small>
              </div>
            ))}
            {!supportRequests.length && <p className="muted-text">No requests linked to this account yet.</p>}
          </div>
        ) : (
          <div className="bullet-grid">
            <div className="bullet-card">
              <strong>Legal rights</strong>
              <p>Protection order guidance, evidence collection, and next steps.</p>
            </div>
            <div className="bullet-card">
              <strong>Health and trauma</strong>
              <p>Clear information on physical and emotional health risks.</p>
            </div>
            <div className="bullet-card">
              <strong>Support coordination</strong>
              <p>Shared tracking across counselling, legal, and admin teams.</p>
            </div>
          </div>
        )}
      </article>

      {user?.role === "ADMIN" && adminOverview && (
        <>
          <article className="panel">
            <div className="panel-heading">
              <span className="eyebrow">Admin overview</span>
              <h3>Operational snapshot</h3>
            </div>
            <div className="stats-grid">
              <div className="stat-box">
                <span>Total users</span>
                <strong>{adminOverview.totalUsers}</strong>
              </div>
              <div className="stat-box">
                <span>Total resources</span>
                <strong>{adminOverview.totalResources}</strong>
              </div>
              <div className="stat-box">
                <span>Total cases</span>
                <strong>{adminOverview.totalSupportRequests}</strong>
              </div>
            </div>
            <div className="inline-groups">
              {Object.entries(adminOverview.supportStatusCounts).map(([status, count]) => (
                <span key={status} className="status-pill">
                  {status.replaceAll("_", " ")}: {count}
                </span>
              ))}
            </div>
          </article>

          <article className="panel">
            <div className="panel-heading">
              <span className="eyebrow">User access</span>
              <h3>Manage roles</h3>
            </div>
            <div className="user-table">
              {adminUsers.map((account) => (
                <div key={account.id} className="table-row">
                  <div>
                    <strong>{account.name}</strong>
                    <p>{account.email}</p>
                  </div>
                  <label className="inline-field">
                    <span className="sr-only">Role</span>
                    <select value={account.role} onChange={(event) => onRoleUpdate(account.id, event.target.value)}>
                      <option value="VICTIM_SURVIVOR">Victim / Survivor</option>
                      <option value="COUNSELLOR">Counsellor</option>
                      <option value="LEGAL_ADVISOR">Legal Advisor</option>
                      <option value="ADMIN">Admin</option>
                    </select>
                  </label>
                </div>
              ))}
            </div>
          </article>
        </>
      )}
    </section>
  );
}
