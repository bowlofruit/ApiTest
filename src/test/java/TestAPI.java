import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class TestAPI {

    // 2. Реалізуйте автоматизований тест, який перевіряє, чи можна створити новий ресурс через ваше API.
    // Перевірте, чи повертається правильний статус-код після створення ресурсу.
    @Test
    public void createTestObj() {
        // Construct the JSON body using a Map
        String jsonString = "{\"id\": 0, \"title\": \"string\", \"dueDate\": \"2024-03-04T19:34:05.119Z\", \"completed\": true}";

        // Send a POST request with the JSON body
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(jsonString)
                .baseUri("https://fakerestapi.azurewebsites.net/api/v1")
                .when()
                .post("/Activities");

        response.then()
                .statusCode(200);
    }

    // 3. Напишіть тести для перевірки аутентифікації через ваше API.
    // Перевірте, чи можна успішно отримати токен доступу або авторизаційний токен.
    @Test
    public void createTestToken() {
        RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .contentType("application/json")
                .when()
                .post("/auth")
                .then()
                .statusCode(200);
    }

    // 4. Створіть тести для перевірки функцій пошуку або фільтрації вашого API.
    // Перевірте, чи повертається очікуваний результат при застосуванні різних параметрів пошуку.
    @Test
    public void getFilteredTestData() {
        // Створення нового унікального ресурсу
        Response createdObj = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .contentType("application/json")
                .body("{\"firstname\": \"CustomText\", \"lastname\": \"Brown\", \"totalprice\": 111, \"depositpaid\": true,\"bookingdates\": {\"checkin\": \"2018-01-01\", \"checkout\": \"2019-01-01\"}, \"additionalneeds\": \"Breakfast\"}")
                .when()
                .post("/booking");
        // Отримання id створенного ресурсу
        int index = createdObj.then().statusCode(200).extract().path("bookingid");

        // Фільтрація ресурсів по імені яке ми тільки що створили
        Response foundIndex = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .contentType("application/json")
                .params("firstname", "CustomText")
                .when()
                .get("/booking");

        // Порівняння отриманого результату з створеним id
        foundIndex.then().statusCode(200)
                .body("[0].bookingid", equalTo(index));
    }

    // 6. Напишіть тести для перевірки авторизованого доступу до захищених ресурсів
    // через ваше API. Перевірте, чи блокується доступ до ресурсів без правильних авторизаційних даних.
    @Test
    public void checkAuthorizationFail() {
        RestAssured.given()
                .baseUri("https://airportgap.com/api")
                .contentType("application/json")
                .when()
                .get("/favorites")
                .then()
                // Намагаючись отримати дані без авторизації очікувано отримуємо 401
                .statusCode(401);
    }

    // 7. Реалізуйте тести для перевірки використання різних HTTP-методів
    // (GET, POST, PUT, DELETE) для різних ресурсів вашого API. Перевірте,
    // чи працюють ці методи правильно та чи повертають вони очікувані результати.
    @Test
    public void checkBasicMethods() {
        RestAssured.given()
                .baseUri("https://fakerestapi.azurewebsites.net/api/v1")
                .contentType("application/json")
                .when()
                .get("/Activities")
                .then()
                .statusCode(200);

        RestAssured.given()
                .baseUri("https://fakerestapi.azurewebsites.net/api/v1")
                .contentType("application/json")
                .body("{\"id\": 0, \"title\": \"string\", \"dueDate\": \"2024-03-04T19:34:05.119Z\", \"completed\": true}")
                .when()
                .post("/Activities")
                .then()
                .statusCode(200);

        RestAssured.given()
                .baseUri("https://fakerestapi.azurewebsites.net/api/v1")
                .contentType("application/json")
                .body("{\"id\": 1, \"title\": \"string\", \"dueDate\": \"2024-03-04T19:34:05.119Z\", \"completed\": true}")
                .when()
                .put("/Activities/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1));

        RestAssured.given()
                .baseUri("https://fakerestapi.azurewebsites.net/api/v1")
                .contentType("application/json")
                .when()
                .delete("/Activities/1")
                .then()
                .statusCode(200);
    }
}
