package com.petcircle.test.petstoreapi;

import com.petcircle.validation.PetStoreApiResponseValidator;
import com.petcircle.model.common.RequestArtifact;
import com.petcircle.service.petstoreapi.PetStoreApiTestService;
import com.petcircle.BaseApiTest;
import com.petcircle.utils.ApiUtils.Operation;
import com.petcircle.utils.ReportUtils.ApiLogger;
import io.restassured.response.Response;
import java.io.IOException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.petcircle.model.common.HttpStatusCodes.*;

@Tags({@Tag("PetStoreApiTest"), @Tag("PetEndpointTest")})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {com.petcircle.Application.class, PetStoreApiConfig.class})
public class PetEndpointTest extends BaseApiTest {

    @Autowired
    private PetStoreApiTestService petStoreApiTestService;
    @Autowired
    private PetStoreApiResponseValidator petStoreApiResponseValidator;

    private RequestArtifact requestArtifact;
    private RequestArtifact requestArtifactClone;

    @BeforeAll
    public void setup() throws IOException {
        requestArtifact = petStoreApiTestService.createNewPetRequestArtifact();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        requestArtifactClone = apiUtil.clone(requestArtifact);
    }

    @Test
    @Order(1)
    void createNewPetTest() throws IOException {
        ApiLogger.startTest("createNewPetSuccessTest", "Verify that a new pet can be created successfully");
        Response response = apiUtil.performOperation(Operation.POST, requestArtifact);
        petStoreApiResponseValidator.validatePetResponse(OK, requestArtifact.getBody(), response);
    }

    @Test
    @Order(2)
    void getExistingPetByIdTest() throws IOException {
        ApiLogger.startTest("getExistingPetByIdTest", "Verify retrieving pet details from the Petstore");
        RequestArtifact getPetRequestArtifact = petStoreApiTestService.petByIdRequestArtifact(2);
        Response response = apiUtil.performOperation(Operation.GET, getPetRequestArtifact);
        petStoreApiResponseValidator.validatePetResponse(OK, requestArtifact.getBody(), response);
    }

    @Test
    @Order(3)
    void updateExistingPetTest() throws IOException {
        ApiLogger.startTest("updateExistingPetTest", "Verify updated pet details");
        RequestArtifact updatePetRequest = petStoreApiTestService.updatePetRequestArtifact();
        Response response = apiUtil.performOperation(Operation.PUT, updatePetRequest);
        petStoreApiResponseValidator.validatePetResponse(OK, requestArtifact.getBody(), response);
    }

    @Test
    @Order(4)
    void deleteExistingPetTest() throws IOException {
        ApiLogger.startTest("deleteExistingPetTest", "Verify successful deletion of pet in Petstore");
        RequestArtifact deletePetRequestArtifact = petStoreApiTestService.petByIdRequestArtifact(5);
        Response response = apiUtil.performOperation(Operation.DELETE, deletePetRequestArtifact);
        petStoreApiResponseValidator.validatePetResponse(OK,
                response,
                "success",
                "Resource with ID 5 deleted successfully");
    }


    @Test
    void errorCreatingNewPetWithNoBodyTest() throws IOException {
        ApiLogger.startTest("errorCreatingNewPetWithNoBodyTest", "Verify the error response for invalid input");
        requestArtifactClone.setBody(null);
        Response response = apiUtil.performOperation(Operation.POST, requestArtifactClone);
        petStoreApiResponseValidator.validatePetResponse(METHOD_NOT_ALLOWED,
                response,
                "unknown",
                "no data");
    }

    @ParameterizedTest
    @CsvSource({
            "0, error, Pet not found",
            "-1, error, Pet not found",
            "1000, error, Pet not found"
    })
    void errorGettingPetByInvalidId(int petId, String expectedType, String expectedMessage) throws IOException {
        ApiLogger.startTest("errorGettingPetByInvalidIdTest - ID: " + petId,
                "Verify the error response for petId = " + petId);

        RequestArtifact getPetRequest = petStoreApiTestService.petByIdRequestArtifact(petId);
        Response response = apiUtil.performOperation(Operation.GET, getPetRequest);

        petStoreApiResponseValidator.validatePetResponse(NOT_FOUND, response, expectedType, expectedMessage);
    }

    @Test
    void errorDeletingExistingPetTest() throws IOException {
        ApiLogger.startTest("errorDeletingExistingPetTest", "Verify error response for deletion of pet in Petstore");
        RequestArtifact deletePetRequestArtifact = petStoreApiTestService.petByIdRequestArtifact(0);
        Response response = apiUtil.performOperation(Operation.DELETE, deletePetRequestArtifact);
        petStoreApiResponseValidator.validatePetResponseWithEmptyBody(NOT_FOUND, response);
    }
}
