# Home Assistant

Home Assistant is a web application that allows you to gather information useful in daily life in one place. Currently, it focuses on managing a home library.

## Requirements

### Frontend
- Node.js
- npm (Node Package Manager)

### Backend
- Java 22
- Maven

## Frontend Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kasiaOsowska/home-assistant.git
2. **Navigate to the project frontend directory:**
   ```bash
   cd home-assistant/homeassistant-frontend
3. **Install the dependencies:**
   ```bash
   npm install
## Running the frontend of application
1. **Start the frontend application:**
   ```bash
   npm start
2. **Frontend of the application should now be accessible at http://localhost:3000**
3. You can also set up login and password for managing home library inside \home-assistant\homeassistant-frontend\src\components\credentials.js
 ## Backend Installation
 
 1. **Download Java 22:**
   - Go to the [Java SE Downloads](https://www.oracle.com/java/technologies/javase-downloads.html) page.
   - Download the appropriate installer for your operating system.
 2. **Install apache maven**
   - Go to the [Maven apache org](https://maven.apache.org/download.cgi) page.
   - set up path variable
   - tutorial https://www.youtube.com/watch?v=YTvlb6eny_0
3. Lastly you need to set up variables **spring.datasource.password, spring.datasource.username, openai.api.key** inside \home-assistant\homeassistant\src\main\resources\application.properties
## Running backend of the application
1. **navigate to root backend folder**
   ```bash
   cd home-assistant\homeassistant
2. **install dependencies**
   ```bash
   mvn clean install
3. **Run backend**
   ```bash
   mvn spring-boot:run
4. **You can see it working on http://localhost:8080/home-assistant/api/books**
