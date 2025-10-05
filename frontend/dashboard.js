let token = localStorage.getItem("token");

if (!token) {
  window.location.href = "login.html";
}

function uploadFile() {
  const file = document.getElementById("fileInput").files[0];
  const formData = new FormData();
  formData.append("file", file);

  fetch("http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/file/upload", {
    method: "POST",
    headers: {
      "Authorization": "Bearer " + token,
    },
    body: formData,
  })
    .then((res) => res.json())
    .then((data) => {
      document.getElementById("uploadResult").innerHTML = `
        ✅ File uploaded!<br/>
        <b>ID:</b> ${data.fileId}<br/>
        <b>Download:</b> <a href="${data.downloadUrl}" target="_blank">Click here</a><br/>
        <img src="${data.qrCodeImage}" width="100">
      `;
    })
    .catch((err) => {
      console.error(err);
      alert("Upload failed!");
    });
}

function deleteFile() {
  const fileId = document.getElementById("fileIdToDelete").value;
  fetch(`http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/file/delete/${fileId}`, {
    method: "DELETE",
    headers: {
      "Authorization": "Bearer " + token,
    },
  })
    .then((res) => {
      if (res.ok) {
        alert("✅ File deleted!");
      } else {
        alert("❌ Delete failed!");
      }
    })
    .catch((err) => {
      console.error(err);
      alert("Error deleting file.");
    });
}

function logout() {
  localStorage.removeItem("token");
  window.location.href = "login.html";
}

// Initial load
loadTeamMembers();
loadTeamName();

async function loadTeamInfo() {
  const token = localStorage.getItem("token");
  try {
    const response = await fetch("http://ec2-54-147-234-251.compute-1.amazonaws.com:8080/team/info", {
      method: "GET",
      headers: {
        "Authorization": "Bearer " + token
      }
    });
    if (!response.ok) {
      throw new Error("Failed to fetch team info");
    }
    const data = await response.json();
    // Display team name
    // Display team members
    const memberList = document.getElementById("member-list");
    memberList.innerHTML = "";
    data.members.forEach(member => {
      const li = document.createElement("li");
      li.style.marginBottom = "6px";
      li.innerHTML = `<strong>${member.username}</strong>` + (member.role === "ADMIN" ? " <span style='color: green;'>(Admin)</span>" : "");
      memberList.appendChild(li);
    });
  } catch (err) {
    console.error(err);
    document.getElementById("team-name").innerText = "Error loading team";
  }
}

window.onload = () => {
  loadTeamInfo();
};