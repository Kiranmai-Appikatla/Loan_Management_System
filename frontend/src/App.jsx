import { useEffect, useMemo, useState } from "react";
import AuthPage from "./pages/AuthPage";
import ContactPage from "./pages/ContactPage";
import HomePage from "./pages/HomePage";
import ResourcesPage from "./pages/ResourcesPage";
import SupportServicesPage from "./pages/SupportServicesPage";
import { apiFetch, clearSession, getSession, saveSession } from "./services/api";

const availablePages = ["home", "resources", "support", "contact", "login", "register"];

function emptyResourceForm() {
  return {
    title: "",
    description: "",
    type: "LEGAL_RIGHTS",
    contactLink: ""
  };
}

export default function App() {
  const [activePage, setActivePage] = useState("home");
  const [session, setSession] = useState(() => getSession());
  const [resources, setResources] = useState([]);
  const [providers, setProviders] = useState([]);
  const [supportRequests, setSupportRequests] = useState([]);
  const [adminUsers, setAdminUsers] = useState([]);
  const [adminOverview, setAdminOverview] = useState(null);
  const [resourceForm, setResourceForm] = useState(emptyResourceForm());
  const [resourceEditingId, setResourceEditingId] = useState(null);
  const [feedback, setFeedback] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const user = session?.user || null;

  const navItems = useMemo(() => {
    const base = [
      { key: "home", label: "Home" },
      { key: "resources", label: "Resources" },
      { key: "support", label: "Support Services" },
      { key: "contact", label: "Contact" }
    ];

    if (!user) {
      return [...base, { key: "login", label: "Login" }, { key: "register", label: "Register" }];
    }

    return base;
  }, [user]);

  useEffect(() => {
    loadPortalData();
  }, [session?.token]);

  async function loadPortalData() {
    setLoading(true);
    setError("");

    try {
      const [resourceData, providerData] = await Promise.all([
        apiFetch(session?.token ? "/resources" : "/resources/public"),
        apiFetch("/users/providers")
      ]);

      setResources(resourceData);
      setProviders(providerData);

      if (session?.token) {
        const supportData = await apiFetch("/support-requests");
        setSupportRequests(supportData);

        if (user?.role === "ADMIN") {
          const [usersData, overviewData] = await Promise.all([
            apiFetch("/admin/users"),
            apiFetch("/admin/overview")
          ]);
          setAdminUsers(usersData);
          setAdminOverview(overviewData);
        } else {
          setAdminUsers([]);
          setAdminOverview(null);
        }
      } else {
        setSupportRequests([]);
        setAdminUsers([]);
        setAdminOverview(null);
      }
    } catch (loadError) {
      setError(loadError.message);
      if (session?.token) {
        clearSession();
        setSession(null);
      }
    } finally {
      setLoading(false);
    }
  }

  async function handleAuthentication(path, payload) {
    setError("");
    setFeedback("");

    try {
      const response = await apiFetch(path, {
        method: "POST",
        body: JSON.stringify(payload)
      });
      const nextSession = { token: response.token, user: response.user };
      saveSession(nextSession);
      setSession(nextSession);
      setActivePage("home");
      setFeedback(response.message);
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  function handleLogout() {
    clearSession();
    setSession(null);
    setActivePage("home");
    setFeedback("You have been signed out.");
    setError("");
  }

  async function handleResourceSave(payload) {
    try {
      await apiFetch(resourceEditingId ? `/resources/${resourceEditingId}` : "/resources", {
        method: resourceEditingId ? "PUT" : "POST",
        body: JSON.stringify(payload)
      });
      setResourceEditingId(null);
      setResourceForm(emptyResourceForm());
      setFeedback(resourceEditingId ? "Resource updated." : "Resource created.");
      await loadPortalData();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handleResourceDelete(resourceId) {
    try {
      await apiFetch(`/resources/${resourceId}`, { method: "DELETE" });
      setFeedback("Resource deleted.");
      await loadPortalData();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handleSupportRequestCreate(payload) {
    try {
      await apiFetch("/support-requests", {
        method: "POST",
        body: JSON.stringify(payload)
      });
      setFeedback("Support request submitted.");
      await loadPortalData();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handleSupportStatusUpdate(id, payload) {
    try {
      await apiFetch(`/support-requests/${id}/status`, {
        method: "PATCH",
        body: JSON.stringify(payload)
      });
      setFeedback("Support request updated.");
      await loadPortalData();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handleLegalAdvice(id, legalAdvice) {
    if (!legalAdvice) {
      setError("Legal advice cannot be empty.");
      return;
    }

    try {
      await apiFetch(`/support-requests/${id}/legal-advice`, {
        method: "POST",
        body: JSON.stringify({ legalAdvice })
      });
      setFeedback("Legal advice saved.");
      await loadPortalData();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handleCounsellingNote(id, notes) {
    if (!notes) {
      setError("Counselling note cannot be empty.");
      return;
    }

    try {
      await apiFetch(`/support-requests/${id}/counselling-notes`, {
        method: "POST",
        body: JSON.stringify({ notes })
      });
      setFeedback("Counselling note saved.");
      await loadPortalData();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handleContact(payload) {
    try {
      const response = await apiFetch("/contact", {
        method: "POST",
        body: JSON.stringify(payload)
      });
      setFeedback(response.message);
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  async function handleRoleUpdate(userId, role) {
    try {
      await apiFetch(`/admin/users/${userId}/role`, {
        method: "PATCH",
        body: JSON.stringify(role)
      });
      setFeedback("User role updated.");
      await loadPortalData();
    } catch (requestError) {
      setError(requestError.message);
    }
  }

  function editResource(resource) {
    setResourceEditingId(resource.id);
    setResourceForm({
      title: resource.title,
      description: resource.description,
      type: resource.type,
      contactLink: resource.contactLink || ""
    });
    setActivePage("resources");
  }

  function cancelResourceEdit() {
    setResourceEditingId(null);
    setResourceForm(emptyResourceForm());
  }

  const currentPage = availablePages.includes(activePage) ? activePage : "home";

  return (
    <div className="app-shell">
      <div className="page-backdrop" aria-hidden="true" />

      <header className="site-header">
        <div className="brand-block">
          <div className="brand-mark">S</div>
          <div>
            <p className="brand-tag">Safe, informed, connected</p>
            <h1>SafeHaven</h1>
          </div>
        </div>

        <nav className="site-nav" aria-label="Primary navigation">
          {navItems.map((item) => (
            <button
              key={item.key}
              type="button"
              className={item.key === currentPage ? "nav-link active" : "nav-link"}
              onClick={() => setActivePage(item.key)}
            >
              {item.label}
            </button>
          ))}
          {user && (
            <button type="button" className="nav-link utility" onClick={handleLogout}>
              Sign out
            </button>
          )}
        </nav>
      </header>

      <main className="app-main">
        {(feedback || error) && (
          <div className={error ? "banner error" : "banner success"} role="status">
            {error || feedback}
          </div>
        )}

        {currentPage === "home" && (
          <HomePage
            user={user}
            loading={loading}
            resources={resources}
            supportRequests={supportRequests}
            adminUsers={adminUsers}
            adminOverview={adminOverview}
            onNavigate={setActivePage}
            onRoleUpdate={handleRoleUpdate}
          />
        )}

        {currentPage === "resources" && (
          <ResourcesPage
            user={user}
            loading={loading}
            resources={resources}
            resourceForm={resourceForm}
            resourceEditingId={resourceEditingId}
            setResourceForm={setResourceForm}
            onSaveResource={handleResourceSave}
            onEditResource={editResource}
            onDeleteResource={handleResourceDelete}
            onCancelEdit={cancelResourceEdit}
          />
        )}

        {currentPage === "support" && (
          <SupportServicesPage
            user={user}
            loading={loading}
            providers={providers}
            supportRequests={supportRequests}
            onCreateSupportRequest={handleSupportRequestCreate}
            onStatusUpdate={handleSupportStatusUpdate}
            onAddLegalAdvice={handleLegalAdvice}
            onAddCounsellingNote={handleCounsellingNote}
            onNavigate={setActivePage}
          />
        )}

        {currentPage === "contact" && <ContactPage onSubmit={handleContact} />}

        {currentPage === "login" && (
          <AuthPage
            mode="login"
            onSubmit={(payload) => handleAuthentication("/auth/login", payload)}
            onSwitch={() => setActivePage("register")}
          />
        )}

        {currentPage === "register" && (
          <AuthPage
            mode="register"
            onSubmit={(payload) => handleAuthentication("/auth/register", payload)}
            onSwitch={() => setActivePage("login")}
          />
        )}
      </main>
    </div>
  );
}
