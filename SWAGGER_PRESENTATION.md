# 📖 Swagger API Documentation

## 1. What is Swagger?
**Swagger (OpenAPI)** is an open-source toolset built around the OpenAPI Specification that helps developers design, build, document, and consume RESTful web services. 

In our project, we use **SpringDoc OpenAPI**, which automatically inspects our Spring Boot application at runtime and generates a beautiful, interactive web interface.

## 2. Why Do We Use It?
- **Interactive Documentation**: It provides a live UI where anyone (frontend developers, QA testers, or interviewers) can see all available API endpoints, what inputs they require, and what outputs they produce.
- **Testing Without Postman**: You can click the **"Try it out"** button directly in the browser to send real requests to the backend without needing external tools like Postman or curl.
- **Single Source of Truth**: Because the documentation is generated directly from the Java code (`@RestController`, `@GetMapping`, `@PostMapping`), it is always 100% up-to-date with the actual implementation. It eliminates the problem of outdated wiki pages.
- **Developer Experience**: It drastically speeds up frontend-backend integration because the frontend developer can instantly see exactly how the API expects the JSON payload to be structured.

## 3. How Is It Integrated Here?
The integration is completely automated and lightweight.

**Step 1: Dependency Integration**
We added the `springdoc-openapi-starter-webmvc-ui` dependency to our `pom.xml`. This single library pulls in both the JSON generator and the Swagger UI web interface.

**Step 2: Auto-Configuration**
When the Spring Boot application starts up, SpringDoc scans the classpath for all classes annotated with `@RestController`. It reads all the mapping annotations (like `@PostMapping("/api/v1/materials")`) and inspects the Data Transfer Objects (DTOs) like `MaterialRequest` to determine the expected JSON schema.

**Step 3: Accessing the UI**
Once the backend is running, the interactive documentation is instantly available at:
👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

## 4. How to Demonstrate This to the Interviewer
During your interview, you can show off this integration by following these steps:

1. **Start the Backend**: Make sure the Spring Boot application is running.
2. **Open the Browser**: Navigate to `http://localhost:8080/swagger-ui/index.html`.
3. **Show the Endpoints**: Point out how all the Controllers (e.g., `iso-controller`, `spool-controller`, `material-stock-controller`) are neatly categorized.
4. **Authorize (Optional)**: If you want to make a request, click the **Authorize** padlock button at the top and enter the Basic Auth credentials (`admin` / `admin123`).
5. **Execute a Request**: 
   - Expand the `GET /api/v1/materials` endpoint.
   - Click **Try it out**.
   - Click **Execute**.
   - Show the interviewer the live `200 OK` JSON response containing the materials right there in the browser. 
6. **Highlight the Schemas**: Scroll to the bottom of the page to the **Schemas** section to show how Swagger automatically documented the exact database models and DTO structures.
