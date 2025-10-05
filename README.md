# FileShare

A **cloud-based file sharing web application** that allows users and teams to securely upload, download, and manage files in the cloud.
the **main purpose** of creating this project is to Provision VPC/Network, public web servers, private app servers, and managed database.

---

## 🌟 Features

- **User Authentication**  
  Sign up, log in, and manage accounts with JWT-based authentication.

- **Team Management**  
  - Create a team and become the admin.  
  - Join an existing team using a team code.  
  - View all team members and their roles.

- **File Management**  
  - Upload files with a configurable download limit.  
  - Download files securely.  
  - Delete files you own or have permission for.  
  - QR code generation for easy file downloads.

- **Frontend**  
  - Modern, responsive UI with HTML, CSS, and JavaScript.  
  - Dashboard for team members with file management tools.  
  - Animated UI elements for a polished user experience.

---

## 🛠️ Technology Stack

| Layer          | Technology           |
|----------------|--------------------|
| Backend        | Java Spring Boot     |
| Database       | MySQL               |
| Frontend       | HTML, CSS, JS       |
| Hosting        | AWS EC2             |
| Authentication | JWT (JSON Web Tokens)|
| Version Control| Git & GitHub        |

---

# Cloud Deployment (AWS)

The application is deployed on AWS with a secure and scalable network architecture using a custom VPC.

## VPC & Subnets

VPC: A dedicated Virtual Private Cloud (VPC) hosts all application resources securely.

**Subnets:**

**Public Subnet:**

Hosts the application server (Spring Boot backend + frontend).

Assigned a public IP for internet access.

**Private Subnet:**

Hosts the MySQL database server.

No direct internet access for enhanced security.

## Secure Communication

The application server in the public subnet communicates with the MySQL server in the private subnet securely.

**Security group rules allow:**

Inbound MySQL traffic (port 3306) only from the public instance IP of the application server.

All other traffic to the private subnet is blocked from the internet.

## This ensures that:

The database is not exposed publicly.

Only the application server can access the database, maintaining secure separation.