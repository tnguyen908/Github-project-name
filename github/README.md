# Context

This service allows you to fetch and transform GitHub user data into a simplified JSON format, merging user details and repositories for easy integration into applications.

---

## Architecture

### Key Components
1. **Controller**:
    - Exposes RESTful endpoints for fetching user data.
2. **Service**:
    - Business logic for calling the GitHub API, merging user and repository data, and applying transformations.
3. **Feign Client**:
    - Simplifies HTTP requests to the GitHub API using declarative methods.
4. **Cache**:
    - Reduces API calls by caching frequently requested data using Spring's caching abstraction.
5. **Custom Error Handling**:
    - Handles GitHub API errors gracefully, providing meaningful HTTP status codes and messages.

### Service Flow
1. The client sends a GET request with a GitHub username.
2. The service fetches user details and repositories via the GitHub API using Feign.
3. Data is merged into a custom JSON structure (`GithubUser`).
4. The response is returned to the client.
5. Cached responses are utilized for repeated requests to the same username.

---

## Installation

1. Clone the Repository:
   ```bash
   git clone https://github.com/your-repo/github-user-service.git
   cd github-user-service
   ```

2. Build the Project:
    - Ensure you have Java 17+ and Maven installed.
    - Run:
      ```bash
      mvn clean install
      ```

3. Run the Application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the Service:
    - The service will run at `http://localhost:8080`.

---

## Usage

### API Endpoints

**Fetch User Data**
- **GET** `/github/user/{username}`
    - Example: `GET http://localhost:8080/github/user/octocat`

#### Sample Response:
```json
{
  "user_name": "octocat",
  "display_name": "The Octocat",
  "avatar": "https://avatars.githubusercontent.com/u/583231?v=4",
  "geo_location": "San Francisco",
  "email": null,
  "url": "https://github.com/octocat",
  "created_at": "2011-01-25 18:44:36",
  "repos": [
    {
      "name": "boysenberry-repo-1",
      "url": "https://github.com/octocat/boysenberry-repo-1"
    }
  ]
}
```

---

## Decisions

1. **Feign for HTTP Client**:
    - Feign simplifies API calls by using an interface-based declarative style. This reduces boilerplate code and keeps the client layer clean. Alternatives like `RestTemplate` or `WebClient` would require more manual work for request building, response handling, and error management.

2. **Error Handling with Custom ErrorDecoder**:
    - A custom `ErrorDecoder` maps HTTP errors from GitHub to meaningful exceptions. This ensures all errors are handled consistently and simplifies debugging. Without it, handling errors in each method would be repetitive and error-prone.

3. **Caching with Spring Cache and Caffeine**:
    - Caching API responses reduces the load on GitHub's servers and mitigates rate limits. Using Caffeine for in-memory caching is fast and easy to implement. For distributed deployments, Redis or Memcached would be necessary.

4. **Layered Architecture**:
    - The code is divided into controllers, services, and clients:
        - **Controller** handles HTTP requests and validation.
        - **Service** contains the business logic, such as merging user and repository data.
        - **Client** abstracts external API calls.
    - This separation of concerns ensures the code is easy to maintain, extend, and test.

5. **DTO Pattern**:
    - External API responses (`UserResponse`, `RepoResponse`) are mapped to internal models (`GithubUser`, `Repo`) for flexibility and control over the data structure. This pattern avoids coupling the internal logic to GitHub's API schema, making future changes easier.

6. **Lombok for Reducing Boilerplate**:
    - Lombok annotations (`@Data`, `@AllArgsConstructor`) eliminate getter, setter, and constructor boilerplate for DTOs and models, keeping the codebase concise and readable.

7. **Stateless Design for Scalability**:
    - The service is stateless, making it suitable for horizontal scaling by simply adding more instances. Caching and configurations are externalized, ensuring the service can handle increased traffic with minimal changes.

8. **Observability and Logging**:
    - Logs capture key events, including external API errors and invalid input, to assist in debugging. Logging is configured for debug and production environments to balance verbosity and performance.

9. **Input Validation**:
   - Validating usernames (e.g., regex pattern) prevents unnecessary API calls and improves API efficiency. Defensive programming ensures null or missing fields from the GitHub API are handled gracefully.

10. **Scalability and Performance**:
    - Caching minimizes API calls, while the stateless architecture ensures the service scales horizontally. The modular design allows for easy upgrades, like switching to distributed caching or adding more endpoints.

11. **Production-Readiness**:
    - The design adheres to REST standards, returning appropriate HTTP status codes (`404`, `500`) and human-readable error messages. This makes the service predictable and easy to use for clients.

---