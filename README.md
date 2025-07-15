# Portfolio Backend (Spring Boot)

A comprehensive Spring Boot backend service for a personal portfolio website with advanced features including authentication, project management, contact handling, skills showcase, and real-time status updates.

## 🚀 Features

### 🔐 Authentication & Authorization
- **Google OAuth2 Integration** - Seamless login with Google accounts
- **JWT Authentication** - Secure token-based authentication
- **Role-based Access Control** - USER and ADMIN roles with granular permissions
- **Email Verification** - Automated email verification system
- **User Profile Management** - Complete user profile with personal information

### 📁 Project Management
- **CRUD Operations** - Create, read, update, delete projects
- **Project Showcase** - Public API for displaying portfolio projects
- **Rich Project Data** - Support for images, descriptions, technologies, dates, and URLs
- **Admin Controls** - Admin-only project creation and management

### 📞 Contact Management
- **Contact Form API** - Handle contact form submissions from frontend
- **Message Storage** - Persist contact messages in MongoDB
- **Contact Statistics** - Dashboard analytics for contact activity
- **Email Notifications** - Automated email alerts for new messages
- **Message Status Tracking** - Mark messages as read/unread

### 🛠️ Skills Management
- **Skills CRUD** - Complete skills management system
- **Skill Categories** - Organize skills by categories (Frontend, Backend, etc.)
- **Proficiency Levels** - BEGINNER, INTERMEDIATE, ADVANCED, EXPERT levels
- **Featured Skills** - Highlight key skills on frontend
- **Search & Filter** - Search skills by name and filter by category
- **Experience Tracking** - Years of experience per skill

### 📍 Live Status Service
- **Real-time Status Display** - Show current activity/mood on homepage
- **Smart Expiration** - Auto-clear status after custom time periods
- **Status History** - Track all previous status updates
- **Public Status API** - Public endpoint for frontend display
- **Admin Status Management** - Complete dashboard for status control
- **Flexible Expiration Options** - Minutes, end of day, end of week, or never

### 👤 User Profile Features
- **Extended Profile Information** - Title, description, phone, location
- **Social Media Links** - Multiple social platform links
- **Profile & Hero Images** - Support for profile and banner images
- **Personal Information Updates** - Admin can update all profile details

## 🛠️ Technology Stack

### Backend Framework
- **Spring Boot 3.x** - Modern Java framework
- **Spring Security** - Authentication and authorization
- **Spring Data MongoDB** - Database integration
- **Spring Web** - REST API development
- **Spring Validation** - Input validation

### Database
- **MongoDB** - NoSQL document database
- **Spring Data MongoDB** - Object-document mapping

### Security & Authentication
- **Google OAuth2** - Third-party authentication
- **JWT (JSON Web Tokens)** - Stateless authentication
- **BCrypt** - Password hashing
- **CORS Configuration** - Cross-origin resource sharing

### Communication
- **JavaMail** - Email service integration
- **HTML Email Templates** - Rich email formatting

### Development Tools
- **Maven** - Dependency management
- **Lombok** - Reduce boilerplate code
- **Jackson** - JSON serialization/deserialization



**Built with ❤️ using Spring Boot**