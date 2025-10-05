let token = "";

function showPopup(message, success = true) {
  const popup = document.getElementById("popup");
  popup.textContent = message;
  popup.style.backgroundColor = success ? "#28a745" : "#dc3545"; // green or red
  popup.classList.add("show");

  setTimeout(() => {
    popup.classList.remove("show");
  }, 3000);
}

function login() {
  const username = document.getElementById("loginUsername").value;
  const password = document.getElementById("loginPassword").value;

  fetch("http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ username, password })
  })
    .then(res => {
      if (!res.ok) throw new Error("❌ Invalid username or password.");
      return res.json();
    })
    .then(data => {
      token = data.token;
      localStorage.setItem("token", token);
      showPopup("✅ Login successful!", true);
      setTimeout(() => {
        window.location.href = "team.html";
      }, 1000);
    })
    .catch(err => {
      console.error(err);
      showPopup(err.message || "❌ Login failed.", false);
    });
  }

function register() {
  const username = document.getElementById("registerUsername").value;
  const password = document.getElementById("registerPassword").value;

  if (!username || !password) {
    showPopup("❌ Username and password are required.", false);
    return;
  }

  fetch("http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/auth/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
  })
    .then(res => {
      if (res.status === 409) {
        return res.text().then(text => {
          throw new Error(text || " ❌ Username already taken.");
        });
      } else if (!res.ok) {
        return res.text().then(text => {
          throw new Error(text || " ❌ Registration failed.");
        });
      }

      return res.text(); // Successful plain text: "User registered successfully"
    })
    .then(message => {
      // ✅ Check message to be sure
      if (message.toLowerCase().includes("success")) {
        showPopup("✅ " + message, true);
        setTimeout(() => {
          loginAfterRegister(username, password);
        }, 1000);
      } else {
        showPopup("❌ " + message, false); // fallback
      }
    })
    .catch(err => {
      console.error(err);
      showPopup(err.message || "❌ Registration error occurred.", false);
    });
}
  



function loginAfterRegister(username, password) {
  fetch("http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
  })
    .then(res => {
      if (!res.ok) throw new Error("Login after registration failed");
      return res.json();
    })
    .then(data => {
      token = data.token;
      localStorage.setItem("token", token);
      showPopup("✅ Login successful!", true);
      setTimeout(() => {
        window.location.href = "team.html";
      }, 1000);
    })
    .catch(err => {
      console.error(err);
      showPopup("❌ Login failed after registration.", false);
    });
}




// Animate panels after delay
window.addEventListener("load", () => {
  setTimeout(() => {
    document.querySelectorAll(".panel").forEach(panel => {
      if (!panel.classList.contains("welcome-panel")) {
        panel.classList.add("show");
      }
    });
  }, 1500);
});