
# API Test Automation Framework (JUnit 5 + Spring Boot + RestAssured + ExtentReport)

This project is a test automation framework built using **Java 8**, **Gradle**, **JUnit 5**, **Spring Boot**, and **ExtentReport**. It provides a structured way to test REST APIs with custom logging for assertions and response validations.

---

## 🛠 Prerequisites

Make sure the following are installed and configured:

### 1. Java 8 (JDK 1.8)
- **Download**: Java 8 JDK
- **Environment Variables**:
    - Set `JAVA_HOME` to point to the JDK installation path.
    - Add `$JAVA_HOME/bin` to your system `PATH`.

```bash
# Example (Linux/macOS):
export JAVA_HOME=/path/to/jdk1.8.0_451
export PATH=$JAVA_HOME/bin:$PATH
```

### 2. Gradle
- **Download**: [Gradle](https://gradle.org/install/)
- Follow the installation instructions for your operating system.

  For Linux/macOS, you can install Gradle using SDKMAN:

  ```bash
  sdk install gradle
  ```

  For Windows, you can install Gradle via the [Windows installer](https://gradle.org/install/) or use a package manager like **Chocolatey**.

- **Environment Variables**:
    - Set `GRADLE_HOME` to point to your Gradle installation path.
    - Add `$GRADLE_HOME/bin` to your system `PATH`.

```bash
# Example (Linux/macOS):
export GRADLE_HOME=/path/to/gradle
export PATH=$GRADLE_HOME/bin:$PATH
```

---

## 🧪 Running the Tests

### ✅ Option 1: IntelliJ Run/Debug Configuration (Recommended)

To run the tests in IntelliJ IDEA:

1. Go to **Run** > **Edit Configurations...**
2. Click the **+** icon and choose **JUnit**.
3. Configure the following:
    - **Name**: `PetStoreApiTest`
    - **Tags**: `PetStoreApiTest`
    - **Test Kind**: `Tags`
    - **JRE**: `Java 8`
    - **VM Options**: `-cp Petcircle.test`
4. Click **Apply** or **OK**.
5. Run the configuration using the green **Run** button.

---

### ▶️ Option 2: Run with Gradle

To run the tests using Gradle:

```bash
gradle clean test
```

This will clean any previous builds and execute the tests.

---

### 📊 Test Results

Test results are logged using **ExtentReports**, and you can view detailed reports after each test execution. Logs include:
- **TestStep: Request and response**
- **Status code**
- **Schema validation**
- **Response body validations**

### Custom Loggers
We use custom `ExtentLogger` and `ConsoleLogger` to log API test results, including assertion outcomes (pass/fail), to both the console and ExtentReports.

### Report Location
Extent report is located at build/reports/extent/


---

## 🔧 Dependencies

The framework depends on the following libraries:
- **JUnit 5**: For unit and integration testing.
- **Spring Boot**: For running API tests with Spring.
- **RestAssured**: For HTTP requests and response validations.
- **Gradle**: For building and running tests.
- **ExtentReports**: For generating detailed HTML reports.
