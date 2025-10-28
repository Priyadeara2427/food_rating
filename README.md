# 🍽️ Canteen Food & Rating System

A full-stack web application for managing canteens, dishes, and ratings built with Spring Boot, PostgreSQL, and Vue.js.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [API Endpoints](#api-endpoints)
- [Frontend Routes](#frontend-routes)
- [Database Schema](#database-schema)
- [Sample Data](#sample-data)
- [Usage Guide](#usage-guide)
- [Development](#development)

## ✨ Features

### 🏪 Canteen Management
- View all available canteens with details
- See canteen location and description
- Display dish count for each canteen
- Recently viewed canteens tracking

### 🍛 Dish Management
- Add new dishes to canteens
- Edit existing dish information
- Delete dishes
- Categorize dishes (Appetizer, Main Course, Dessert, etc.)
- Price management in Indian Rupees (₹)

### ⭐ Rating System
- Rate dishes from 1-5 stars
- Visual star rating display
- Real-time rating updates
- Session statistics tracking

### 🎯 User Experience
- Responsive design for mobile and desktop
- Session statistics (canteens viewed, dishes rated)
- Recently viewed items
- Intuitive navigation
- Form validation and error handling

## 🛠 Tech Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA (Hibernate)
- **Validation**: Jakarta Bean Validation
- **CORS**: Custom configuration
- **Build Tool**: Maven

### Frontend
- **Framework**: Vue.js 3
- **Router**: Vue Router
- **HTTP Client**: Fetch API
- **Storage**: LocalStorage & SessionStorage
- **Styling**: CSS3 with responsive design

## 📁 Project Structure

```
food-rating-app/
├── backend/ (Spring Boot)
│   ├── src/main/java/com/foodrating/
│   │   ├── entity/          # JPA Entities
│   │   ├── repository/      # Spring Data Repositories
│   │   ├── controller/      # REST Controllers
│   │   ├── config/          # Configuration classes
│   │   └── FoodRatingApplication.java
│   └── src/main/resources/
│       └── application.properties
│
└── frontend/ (Vue.js)
    ├── src/
    │   ├── views/           # Page components
    │   ├── router/          # Vue Router configuration
    │   └── main.js          # Vue app entry point
    └── public/
        └── index.html
```

## 🚀 Installation & Setup

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- PostgreSQL 12 or higher
- Maven 3.6+

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd food-rating-app/backend
   ```

2. **Database Configuration**
   ```bash
   # Create PostgreSQL database
   createdb food_rating_db
   ```

3. **Update application.properties**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/food_rating_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   Backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd food-rating-app/frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Run development server**
   ```bash
   npm run serve
   ```
   Frontend will start on `http://localhost:5173`

## 🔌 API Endpoints

### Canteens
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/canteens` | Get all canteens |
| GET | `/api/canteens/{id}` | Get canteen by ID |
| POST | `/api/canteens` | Create new canteen |
| PUT | `/api/canteens/{id}` | Update canteen |
| DELETE | `/api/canteens/{id}` | Delete canteen |

### Dishes
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/dishes` | Get all dishes |
| GET | `/api/dishes/{id}` | Get dish by ID |
| GET | `/api/dishes/canteen/{canteenId}` | Get dishes by canteen |
| POST | `/api/dishes` | Create new dish |
| PUT | `/api/dishes/{id}` | Update dish |
| DELETE | `/api/dishes/{id}` | Delete dish |

## 🎯 Frontend Routes

| Route | Component | Description |
|-------|-----------|-------------|
| `/` | `HomeView` | Homepage with canteen list |
| `/canteen/:id` | `CanteenView` | Canteen details and dishes |
| `/add-dish` | `AddDishView` | Add new dish form |
| `/edit-dish/:id` | `EditDishView` | Edit existing dish |

## 🗃 Database Schema

### Canteens Table
```sql
CREATE TABLE canteens (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    location VARCHAR(255),
    description TEXT
);
```

### Dishes Table
```sql
CREATE TABLE dishes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255),
    price DECIMAL(10,2),
    rating INTEGER,
    available_date DATE DEFAULT CURRENT_DATE,
    canteen_id BIGINT REFERENCES canteens(id)
);
```

## 📊 Sample Data

The application comes with pre-loaded sample data:

### Canteens
1. **Main Campus Canteen** - Building near Ladies Hostel
2. **Rishabs Canteen** - Near SSN Admin Block (Healthy food & juices)
3. **Snow Cubes Canteen** - Near SSN Admin Block (Snacks & burgers)
4. **Ashwins Canteen** - Near Clock Tower (Stationery & groceries)
5. **Metro Cafe Canteen** - Near SSN ECE Block (Variety dishes)

### Sample Dishes
- Veg Noodles (₹80-90)
- Chapathi with Kurma (₹35-40)
- Orange Juice (₹50)
- Margherita Pizza (₹140)
- Cheese Burger (₹50)
- Samosa (₹15)
- Pani Puri (₹40)

## 📖 Usage Guide

### Viewing Canteens
1. Open the application in your browser
2. Browse the list of available canteens on the homepage
3. Click "View Menu & Rate" to see dishes in a canteen

### Adding Dishes
1. Navigate to a canteen page
2. Click "+ Add Dish to this Canteen"
3. Fill in dish details (name, description, category, price, rating)
4. Submit the form

### Rating Dishes
1. Go to any canteen page
2. Click "Rate This Dish" on any dish card
3. Select rating from 1-5 stars
4. Click "Submit Rating"

### Editing/Deleting Dishes
1. Go to the canteen containing the dish
2. Click "Edit" on the dish card
3. Modify details and click "Update Dish"
4. Or click "Delete Dish" to remove it

## 🔧 Development

### Backend Development
- The backend uses Spring Data JPA for database operations
- CORS is configured for frontend communication
- Sample data is loaded automatically on first run
- Entities use Jackson annotations for JSON serialization

### Frontend Development
- Vue 3 Composition API
- Reactive state management
- Local storage for recently viewed items
- Session storage for user statistics
- Responsive CSS with mobile-first approach

### Key Features Implementation

#### Session Tracking
```javascript
// Tracks user activity during browser session
sessionStorage.setItem('sessionStats', JSON.stringify(stats))
```

#### Recently Viewed
```javascript
// Maintains recently viewed canteens in localStorage
localStorage.setItem('recentlyViewedCanteens', JSON.stringify(recent))
```

#### Form Validation
- Client-side validation with error messages
- Required field checking
- Price and rating range validation
- Real-time field validation on blur

## 🐛 Troubleshooting

### Common Issues

1. **CORS Errors**
   - Ensure backend CORS configuration includes frontend URL
   - Check `CorsConfig.java` and controller `@CrossOrigin` annotations

2. **Database Connection**
   - Verify PostgreSQL is running
   - Check database credentials in `application.properties`

3. **Frontend-Backend Communication**
   - Ensure both servers are running
   - Verify API endpoints match between frontend and backend

4. **Sample Data Not Loading**
   - Check database connection
   - Verify `DataLoader.java` is executed on application start

## 📝 License

This project is for educational purposes. Feel free to modify and use as needed.

## 👥 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Verify all prerequisites are met
3. Ensure all services are running
4. Check console logs for error messages

---

**Happy Coding! 🚀**

