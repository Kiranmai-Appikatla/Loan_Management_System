export default function ResourcesPage({
  user,
  loading,
  resources,
  resourceForm,
  resourceEditingId,
  setResourceForm,
  onSaveResource,
  onEditResource,
  onDeleteResource,
  onCancelEdit
}) {
  function handleChange(event) {
    const { name, value } = event.target;
    setResourceForm((current) => ({ ...current, [name]: value }));
  }

  function handleSubmit(event) {
    event.preventDefault();
    onSaveResource(resourceForm);
  }

  return (
    <section className="page-grid">
      <article className="panel page-lead">
        <span className="eyebrow">Resource center</span>
        <h2>Practical information on legal rights, health risks, and safety planning.</h2>
        <p className="panel-copy">
          Each resource is designed to be direct, readable, and useful under stress.
        </p>
      </article>

      {user?.role === "ADMIN" && (
        <article className="panel">
          <div className="panel-heading">
            <span className="eyebrow">Admin tools</span>
            <h3>{resourceEditingId ? "Edit resource" : "Add a new resource"}</h3>
          </div>
          <form className="stack-form" onSubmit={handleSubmit}>
            <label>
              Title
              <input name="title" value={resourceForm.title} onChange={handleChange} required />
            </label>
            <label>
              Type
              <select name="type" value={resourceForm.type} onChange={handleChange}>
                <option value="LEGAL_RIGHTS">Legal Rights</option>
                <option value="HEALTH_RISKS">Health Risks</option>
                <option value="SAFETY">Safety Planning</option>
                <option value="WELLNESS">Wellness</option>
              </select>
            </label>
            <label>
              Contact link
              <input name="contactLink" value={resourceForm.contactLink} onChange={handleChange} />
            </label>
            <label>
              Description
              <textarea name="description" rows="5" value={resourceForm.description} onChange={handleChange} required />
            </label>
            <div className="hero-actions">
              <button type="submit" className="primary-button">
                {resourceEditingId ? "Save changes" : "Create resource"}
              </button>
              {resourceEditingId && (
                <button type="button" className="secondary-button" onClick={onCancelEdit}>
                  Cancel
                </button>
              )}
            </div>
          </form>
        </article>
      )}

      <article className="resource-grid full-width">
        {loading ? (
          <p className="muted-text">Loading resources...</p>
        ) : (
          resources.map((resource) => (
            <div key={resource.id} className="resource-card">
              <div className="request-header">
                <span className="status-pill accent">{resource.type.replaceAll("_", " ")}</span>
                {user?.role === "ADMIN" && (
                  <div className="table-actions">
                    <button type="button" className="icon-button" onClick={() => onEditResource(resource)}>
                      Edit
                    </button>
                    <button type="button" className="icon-button danger" onClick={() => onDeleteResource(resource.id)}>
                      Delete
                    </button>
                  </div>
                )}
              </div>
              <h3>{resource.title}</h3>
              <p>{resource.description}</p>
              {resource.contactLink && (
                <a className="text-link" href={resource.contactLink} target="_blank" rel="noreferrer">
                  Open reference
                </a>
              )}
            </div>
          ))
        )}
      </article>
    </section>
  );
}
