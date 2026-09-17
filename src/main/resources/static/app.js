const form = document.getElementById("calc-form");
const resultBox = document.getElementById("result");
const paymentAmountEl = document.getElementById("payment-amount");
const affordabilityEl = document.getElementById("affordability");
const applicationsBody = document.getElementById("applications-body");

const currency = new Intl.NumberFormat("en-US", { style: "currency", currency: "USD", maximumFractionDigits: 0 });

function formToPayload(formData) {
  return {
    applicantName: formData.get("applicantName"),
    annualIncome: Number(formData.get("annualIncome")),
    homePrice: Number(formData.get("homePrice")),
    downPayment: Number(formData.get("downPayment")),
    annualInterestRate: Number(formData.get("annualInterestRate")),
    loanTermYears: Number(formData.get("loanTermYears")),
  };
}

async function previewPayment(payload) {
  const res = await fetch("/api/calculate", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error("Calculation failed");
  return res.json();
}

async function submitApplication(payload) {
  const res = await fetch("/api/applications", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error("Submission failed");
  return res.json();
}

async function updateStatus(id, status) {
  await fetch(`/api/applications/${id}/status?status=${status}`, { method: "PATCH" });
}

async function deleteApplication(id) {
  await fetch(`/api/applications/${id}`, { method: "DELETE" });
}

function showResult({ monthlyPayment, affordable }) {
  resultBox.hidden = false;
  paymentAmountEl.textContent = currency.format(monthlyPayment);
  affordabilityEl.textContent = affordable
    ? "Within the typical 28%-of-income guideline."
    : "Above the typical 28%-of-income guideline — lenders may flag this.";
  affordabilityEl.className = "affordability " + (affordable ? "ok" : "tight");
}

function renderApplications(applications) {
  applicationsBody.innerHTML = "";

  if (applications.length === 0) {
    applicationsBody.innerHTML = '<tr class="empty-row"><td colspan="5">No applications yet.</td></tr>';
    return;
  }

  for (const app of applications) {
    const row = document.createElement("tr");

    const statusOptions = ["SUBMITTED", "UNDER_REVIEW", "APPROVED", "DENIED"]
      .map((s) => `<option value="${s}" ${s === app.status ? "selected" : ""}>${s.replace("_", " ")}</option>`)
      .join("");

    row.innerHTML = `
      <td>${app.applicantName}</td>
      <td>${currency.format(app.homePrice)}</td>
      <td>${currency.format(app.monthlyPayment)}</td>
      <td><select class="status-select" data-id="${app.id}">${statusOptions}</select></td>
      <td><button class="remove-btn" data-id="${app.id}">Remove</button></td>
    `;
    applicationsBody.appendChild(row);
  }

  applicationsBody.querySelectorAll(".status-select").forEach((select) => {
    select.addEventListener("change", async (e) => {
      await updateStatus(e.target.dataset.id, e.target.value);
    });
  });

  applicationsBody.querySelectorAll(".remove-btn").forEach((btn) => {
    btn.addEventListener("click", async (e) => {
      await deleteApplication(e.target.dataset.id);
      await loadApplications();
    });
  });
}

async function loadApplications() {
  const res = await fetch("/api/applications");
  renderApplications(await res.json());
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  const action = event.submitter?.getAttribute("formaction") || "preview";
  const payload = formToPayload(new FormData(form));

  try {
    if (action === "preview") {
      showResult(await previewPayment(payload));
    } else {
      // Preview first so the affordability guideline reflects this exact
      // payload, then persist it as a tracked application.
      const preview = await previewPayment(payload);
      showResult(preview);
      await submitApplication(payload);
      form.reset();
      await loadApplications();
    }
  } catch (err) {
    alert("Something went wrong — check the console for details.");
    console.error(err);
  }
});

loadApplications();
