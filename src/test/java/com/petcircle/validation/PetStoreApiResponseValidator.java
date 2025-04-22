package com.petcircle.validation;

import com.petcircle.model.petstoreapi.Pet;
import com.petcircle.utils.TestUtils.ResponseValidator;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PetStoreApiResponseValidator {

    @Value("${petstore.swagger.schema-path}")
    private String swaggerSchemaPath;

    public PetStoreApiResponseValidator(String swaggerSchemaPath) {
        this.swaggerSchemaPath = swaggerSchemaPath;
    }

    public void validatePetResponse(Integer expectedStatusCode, Object expected, Response response) {
        ResponseValidator.validateStatusCode(response, expectedStatusCode);
        ResponseValidator.validateSchema(response, swaggerSchemaPath);
        ResponseValidator.validateJsonEquality(expected, response, Pet.class, "Expected and response Pet objects should match");
    }

    public void validatePetResponse(Integer expectedStatusCode,
                                         Response response,
                                         String type,
                                         String message) {
        ResponseValidator.validateStatusCode(response, expectedStatusCode);
        ResponseValidator.validateSchema(response, swaggerSchemaPath);
        ResponseValidator.validateErrorResponse(response, expectedStatusCode, type, message);
    }

    public void validatePetResponseWithEmptyBody(Integer expectedStatusCode, Response response) {
        ResponseValidator.validateStatusCode(response, expectedStatusCode);
        ResponseValidator.validateBodyIsEmpty(response, true);
    }
}
