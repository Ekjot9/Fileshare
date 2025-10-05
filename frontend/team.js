function showJoinForm() {
  document.getElementById("joinForm").classList.remove("hidden");
  document.getElementById("createForm").classList.add("hidden");
  document.getElementById("adminForm").classList.add("hidden"); // <-- hide admin

  document.getElementById("joinTab").classList.add("active");
  document.getElementById("createTab").classList.remove("active");
  document.getElementById("adminTab").classList.remove("active");
}

function showCreateForm() {
  document.getElementById("createForm").classList.remove("hidden");
  document.getElementById("joinForm").classList.add("hidden");
  document.getElementById("adminForm").classList.add("hidden"); // <-- hide admin

  document.getElementById("createTab").classList.add("active");
  document.getElementById("joinTab").classList.remove("active");
  document.getElementById("adminTab").classList.remove("active");
}

function showAdminJoinForm() {
  document.getElementById("adminForm").classList.remove("hidden");
  document.getElementById("joinForm").classList.add("hidden");
  document.getElementById("createForm").classList.add("hidden");

  document.getElementById("adminTab").classList.add("active");
  document.getElementById("joinTab").classList.remove("active");
  document.getElementById("createTab").classList.remove("active");
}



function showPopup(message, success = true) {
  const popup = document.getElementById("popup");
  popup.textContent = message;
  popup.style.backgroundColor = success ? "#28a745" : "#dc3545";
  popup.classList.add("show");

  setTimeout(() => popup.classList.remove("show"), 3000);
}


function joinTeam() {
  const teamName = document.getElementById("joinTeamName").value;
  const password = document.getElementById("joinTeamPassword").value;
  const token = localStorage.getItem("token");

  fetch("http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/team/join", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify({ teamName, password })
  })
    .then(res => res.text())
    .then(msg => {
      showPopup(msg, msg.startsWith("✅"));
      if (msg.startsWith("✅")) {
        setTimeout(() => window.location.href = "dashboard.html", 1500);
      }
    })
    .catch(err => {
      console.error(err);
      showPopup("❌ Failed to join team", false);
    });
}

function createTeam() {
  const teamName = document.getElementById("createTeamName").value;
  const password = document.getElementById("createTeamPassword").value;
  const token = localStorage.getItem("token");

  if (!teamName || !password) {
    showPopup("❌ Please enter team name and password.", false);
    return;
  }

  fetch("http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/team/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify({ teamName, password })
  })
    .then(res => {
      if (res.status === 409) {
        return res.text().then(text => {
          throw new Error(text || "❌ Team name already exists.");
        });
      } else if (!res.ok) {
        return res.text().then(text => {
          throw new Error(text || "❌ Failed to create team.");
        });
      }

      return res.text();
    })
    .then(message => {
      if (message.toLowerCase().includes("success")) {
        showPopup("✅ " + message, true);
        setTimeout(() => {
          window.location.href = "dashboard.html";
        }, 1000);
      } else {
        showPopup("❌ " + message, false); // fallback
      }
    })
    .catch(err => {
      console.error(err);
      showPopup(err.message || "❌ Team creation error occurred.", false);
    });
}


function adminJoinTeam() {
  const teamName = document.getElementById("adminTeamName").value;
  const password = document.getElementById("adminTeamPassword").value;
  const token = localStorage.getItem("token");

  fetch("http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/team/admin/join", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify({ teamName, password })
  })
    .then(res => res.text())
    .then(msg => {
      showPopup(msg, msg.startsWith("✅"));
      if (msg.startsWith("✅")) {
        setTimeout(() => window.location.href = "dashboard.html", 1500);
      }
    })
    .catch(err => {
      console.error(err);
      showPopup("❌ Failed to join as admin", false);
    });
}
